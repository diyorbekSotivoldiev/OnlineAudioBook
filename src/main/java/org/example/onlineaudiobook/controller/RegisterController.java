package org.example.onlineaudiobook.controller;

import lombok.RequiredArgsConstructor;
import org.example.onlineaudiobook.entity.User;
import org.example.onlineaudiobook.requestDto.MailCodeDTO;
import org.example.onlineaudiobook.requestDto.RegisterDto;
import org.example.onlineaudiobook.service.UserService;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/register")
public class RegisterController {
    private final UserService userService;

    @PostMapping
    public HttpEntity<?> checkPassSendEmail(@RequestBody RegisterDto registerDto) {
        System.out.println(registerDto);
        User user = userService.checkConfirmPasswordAndSendEmail(registerDto);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/checkMailCode")
    public ResponseEntity<?> saveActiveUser(@RequestBody MailCodeDTO mailCodeDTO) {
        User user = userService.saveActiveUser(mailCodeDTO);
        Map<String, User> response = new HashMap<>();
        response.put("activated user ✔", user);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/resendMail")
    public ResponseEntity<?> saveActiveUser(@RequestParam Long userId, @RequestParam String email) {
        userService.resendEmail(userId, email);
        return ResponseEntity.ok("kod yuborildi✔");
    }
}
