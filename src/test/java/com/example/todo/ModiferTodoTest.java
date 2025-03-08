package com.example.todo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.example.todo.application.DateGenerator;
import com.example.todo.domain.Todo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ModiferTodoTest {

    @LocalServerPort
    private int port;
    @MockitoBean
    private DateGenerator dateGenerator;
    private Todo todo;
    private String todoId = UUID.randomUUID().toString();
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void modification_reussie() {
        // Given
        when(dateGenerator.now()).thenReturn("2025-10-10");
        todo = new Todo(todoId, "Dormir", "2025-10-10");
        restTemplate.postForEntity("/todos", todo, Void.class);
        todo.setTexte("Courir");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Todo> requEntity = new HttpEntity<>(todo, headers);

        // when
        ResponseEntity<Todo> response = restTemplate.exchange("/todos", HttpMethod.PUT, requEntity, Todo.class);

        // assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
