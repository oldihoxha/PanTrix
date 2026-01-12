package com.example.pantrix;

import java.util.Base64;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

    private static final String SECRET = "MySecretKeyForPantrixApplicationTokenGenerationAndValidation123456789";

    public String generateToken(User user) {
        // Einfache Token-Generierung: Base64 kodiert
        String tokenData = user.getId() + ":" + user.getEmail() + ":" + System.currentTimeMillis();
        return Base64.getEncoder().encodeToString(tokenData.getBytes());
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

