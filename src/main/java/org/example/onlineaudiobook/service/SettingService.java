package org.example.onlineaudiobook.service;

import lombok.RequiredArgsConstructor;
import org.example.onlineaudiobook.entity.User;
import org.example.onlineaudiobook.repository.UserRepository;

import org.example.onlineaudiobook.requestDto.EditUserDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class SettingService {

    private final UserRepository userRepository;
    private static final String EMAIL_REGEX = "^[\\w-\\.]+@[\\w-]+\\.[a-z]{2,}$";


    public static boolean isValidEmail(String email) {
        Pattern pattern = Pattern.compile(EMAIL_REGEX, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    @Transactional
    public User editUser(Long id, EditUserDTO editUserDto) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("user not found id: " + id));
        if (!isValidEmail(editUserDto.email()))
            throw new RuntimeException("the email is not valid: " + editUserDto.email());

        if (editUserDto.email() != null && userRepository.findByEmail(editUserDto.email()).isPresent())
            if (!user.getEmail().equals(editUserDto.email()))
                throw new RuntimeException("already exist this email");

        if (editUserDto.username() != null && userRepository.findByUsername(editUserDto.username()).isPresent())
            if (!user.getUsername().equals(editUserDto.username()))
                throw new RuntimeException("already exist this username");

        if (editUserDto.phone() != null && userRepository.findByPhone(editUserDto.phone()).isPresent())
            if (!user.getPhone().equals(editUserDto.phone()))
                throw new RuntimeException("already exist this phone");


        user.setEmail(editUserDto.email());
        user.setPhone(Objects.equals(editUserDto.phone(), "") ? null : editUserDto.phone());
        user.setUsername(Objects.equals(editUserDto.username(), "") ? null : editUserDto.username());
        user.setBirthDate(editUserDto.birthDate());
        user.setDisplayName(editUserDto.displayName());
        return userRepository.save(user);
    }
}
