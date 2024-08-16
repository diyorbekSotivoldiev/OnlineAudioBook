package org.example.onlineaudiobook.requestDto;

import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.LocalDate;

public record RegisterDto(@NotNull String email,
                          String username,
                          String displayName,
                          String phone,
                          @NotNull String password,
                          @NotNull String confirmPassword,
                          LocalDate dateOfBirth) implements Serializable {
}