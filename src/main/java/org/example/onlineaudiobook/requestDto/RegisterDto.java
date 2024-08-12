package org.example.onlineaudiobook.requestDto;

import java.io.Serializable;
import java.time.LocalDate;

public record RegisterDto(String email,
                          String username,
                          String displayName,
                          String phone,
                          String password,
                          String confirmPassword,
                          LocalDate dateOfBirth) implements Serializable {
}