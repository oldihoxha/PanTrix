package com.example.pantrix;

public class AuthResponse {
    private boolean success;
    private String message;
    private User user;
    private String token;
    private String access_token; // ✅ WICHTIG - Frontend erwartet access_token!

    public AuthResponse() {}

    public AuthResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
        this.user = null;
        this.token = null;
        this.access_token = null;
    }

    public AuthResponse(boolean success, String message, User user) {
        this.success = success;
        this.message = message;
        this.user = user;
        this.token = null;
        this.access_token = null;
    }

    public AuthResponse(boolean success, String message, User user, String token) {
        this.success = success;
        this.message = message;
        this.user = user;
        this.token = token;
        this.access_token = token; // ✅ Setze access_token gleich wie token
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAccess_token() {
        return token;  // Alias für Token
    }

    public void setAccess_token(String access_token) {
        this.token = access_token;
    }
}

