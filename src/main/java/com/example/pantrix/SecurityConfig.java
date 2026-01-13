package com.example.pantrix;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired(required = false)
    private CorsConfigurationSource corsConfigurationSource;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())  // ✅ CSRF komplett deaktivieren für REST API
            .cors(cors -> {
                if (corsConfigurationSource != null) {
                    cors.configurationSource(corsConfigurationSource);
                }
            })  // ✅ CORS mit CorsConfigurationSource aus CorsConfig
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/auth/**").permitAll()  // ✅ Alle Auth-Requests erlaubt
                .requestMatchers("/api/**").permitAll()   // ✅ Alle API-Requests erlaubt
                .requestMatchers("/products/**").permitAll()  // ✅ Fallback Routes
                .requestMatchers("/h2-console/**").permitAll()
                .anyRequest().permitAll()  // ✅ Alles andere auch erlaubt
            )
            .httpBasic(basic -> basic.disable());  // ✅ Keine HTTP Basic Auth

        http.headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()));

        return http.build();
    }
}

