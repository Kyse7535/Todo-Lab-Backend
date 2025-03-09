package com.example.todo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.example.todo.application.DateGenerator;
import com.example.todo.domain.Todo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class getTodosTest {

    @Autowired
    private TestRestTemplate template;
    private Todo todo;
    private String auteurId = UUID.randomUUID().toString();
    @MockitoBean
    private DateGenerator dateGenerator;

    @Test
    public void consultation_reussie() {

        when(dateGenerator.now()).thenReturn("2025-07-14");
        todo = Todo.creerTodo(UUID.randomUUID().toString(), "Dormir", "2025-07-20", auteurId);
        template.postForEntity("/todos", todo, Void.class);

        ResponseEntity<List<Todo>> response = template.exchange("/todos/" + auteurId, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Todo>>() {
                });
        Todo result = (Todo) response.getBody().get(0);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("Dormir", result.getTexte());
    }
}
