package dev.keeneye.services;

import dev.keeneye.dto.CsvProcessingResult;
import dev.keeneye.dto.UserCsvDto;
import dev.keeneye.entities.*;
import dev.keeneye.enums.ApplicationStatus;
import dev.keeneye.exceptions.CsvProcessingException;
import dev.keeneye.exceptions.ResourceNotFoundException;
import dev.keeneye.exceptions.UniqueConstraintException;
import dev.keeneye.mappers.ProfessorMapper;
import dev.keeneye.mappers.RegistrationApplicationMapper;
import dev.keeneye.mappers.StudentMapper;
import dev.keeneye.mappers.UserMapper;
import dev.keeneye.repositories.*;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegistrationApplicationService {

    private final RegistrationApplicationRepository applicationRepository;
    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final AdminRepository adminRepository;
    private final ProfessorRepository professorRepository;
    private final Validator validator;
    private final EmailService emailService;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final GroupRepository groupRepository;
    private final StudentMapper studentMapper;
    private final ProfessorMapper professorMapper;
    private final RegistrationApplicationMapper registrationApplicationMapper;
    private final OutboxEmailRepository outboxEmailRepository;

    @Value("${keeneye.registration-expiry-hours}")
    private int expirationHours;

    @Transactional
    public CsvProcessingResult processRegistrationApplications(List<UserCsvDto> dtos) {
        List<String> errors = new ArrayList<>();
        int successCount = 0;

        for (int i = 0; i < dtos.size(); i++) {
            int lineNumber = i + 1;
            UserCsvDto dto = dtos.get(i);

            Set<ConstraintViolation<UserCsvDto>> violations = validator.validate(dto);
            if (!violations.isEmpty()) {
                for (ConstraintViolation<UserCsvDto> violation : violations) {
                    errors.add("Строка %d: %s".formatted(lineNumber, violation.getMessage()));
                }
                continue;
            }
            if (!applicationRepository.existsByGroupName(dto.getGroupName())) {
                errors.add("Строка %d: группы с названием %s не найдено".formatted(lineNumber, dto.getGroupName()));
            }

            try {
                RegistrationApplication application = registrationApplicationMapper.toEntity(dto, expirationHours);

                applicationRepository.saveAndFlush(application);

                successCount++;

                OutboxEmail confirmationTask = new OutboxEmail(application.getEmail(), application.getConfirmationToken());
                outboxEmailRepository.save(confirmationTask);

            } catch (DataIntegrityViolationException e) {
                throw new UniqueConstraintException("Unique field dublicate in DB (Registration Application with this email already exists)");
            } catch (IllegalArgumentException e) {
                errors.add("Строка %d (%s): Неверно указана роль. Ожидается 'ROLE_STUDENT', 'ROLE_PROFESSOR' или 'ROLE_ADMIN'".formatted(lineNumber, dto.getRole()));
            } catch (Exception e) {
                errors.add("Строка %d: Непредвиденная ошибка при сохранении: %s".formatted(lineNumber, e.getMessage()));
            }
        }
        if (!errors.isEmpty()) {
            throw new CsvProcessingException("Csv processing failure.Successfully processed lines:" + successCount, errors);
        }
        return new CsvProcessingResult(successCount, errors);
    }

    @Transactional
    public void confirmRegistration(String token) {
        RegistrationApplication application = applicationRepository.findByConfirmationToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("Confirmation token not found"));


        if (application.getExpiryDate().isBefore(Instant.now())) {
            applicationRepository.delete(application);
            throw new IllegalStateException("Application is expired");
        }

        String tempPassword = UUID.randomUUID().toString();
        String encodedPassword = passwordEncoder.encode(tempPassword);
        User newUser = userMapper.toEntity(application, encodedPassword);

        try {
            userRepository.saveAndFlush(newUser);
        } catch (DataIntegrityViolationException e) {
            throw new UniqueConstraintException("Unique field dublicate in DB (User with this username or email already exists)");
        }

        Group group = groupRepository.findByName(application.getGroupName())
                .orElseThrow(() -> new ResourceNotFoundException("Group with name: %s not found".formatted(application.getGroupName())));

        switch (application.getRole()) {
            case ROLE_STUDENT -> {
                Student student = studentMapper.toEntity(application, newUser, group);
                studentRepository.save(student);
            }
            case ROLE_PROFESSOR -> {
                Professor professor = professorMapper.toEntity(application, newUser, group);
                professorRepository.save(professor);
            }
            case ROLE_ADMIN -> {
                Administrator admin = new Administrator();
                admin.setUser(newUser);
                admin.setUsername(newUser.getUsername());
                adminRepository.save(admin);
            }
        }
        applicationRepository.delete(application);
        OutboxEmail successTask = new OutboxEmail(newUser.getEmail(), newUser.getUsername(), tempPassword);
    }
}