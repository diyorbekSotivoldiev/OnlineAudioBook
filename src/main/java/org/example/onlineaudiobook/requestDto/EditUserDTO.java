package org.example.onlineaudiobook.requestDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.LocalDate;

public record EditUserDTO(@NotNull @NotBlank String email,
                          String displayName,
                          String username,
                          String phone,
                          LocalDate birthDate) implements Serializable {
}