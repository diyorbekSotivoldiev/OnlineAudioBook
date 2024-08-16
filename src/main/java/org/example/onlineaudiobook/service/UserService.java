package org.example.onlineaudiobook.service;

import io.jsonwebtoken.io.CodecException;
import lombok.RequiredArgsConstructor;
import org.example.onlineaudiobook.entity.CodesMail;
import org.example.onlineaudiobook.entity.User;
import org.example.onlineaudiobook.handler.exceptions.AlreadyExist;
import org.example.onlineaudiobook.handler.exceptions.PasswordMismatchException;
import org.example.onlineaudiobook.repository.CodesMailRepository;
import org.example.onlineaudiobook.requestDto.MailCodeDTO;
import org.example.onlineaudiobook.requestDto.RegisterDto;
import org.example.onlineaudiobook.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final CodesMailRepository codesMailRepository;
    private static final String EMAIL_REGEX = "^[\\w-\\.]+@[\\w-]+\\.[a-z]{2,}$";
    private final PasswordEncoder passwordEncoder;

    public static boolean isValidEmail(String email) {
        Pattern pattern = Pattern.compile(EMAIL_REGEX, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public User checkConfirmPasswordAndSendEmail(RegisterDto registerDto) {
        if (registerDto.password().equals(registerDto.confirmPassword())) {

            if (!isValidEmail(registerDto.email()))
                throw new RuntimeException("email xato kiritildi");
            if (userRepository.existsByEmail(registerDto.email()))
                throw new AlreadyExist("user EMAIL already exist");
            if (registerDto.phone() != null && userRepository.existsByPhone(registerDto.phone()))
                throw new AlreadyExist("user PHONE already exist");
            if (registerDto.username() != null && userRepository.existsByUsername(registerDto.username()))
                throw new AlreadyExist("user USERNAME already exist");

            String code = String.valueOf(ThreadLocalRandom.current().nextInt(1000, 10000));
            emailService.sendSimpleEmail(registerDto.email(), "Ushbu kodni hech kimga bermang", code);
            User save = userRepository.save(User.builder()
                    .username(registerDto.username())
                    .displayName(registerDto.displayName())
                    .phone(registerDto.phone())
                    .password(passwordEncoder.encode(registerDto.password()))
                    .birthDate(registerDto.dateOfBirth())
                    .email(registerDto.email()).build());
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
