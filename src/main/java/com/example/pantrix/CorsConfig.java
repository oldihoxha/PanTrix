package com.example.pantrix;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
public class CorsConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Erlaubte Ursprünge
        configuration.setAllowedOrigins(Arrays.asList(
            "http://localhost:5173",
            "http://localhost:5713",
            "http://localhost:8080",
            "https://pantrix-frontnd.onrender.com"
        ));

        // Erlaubte HTTP-Methoden
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));

        // Erlaubte Header
        configuration.setAllowedHeaders(Arrays.asList("*"));

        // Exponierte Header (wichtig für Tokens)
        configuration.setExposedHeaders(Arrays.asList("Authorization", "Content-Type"));

        // Credentials erlauben
        configuration.setAllowCredentials(true);

        // Max Age für CORS Preflight Cache (in Sekunden)
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}

