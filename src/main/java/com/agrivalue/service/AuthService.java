package com.agrivalue.service;

import com.agrivalue.dto.AuthResponse;
import com.agrivalue.dto.LoginRequest;
import com.agrivalue.dto.RegisterRequest;
import com.agrivalue.exception.PasswordValidationException;
import com.agrivalue.model.User;
import com.agrivalue.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request) {
        validatePassword(request.getPassword());
        var user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();
        userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthResponse.builder()
                .token(jwtToken)
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        return AuthResponse.builder()
                .token(jwtToken)
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }

    private void validatePassword(String password) {
        if (password == null || password.length() < 8) {
            throw new PasswordValidationException("Password must be at least 8 characters long.");
        }
        if (!password.matches(".*[A-Z].*")) {
            throw new PasswordValidationException("Password must contain at least one uppercase letter (A-Z).");
        }
        if (!password.matches(".*[0-9].*")) {
            throw new PasswordValidationException("Password must contain at least one number (0-9).");
        }
        if (!password.matches(".*[@$!%*?&#^].*")) {
            throw new PasswordValidationException("Password must contain at least one special character (@$!%*?&#^).");
        }
    }
}
