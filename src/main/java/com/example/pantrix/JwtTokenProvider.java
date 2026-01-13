package com.example.pantrix;

import java.util.Base64;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

    private static final String SECRET = "MySecretKeyForPantrixApplicationTokenGenerationAndValidation123456789";

    public String generateToken(User user) {
        // Einfache Token-Generierung: Base64 kodiert
        if (user == null || user.getEmail() == null) {
            throw new IllegalArgumentException("User und Email dürfen nicht null sein");
        }

        Long userId = user.getId() != null ? user.getId() : 0L;
        String tokenData = userId + ":" + user.getEmail() + ":" + System.currentTimeMillis();
        return Base64.getEncoder().encodeToString(tokenData.getBytes());
    }

    public Long extractUserId(String token) {
        try {
            String decoded = new String(Base64.getDecoder().decode(token));
            String[] parts = decoded.split(":");
            return parts.length > 0 ? Long.parseLong(parts[0]) : null;
        } catch (Exception e) {
            return null;
        }
    }

    public String extractEmail(String token) {
        try {
            String decoded = new String(Base64.getDecoder().decode(token));
            String[] parts = decoded.split(":");
            return parts.length > 1 ? parts[1] : null;
        } catch (Exception e) {
            return null;
        }
    }

    public boolean validateToken(String token) {
        try {
            String decoded = new String(Base64.getDecoder().decode(token));
            return decoded.split(":").length == 3;
        } catch (Exception e) {
            return false;
        }
    }
}

