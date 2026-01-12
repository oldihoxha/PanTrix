package com.example.pantrix;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * Register a new user
     */
    public AuthResponse register(RegisterRequest request) {
        // Check if email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            return new AuthResponse(false, "Email already registered");
        }

        // Validate input
        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            return new AuthResponse(false, "Email is required");
        }
        if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
            return new AuthResponse(false, "Password is required");
        }
        if (request.getFirstName() == null || request.getFirstName().trim().isEmpty()) {
            return new AuthResponse(false, "First name is required");
        }
        if (request.getLastName() == null || request.getLastName().trim().isEmpty()) {
            return new AuthResponse(false, "Last name is required");
        }

        try {
            // Create new user with encrypted password
            User newUser = new User(
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                request.getFirstName(),
                request.getLastName()
            );

            User savedUser = userRepository.save(newUser);
            return new AuthResponse(true, "User registered successfully", savedUser);
        } catch (Exception e) {
            return new AuthResponse(false, "Registration failed: " + e.getMessage());
        }
    }

    /**
     * Login user
     */
    public AuthResponse login(LoginRequest request) {
        // Validate input
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

            // Check if password matches
            if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                return new AuthResponse(true, "Login successful", user);
            } else {
                return new AuthResponse(false, "Invalid password");
            }
        } catch (Exception e) {
            return new AuthResponse(false, "Login failed: " + e.getMessage());
        }
    }

    /**
     * Get user by ID
     */
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    /**
     * Get user by email
     */
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}

