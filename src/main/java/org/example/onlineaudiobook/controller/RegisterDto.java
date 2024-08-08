package org.example.onlineaudiobook.controller;

import java.io.Serializable;
import java.time.LocalDate;

public record RegisterDto(String email,
                          String password,
                          String confirmPassword,
                          LocalDate dateOfBirth) implements Serializable {

}