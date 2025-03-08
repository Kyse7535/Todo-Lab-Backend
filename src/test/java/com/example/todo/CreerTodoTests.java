package com.example.todo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.example.todo.application.DateGenerator;
import com.example.todo.domain.Todo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CreerTodoTests {

    @Autowired
    private TestRestTemplate testRestTemplate;
    private Todo todo;

    @MockitoBean
    private DateGenerator dateGenerator;
    @LocalServerPort
    private int port;

    @Test
    public void creation_reussie() {
        when(dateGenerator.now()).thenReturn("2025-07-14");
        todo = new Todo(UUID.randomUUID().toString(), "Dormir", "2025-07-20");
        ResponseEntity<Void> response = testRestTemplate.postForEntity("/todos", todo, Void.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    public void format_texte_invalide() {
        when(dateGenerator.now()).thenReturn("2025-07-14");
        todo = new Todo(UUID.randomUUID().toString(), "Dormir8", "2025-07-20");
        ResponseEntity<String> response = testRestTemplate.postForEntity("/todos", todo, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Le texte ne peut contenir de chiffres, ni de carectères spéciaux", response.getBody());
    }

    @Test
    public void date_invalide() {
        when(dateGenerator.now()).thenReturn("2025-07-14");
        todo = new Todo(UUID.randomUUID().toString(), "Dormir", "2025-01-01");
        ResponseEntity<String> response = testRestTemplate.postForEntity("/todos", todo, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("La date doit être supérieure à la date du jour", response.getBody());
    }

}
