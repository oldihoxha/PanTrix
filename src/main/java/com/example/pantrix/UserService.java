package com.example.pantrix;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            return new AuthResponse(false, "Email already registered");
        }

        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            return new AuthResponse(false, "Email is required");
        }
        if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
            return new AuthResponse(false, "Password is required");
        }

        try {
            User newUser = new User(
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                request.getFirstName() != null ? request.getFirstName() : "User",
                request.getLastName() != null ? request.getLastName() : ""
            );

            User savedUser = userRepository.save(newUser);
            String token = jwtTokenProvider.generateToken(savedUser);
            return new AuthResponse(true, "User registered successfully", savedUser, token);
        } catch (Exception e) {
            return new AuthResponse(false, "Registration failed: " + e.getMessage());
        }
    }

    public AuthResponse login(LoginRequest request) {
        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            return new AuthResponse(false, "Email is required");
        }
        if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
            return new AuthResponse(false, "Password is required");
        }

        try {
            Optional<User> userOptional = userRepository.findByEmail(request.getEmail());

            if (userOptional.isEmpty()) {
                return new AuthResponse(false, "User not found");
            }

            User user = userOptional.get();

            if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                String token = jwtTokenProvider.generateToken(user);
                return new AuthResponse(true, "Login successful", user, token);
            } else {
                return new AuthResponse(false, "Invalid password");
            }
        } catch (Exception e) {
            return new AuthResponse(false, "Login failed: " + e.getMessage());
        }
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}

