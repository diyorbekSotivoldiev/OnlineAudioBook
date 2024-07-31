package org.example.onlineaudiobook.controller;


import lombok.RequiredArgsConstructor;
import org.example.onlineaudiobook.entity.User;
import org.example.onlineaudiobook.repository.UserRepository;
import org.example.onlineaudiobook.security.JwtUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth/refresh/token")
@RequiredArgsConstructor
public class RefreshTokenController {
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @GetMapping
    public String refreshToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (String) authentication.getPrincipal();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("user not found"));
        return jwtUtil.generateToken(user);
    }
}
