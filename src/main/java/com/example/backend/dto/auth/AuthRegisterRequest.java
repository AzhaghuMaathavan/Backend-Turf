package com.example.backend.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Value;

@Value
public class AuthRegisterRequest {
    @NotBlank
    @Size(min = 3, max = 40)
    String userName;

    @Email
    @NotBlank
    String email;

    @NotBlank
    @Size(min = 4, max = 80)
    String password;
}
