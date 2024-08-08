package org.example.onlineaudiobook.service;

import lombok.RequiredArgsConstructor;
import org.example.onlineaudiobook.controller.RegisterDto;
import org.example.onlineaudiobook.repository.UserRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final EmailService emailService;

    @Async
    public void checkConfirmPasswordAndSendEmail(RegisterDto registerDto) {
        if (registerDto.password().equals(registerDto.confirmPassword())) {
            int code = ThreadLocalRandom.current().nextInt(1000, 10000);
            emailService.sendSimpleEmail(registerDto.email(), "Ushbu kodni hech kimga bermang", String.valueOf(code));
        }
    }

}
