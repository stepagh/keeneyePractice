package dev.keeneye.scheduler;

import dev.keeneye.entities.OutboxEmail;
import dev.keeneye.enums.OutboxStatus;
import dev.keeneye.repositories.OutboxEmailRepository;
import dev.keeneye.services.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class OutboxEmailScheduler {

    private final OutboxEmailRepository outboxRepository;
    private final EmailService emailService;

    @Scheduled(fixedDelay = 5000)
    @Transactional
    public void processOutbox() {
        List<OutboxEmail> messages = outboxRepository.findTop10ByStatusOrderByCreatedAtAsc(OutboxStatus.PENDING);

        if (messages.isEmpty()) {
            return;
        }

        log.info("Найдено {} писем в Outbox для обработки", messages.size());

        for (OutboxEmail message : messages) {
            try {


                switch (message.getType()) {
                    case REGISTRATION_CONFIRMATION ->
                        emailService.sendConfirmationEmail(
                                message.getEmail(),
                                message.getConfirmationToken()
                        );
                    case REGISTRATION_SUCCESS ->
                        emailService.sendSuccessfulRegistrationEmail(
                                message.getEmail(),
                                message.getUsername(),
                                message.getTempPassword()
                        );
                }

                    message.setStatus(OutboxStatus.PROCESSED);
                    message.setProcessedAt(Instant.now());

            } catch (Exception e) {
                log.error("Ошибка при отправке письма Outbox ID: {}", message.getId(), e);

                int currentRetries = message.getRetryCount() + 1;
                message.setRetryCount(currentRetries);

                if (currentRetries >= 3) {
                    message.setStatus(OutboxStatus.FAILED);
                }
            }
            outboxRepository.save(message);
        }
    }
}