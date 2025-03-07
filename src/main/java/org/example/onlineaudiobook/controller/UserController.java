package org.example.onlineaudiobook.controller;

import lombok.RequiredArgsConstructor;
import org.example.onlineaudiobook.repository.UserRepository;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api1/user")
public class UserController {
    private final UserRepository userRepository;

    @GetMapping("{id}")
    public HttpEntity<?> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(userRepository.findById(id).orElseThrow(() -> new RuntimeException("user not found")));
    }

    @GetMapping("getAll")
    public HttpEntity<?> getAll() {
        return ResponseEntity.ok(userRepository.findAll());
    }
}
