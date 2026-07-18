package dev.keeneye.services;

import dev.keeneye.dto.UserCsvDto;
import dev.keeneye.entities.OutboxEmail;
import dev.keeneye.entities.RegistrationApplication;
import dev.keeneye.exceptions.CsvProcessingException;
import dev.keeneye.exceptions.CsvRowProcessingException;
import dev.keeneye.exceptions.UniqueConstraintException;
import dev.keeneye.mappers.RegistrationApplicationMapper;
import dev.keeneye.repositories.GroupRepository;
import dev.keeneye.repositories.OutboxEmailRepository;
import dev.keeneye.repositories.RegistrationApplicationRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RowProcessor {

    private final RegistrationApplicationRepository applicationRepository;
    private final OutboxEmailRepository outboxEmailRepository;
    private final RegistrationApplicationMapper registrationApplicationMapper;
    private final Validator validator;
    private final GroupRepository groupRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public boolean processSingleRow(UserCsvDto dto, int expirationHours, int lineNumber, List<String> errors) {

        Set<ConstraintViolation<UserCsvDto>> violations = validator.validate(dto);

        if (!violations.isEmpty()) {
            for (ConstraintViolation<UserCsvDto> violation : violations) {
                errors.add("Строка %d: %s".formatted(lineNumber, violation.getMessage()));
            }
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }
        if (!groupRepository.existsByName(dto.getGroupName())) {
            errors.add("Строка %d: группы с названием %s не найдено".formatted(lineNumber, dto.getGroupName()));
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }

        try {
            RegistrationApplication application = registrationApplicationMapper.toEntity(dto, expirationHours);
            applicationRepository.save(application);

            OutboxEmail confirmationTask = new OutboxEmail(application.getEmail(), application.getConfirmationToken());
            outboxEmailRepository.save(confirmationTask);

        } catch (DataIntegrityViolationException e ) {
            errors.add("Строка %d (%s): Заявка на регистрацию с таким email уже существует.");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        } catch (IllegalArgumentException e ) {
            errors.add("Строка %d (%s): Неверно указана роль. Ожидается 'ROLE_STUDENT', 'ROLE_PROFESSOR' или 'ROLE_ADMIN'".formatted(lineNumber, dto.getRole()));
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        } catch (Exception e) {
            errors.add("Строка %d: Непредвиденная ошибка при сохранении: %s".formatted(lineNumber, e.getMessage()));
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }
        return true;
    }
}