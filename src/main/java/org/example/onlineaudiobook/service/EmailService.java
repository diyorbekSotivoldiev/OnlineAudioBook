package org.example.onlineaudiobook.service;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    @Value("${spring.mail.username}")
    String mail;
    private final JavaMailSender mailSender;

    @Async
    public void sendSimpleEmail(@NotNull String toEmail, @NotNull String subject, @NotNull String body) throws MailException {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject(subject);
            message.setText(body);
            message.setFrom(mail);
            mailSender.send(message);
        } catch (MailException e) {
            throw new MailException("gmailga sms yuborishda xatolik yuz berdi") {
                @Override
                public String getMessage() {
                    return super.getMessage();
                }
            };
        } catch (RuntimeException e) {
            throw new RuntimeException("gmailga sms yuborishda xatolik yuz berdi");
        }
    }
}
