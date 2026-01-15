package com.example.pantrix;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Behandle UnauthorizedException - wirft 401 Unauthorized
     */
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorizedException(UnauthorizedException ex) {
        logger.error("Unauthorized: {}", ex.getMessage());
        ErrorResponse error = new ErrorResponse(
            "Unauthorized",
            ex.getMessage(),
            HttpStatus.UNAUTHORIZED.value()
        );
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    /**
     * ErrorResponse Klasse für JSON Response
     */
    public static class ErrorResponse {
        private String status;
        private String message;
        private int code;

        public ErrorResponse(String status, String message, int code) {
            this.status = status;
            this.message = message;
            this.code = code;
        }

        public String getStatus() {
            return status;
        }

        public String getMessage() {
            return message;
        }

        public int getCode() {
            return code;
        }
    }
}



