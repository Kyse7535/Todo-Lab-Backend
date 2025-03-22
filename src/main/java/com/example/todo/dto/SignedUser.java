package com.example.todo.dto;

public record SignedUser(String username, String accessToken, String refreshToken, boolean accountCreatedFromGoogle) {
}