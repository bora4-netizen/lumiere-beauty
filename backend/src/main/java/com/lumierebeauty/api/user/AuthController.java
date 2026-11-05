package com.lumierebeauty.api.user;

import com.lumierebeauty.api.user.dto.AuthResponse;
import com.lumierebeauty.api.user.dto.LoginRequest;
import com.lumierebeauty.api.user.dto.RegisterRequest;
import com.lumierebeauty.api.user.dto.UserResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Authentication endpoints.
 *
 * - POST /api/auth/register — create a CUSTOMER account, returns a JWT
 * - POST /api/auth/login    — verify credentials, returns a JWT
 * - GET /api/auth/me        — current user (requires the Bearer token)
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthResponse register(@Valid @RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @GetMapping("/me")
    public UserResponse me(Authentication authentication) {
        // The JWT filter stores the user's email as the principal name.
        return authService.currentUser(authentication.getName());
    }
}
