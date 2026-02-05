package com.example.backend.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Value;

@Value
public class AuthLoginRequest {
    @Email
    @NotBlank
    String email;

    @NotBlank
    String password;
}
