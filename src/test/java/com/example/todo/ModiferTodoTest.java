package com.example.todo;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.example.Utils;
import com.example.todo.application.DateGenerator;
import com.example.todo.application.todo.ToDoRepository;
import com.example.todo.domain.Todo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ModiferTodoTest {

    @LocalServerPort
    private int port;
    @MockitoBean
    private DateGenerator dateGenerator;
    @Autowired
    private ToDoRepository repository;
    private Todo todo;
    private String todoId = UUID.randomUUID().toString();
    @Autowired
    private TestRestTemplate restTemplate;

    void setup() {
        repository.clear();
    }

    @Test
    public void modification_reussie() {
        // Given
        when(dateGenerator.now()).thenReturn("2025-10-10");
        todo = Todo.creerTodo(UUID.randomUUID().toString(), "Dormir", "2025-10-10", UUID.randomUUID().toString());
        restTemplate.postForEntity("/todos", todo, Void.class);
        todo.setTexte("Courir");
        HttpEntity<Todo> requestEntity = Utils.createRequest(todo);

        // when
        ResponseEntity<Todo> response = restTemplate.exchange("/todos", HttpMethod.PUT, requestEntity, Todo.class);

        // assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
