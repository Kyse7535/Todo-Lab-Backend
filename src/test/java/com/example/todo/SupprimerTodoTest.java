package com.example.todo;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
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
public class SupprimerTodoTest {

    @Autowired
    private ToDoRepository repository;
    private Todo todo;
    @MockitoBean
    private DateGenerator dateGenerator;
    @Autowired
    private TestRestTemplate template;
    private String auteurId = UUID.randomUUID().toString();
    private String todoId = UUID.randomUUID().toString();

    @Test
    public void suppression_reussie() {
        when(dateGenerator.now()).thenReturn("2025-07-14");
        todo = Todo.creerTodo(todoId, "Dormir", "2025-07-20", auteurId);
        HttpEntity<Todo> requestEntity = Utils.createRequest(todo);
        template.exchange("/todos", HttpMethod.POST, requestEntity, Void.class);

        ResponseEntity<Void> response = template.exchange("/todos/" + todoId, HttpMethod.DELETE, requestEntity, Void.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
