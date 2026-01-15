package com.example.pantrix;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT Authentication Filter - extrahiert und validiert JWT Token aus dem Authorization Header
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            // Extrahiere JWT Token aus dem Authorization Header
            String jwt = extractJwtFromRequest(request);

            if (StringUtils.hasText(jwt)) {
                logger.info("JWT Token gefunden in Authorization Header");

                // Validiere Token
                if (tokenProvider.validateToken(jwt)) {
                    logger.info("JWT Token ist gültig");

                    // Extrahiere User ID aus Token
                    Long userId = tokenProvider.extractUserId(jwt);
                    logger.info("User ID aus Token extrahiert: {}", userId);

                    // Erstelle Authentication Object mit dem Token als credentials
                    UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userId, jwt, null);
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // Setze Authentication in Security Context
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    logger.info("Authentication in Security Context gesetzt für User ID: {}", userId);
                } else {
                    logger.warn("JWT Token ist ungültig oder abgelaufen");
                }
            } else {
                logger.debug("Kein JWT Token im Authorization Header gefunden");
            }
        } catch (Exception ex) {
            logger.error("Fehler beim JWT Authentication Filter: {}", ex.getMessage());
        }

        // Fahre mit der Filter Chain fort
        filterChain.doFilter(request, response);
    }

    /**
     * Extrahiere JWT Token aus dem Authorization Header
     * Format: "Bearer <token>"
     */
    private String extractJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // Entferne "Bearer " Prefix
        }

        return null;
    }
}

