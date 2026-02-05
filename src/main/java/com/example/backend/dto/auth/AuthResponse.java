package com.example.backend.dto.auth;

import com.example.backend.entity.User;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class AuthResponse {
    Long userId;
    String userName;
    String email;
    User.UserRole role;

    // Advanced: Add JWT access token + refresh token here.
}
