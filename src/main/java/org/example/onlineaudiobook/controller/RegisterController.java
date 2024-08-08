package org.example.onlineaudiobook.controller;

import lombok.RequiredArgsConstructor;
import org.example.onlineaudiobook.entity.Book;
import org.example.onlineaudiobook.service.UserService;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class RegisterController {
    private final UserService userService;

    @PostMapping
    public HttpEntity<?> saveUser(@RequestBody RegisterDto registerDto) {
        //some logic
        return ResponseEntity.ok("gmail ga sms yuborildi");
    }

}
