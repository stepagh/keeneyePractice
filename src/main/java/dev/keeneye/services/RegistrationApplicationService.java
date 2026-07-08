package dev.keeneye.services;

import dev.keeneye.dto.CsvProcessingResult;
import dev.keeneye.dto.UserCsvDto;
import dev.keeneye.entities.*;
import dev.keeneye.enums.ApplicationStatus;
import dev.keeneye.enums.Role;
import dev.keeneye.exceptions.InvalidFileFormatException;
import dev.keeneye.exceptions.ResourceNotFoundException;
import dev.keeneye.mappers.ProfessorMapper;
import dev.keeneye.mappers.StudentMapper;
import dev.keeneye.mappers.UserMapper;
import dev.keeneye.repositories.*;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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

            if (userRepository.existsByEmail(dto.getEmail())) {
                errors.add("Строка %d (%s): Пользователь с таким Email уже зарегистрирован в системе".formatted(lineNumber, dto.getEmail()));
                continue;
            }

            if (applicationRepository.existsByEmail(dto.getEmail())) {
                errors.add("Строка %d (%s): Заявка на этот Email уже была создана ранее и ожидает подтверждения".formatted(lineNumber, dto.getEmail()));
                continue;
            }

            try {
                RegistrationApplication application = RegistrationApplication.from(dto, 24);

                applicationRepository.save(application);
                successCount++;
                emailService.sendConfirmationEmail(
                        application.getEmail(),
                        application.getConfirmationToken()
                );


            } catch (IllegalArgumentException e) {
                errors.add("Строка %d (%s): Неверно указана роль. Ожидается 'ROLE_STUDENT', 'ROLE_PROFESSOR' или 'ROLE_ADMIN'".formatted(lineNumber, dto.getRole()));
            } catch (Exception e) {
                errors.add("Строка %d: Непредвиденная ошибка при сохранении: %s".formatted(lineNumber, e.getMessage()));
            }
        }

        return new CsvProcessingResult(successCount, errors);
    }

    @Transactional
    public void confirmRegistration(String token) {
        RegistrationApplication application = applicationRepository.findByConfirmationToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("Confirmation token not found"));

        if (application.getStatus() == ApplicationStatus.CONFIRMED) {
            throw new IllegalStateException("The application was already confirmed earlier");
        }
        if (application.getStatus() == ApplicationStatus.EXPIRED || application.getExpiryDate().isBefore(Instant.now())) {
            application.setStatus(ApplicationStatus.EXPIRED);
            throw new IllegalStateException("Application is expired");
        }

        if (userRepository.existsByEmail(application.getEmail())) {
            application.setStatus(ApplicationStatus.CONFIRMED);
            throw new IllegalStateException("User with this email already exists");
        }

        User newUser = userMapper.toEntity(application);
        String tempPassword = UUID.randomUUID().toString();
        newUser.setPassword(passwordEncoder.encode(tempPassword));
        userRepository.saveAndFlush(newUser);
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
        emailService.sendSuccsesfulRegistrationEmail(newUser.getEmail(),newUser.getUsername(), tempPassword);
        application.setStatus(ApplicationStatus.CONFIRMED);
    }
}