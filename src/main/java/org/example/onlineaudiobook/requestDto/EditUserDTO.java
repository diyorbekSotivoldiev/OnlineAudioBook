package org.example.onlineaudiobook.requestDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.LocalDate;

public record EditUserDTO(@NotNull @NotBlank String email,
                          @NotNull @NotBlank String displayName,
                          @NotNull @NotBlank String username,
                          @NotNull @NotBlank String phone,
                          @NotNull LocalDate birthDate) implements Serializable {
}