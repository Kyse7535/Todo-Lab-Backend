package com.example.todo.dto;

public record CaptchaToValidate(
    String secret,
    String remoteip,
    String response
) {
    
}
