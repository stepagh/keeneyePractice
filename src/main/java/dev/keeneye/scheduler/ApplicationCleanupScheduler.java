package dev.keeneye.scheduler;

import dev.keeneye.repositories.RegistrationApplicationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;

@Component
@Slf4j
@RequiredArgsConstructor
public class ApplicationCleanupScheduler {

    private final RegistrationApplicationRepository applicationRepository;

    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void cleanupExpiredApplications() {
        log.info("Запуск плановой очистки протухших заявок регистрации");

        long deletedCount = applicationRepository.deleteAllByExpiryDateBefore(Instant.now());

        log.info("Успешно удалено протухших заявок: {}", deletedCount);
    }
}