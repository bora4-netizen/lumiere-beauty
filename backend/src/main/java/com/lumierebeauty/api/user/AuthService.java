package com.lumierebeauty.api.user;

import com.lumierebeauty.api.common.DuplicateEmailException;
import com.lumierebeauty.api.common.ResourceNotFoundException;
import com.lumierebeauty.api.security.JwtService;
import com.lumierebeauty.api.user.dto.LoginRequest;
import com.lumierebeauty.api.user.dto.RegisterRequest;
import com.lumierebeauty.api.user.dto.AuthResponse;
import com.lumierebeauty.api.user.dto.UserResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Registration, login and "who am I" lookup.
 *
 * All users register as CUSTOMER; the seeded admin account (and any manually
 * promoted user) holds the ADMIN role that unlocks the /api/admin/** area.
 */
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager,
                       JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        String email = request.email().trim().toLowerCase();
        if (userRepository.existsByEmail(email)) {
            throw new DuplicateEmailException("An account with email " + email + " already exists.");
        }

        User user = new User(
                request.name().trim(),
                email,
                passwordEncoder.encode(request.password()),
                Role.CUSTOMER);
        userRepository.save(user);

        return buildAuthResponse(user);
    }

    public AuthResponse login(LoginRequest request) {
        // Throws BadCredentialsException (mapped to HTTP 401) when credentials are wrong.
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email().trim().toLowerCase(),
                        request.password()));

        User user = userRepository.findByEmail(request.email().trim().toLowerCase())
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));

        return buildAuthResponse(user);
    }

    @Transactional(readOnly = true)
    public UserResponse currentUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));
        return UserResponse.from(user);
    }

    private AuthResponse buildAuthResponse(User user) {
        return new AuthResponse(
                jwtService.generateToken(user),
                "Bearer",
                jwtService.getExpirationMs(),
                UserResponse.from(user));
    }
}
