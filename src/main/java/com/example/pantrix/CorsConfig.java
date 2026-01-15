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

        // Erlaubte Ursprünge - ALLE localhost und *.onrender.com
        configuration.setAllowedOrigins(Arrays.asList(
            "http://localhost:5173",
            "http://localhost:5713",
            "http://localhost:8080",
            "http://localhost:3000",
            "https://*.onrender.com",  // Alle Render-URLs
            "https://pantrix-frontnd.onrender.com",
            "https://pantrix-frontend.onrender.com"
        ));

        // Erlaubte HTTP-Methoden
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));

        // Erlaubte Header
        configuration.setAllowedHeaders(Arrays.asList("*"));

        // Exponierte Header (WICHTIG für Authorization!)
        configuration.setExposedHeaders(Arrays.asList("Authorization", "Content-Type", "X-Total-Count"));

        // Credentials erlauben - WICHTIG für Token!
        configuration.setAllowCredentials(true);

        // Max Age für CORS Preflight Cache (in Sekunden)
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}

