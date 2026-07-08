package dev.keeneye.services;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    @Async("mailExecutor")
    public void sendConfirmationEmail(String toEmail, String token) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(toEmail);
            helper.setSubject("Приглашение на регистрацию в системе");

            String confirmationUrl = "http://localhost:8080/api/v1/auth/register/confirm?token=" + token;

            helper.setText(confirmationUrl, false);

            mailSender.send(message);
            log.info("Письмо на подтверждение регистрации на почту {} отправлено", toEmail);
        } catch (Exception e) {
            log.error("Не удалось отправить письмо на {}", toEmail, e);
        }
    }

    @Async("mailExecutor")
    public void sendSuccsesfulRegistrationEmail(String toEmail, String username, String tempPassword) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(toEmail);
            helper.setSubject("Вы успешно зарегестрированы в системе");
            helper.setText("""
                    Ваш логин: %s
                    Ваш временный пароль: %s
                    Поменяйте его после входа в аккаунт.
                    """.formatted(username, tempPassword));
            mailSender.send(message);
            log.info("Письмо об успешной регистрации на почту {} отправлено", toEmail);
        } catch (Exception e) {
            log.error("Не удалось отправить письмо на {}", toEmail, e);
        }
    }
}