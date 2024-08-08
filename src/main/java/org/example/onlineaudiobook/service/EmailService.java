package org.example.onlineaudiobook.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    @Value("${spring.mail.username}")
    String mail;
    private final JavaMailSender mailSender;

    public void sendSimpleEmail(String toEmail, String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject(subject);
            message.setText(body);
            message.setFrom(mail);
            mailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
