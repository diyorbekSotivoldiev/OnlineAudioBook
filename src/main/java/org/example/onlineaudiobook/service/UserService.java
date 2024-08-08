package org.example.onlineaudiobook.service;

import io.jsonwebtoken.io.CodecException;
import lombok.RequiredArgsConstructor;
import org.example.onlineaudiobook.entity.CodesMail;
import org.example.onlineaudiobook.entity.User;
import org.example.onlineaudiobook.handler.exceptions.PasswordMismatchException;
import org.example.onlineaudiobook.repository.CodesMailRepository;
import org.example.onlineaudiobook.requestDto.MailCodeDTO;
import org.example.onlineaudiobook.requestDto.RegisterDto;
import org.example.onlineaudiobook.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final CodesMailRepository codesMailRepository;

    public User checkConfirmPasswordAndSendEmail(RegisterDto registerDto) {
        if (registerDto.password().equals(registerDto.confirmPassword())) {
            String code = String.valueOf(ThreadLocalRandom.current().nextInt(1000, 10000));
            System.out.println("gmail code: " + code);
            emailService.sendSimpleEmail(registerDto.email(), "Ushbu kodni hech kimga bermang", code);
            User save = userRepository.save(User.builder().password(registerDto.password()).birthDate(registerDto.dateOfBirth()).email(registerDto.email()).build());
            codesMailRepository.deleteAllByUserId(save.getId());
            codesMailRepository.save(new CodesMail(save, code));
            return save;
        } else {
            throw new PasswordMismatchException("password tasdiqlanmadi!");
        }
    }

    @Transactional
    public User saveActiveUser(MailCodeDTO mailCodeDTO) {
        User user = userRepository.findById(mailCodeDTO.userId()).orElseThrow(() -> new RuntimeException("userNotfound"));
        CodesMail codesMail = codesMailRepository.findByUserId(mailCodeDTO.userId()).orElseThrow(() -> new RuntimeException("invalid code"));
        System.out.println(codesMail.getCode() + "  " + mailCodeDTO.code());
        if (codesMail.getCode().equals(mailCodeDTO.code())) {
            user.setActive(true);
            User save = userRepository.save(user);
            codesMailRepository.deleteAllByUserId(save.getId());
            return user;
        } else {
            throw new CodecException("xato kod kiritildi");
        }
    }

    public void resendEmail(Long userId, String email) {
        try {
            User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("userNotfound"));
            codesMailRepository.deleteAllByUserId(user.getId());
            String code = String.valueOf(ThreadLocalRandom.current().nextInt(1000, 10000));
            codesMailRepository.save(new CodesMail(user, code));
            emailService.sendSimpleEmail(email, "Bu kodni hech kimga bermang!", code);
        } catch (RuntimeException e) {
            throw new RuntimeException("kod yuborishda xatolik");
        }
    }
}
