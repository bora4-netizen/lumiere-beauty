package com.lumierebeauty.api.user.dto;

import com.lumierebeauty.api.user.User;

/** User data sent to the frontend. Never includes the password hash. */
public record UserResponse(
        Long id,
        String name,
        String email,
        String role
) {
    public static UserResponse from(User user) {
        return new UserResponse(user.getId(), user.getName(), user.getEmail(), user.getRole().name());
    }
}
