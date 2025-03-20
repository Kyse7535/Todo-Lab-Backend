package com.example;

import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import com.example.todo.application.SecurityConfig;
import com.example.todo.domain.Todo;

public class Utils {
    public static HttpEntity<Todo> createRequest(Todo todo) {
        String accessToken;
        try {
            accessToken = SecurityConfig.generateTokens("testUser", List.of("ROLE_USER")).get("accessToken");
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de generation du token");
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);
        return new HttpEntity<>(todo, headers);
    }
    
}
