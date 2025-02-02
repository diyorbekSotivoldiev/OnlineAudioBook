package org.example.onlineaudiobook.controller;

import lombok.RequiredArgsConstructor;
import org.example.onlineaudiobook.entity.User;
import org.example.onlineaudiobook.repository.UserRepository;
import org.example.onlineaudiobook.requestDto.LoginRequestDTO;
import org.example.onlineaudiobook.requestDto.TokenDTO;
import org.example.onlineaudiobook.responseDto.OauthClientDTO;
import org.example.onlineaudiobook.security.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping("/api1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @Value("${spring.oauth.auth-url}")
    private String authUrl;
    @Value("${spring.oauth.client-id}")
    private String clientId;
    @Value("${spring.oauth.redirect-uri}")
    private String redirectUri;
    @Value("${spring.oauth.client-secret}")
    private String clientSecret;
    @Value("${spring.oauth.token-url}")
    private String tokenUrl;
    @Value("${spring.oauth.user-info-url}")
    private String userInfoUrl;
    private final UserRepository userRepository;


    @PostMapping("/login")
    public TokenDTO login(@RequestBody LoginRequestDTO loginRequestDTO) {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequestDTO.getEmail(), loginRequestDTO.getPassword()));
        System.out.println("successfully auth");
        UserDetails userDetails = (UserDetails) authenticate.getPrincipal();
        String accessToken = jwtUtil.generateToken(userDetails);
        String refreshToken = jwtUtil.generateRefreshToken(userDetails);
        return new TokenDTO(accessToken, refreshToken);
    }

    @GetMapping("/oauth2")
    public ResponseEntity<?> initiateLogin() {
        String authorizationUrl = authUrl + "?client_id=" + clientId + "&redirect_uri=" + redirectUri + "&response_type=code&scope=openid%20profile%20email";
        Map<String, String> response = new HashMap<>();
        response.put("authorizationUrl", authorizationUrl);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/oauth2/callback")
    public ResponseEntity<?> callback(@RequestParam String code) {
        RestTemplate restTemplate = new RestTemplate();
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("code", code);
        params.add("grant_type", "authorization_code");
        params.add("redirect_uri", redirectUri);

        var response = restTemplate.postForEntity(tokenUrl, params, Map.class);
        Map<String, String> tokens = response.getBody();
        String googleAccessToken = tokens.get("access_token");
        String email = fetchUserInfo(googleAccessToken);
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()){
            userRepository.save(new User(null, email, "newUser", "newUser", passwordEncoder.encode("newPassword"), "phone", LocalDate.now(),null, false,null));
        }
        String accessToken = jwtUtil.generateToken(email);
        String refreshToken = jwtUtil.generateRefreshToken(email);

        return ResponseEntity
                .status(301)
                .location(URI.create("http://localhost:63342/OnlineAudioBook/src/main/resources/templates/login.html?_ijt=vo5vb6sbn8gpsn6g691nach2n3&_ij_reload=RELOAD_ON_SAVE&accessToken=" + accessToken + "&refreshToken=" + refreshToken))
                .build();

    }

    private String fetchUserInfo(String googleAccessToken) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "Bearer " + googleAccessToken);
        HttpEntity<String> entity = new HttpEntity<>(httpHeaders);
        ResponseEntity<OauthClientDTO> response = restTemplate.exchange(
                userInfoUrl,
                HttpMethod.GET,
                entity,
                OauthClientDTO.class

        );
        OauthClientDTO userInfo = response.getBody();
        System.out.println(userInfo);
        assert userInfo != null;
        return userInfo.getEmail();
    }
}