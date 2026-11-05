package com.lumierebeauty.api.user.dto;

/** Body returned by POST /api/auth/register, POST /api/auth/login and GET /api/auth/me. */
public record AuthResponse(
        String token,
        String tokenType,
        long expiresInMs,
        UserResponse user
) {
}
