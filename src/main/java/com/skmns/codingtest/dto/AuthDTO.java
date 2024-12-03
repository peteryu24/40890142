package com.skmns.codingtest.dto;

public class AuthDTO {
    private Long userId;
    private String username;
    private String password;

    public AuthDTO() {
    }

    public AuthDTO(Long userId, String username, String password) {
        this.userId = userId;
        this.username = username;
        this.password = password;
    }

    public Long getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
