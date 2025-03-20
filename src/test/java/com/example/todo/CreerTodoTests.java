package com.example.todo;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeAll;
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
import com.example.todo.application.SecurityConfig;
import com.example.todo.application.todo.ToDoRepository;
import com.example.todo.application.user.UserRepository;
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
    static String accessToken = "";
    private HttpEntity<Todo> requestEntity;

    @AfterEach
    void setup(@Autowired ToDoRepository repository) {
        repository.clear();
    }
    @BeforeAll
    static void setupAll(@Autowired UserRepository userRepository) {
        // userRepository.createUser(new User("testUser", , List.of("ROLE_USER")));
        try {
            accessToken = SecurityConfig.generateTokens("testUser", List.of("ROLE_USER")).get("accessToken");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test
    public void creation_reussie() {
        when(dateGenerator.now()).thenReturn("2025-07-14");
        todo = Todo.creerTodo(UUID.randomUUID().toString(), "Dormir", "2025-07-20", UUID.randomUUID().toString());
        requestEntity = Utils.createRequest(todo);
        ResponseEntity<Void> response = testRestTemplate.exchange("/todos", HttpMethod.POST, requestEntity, Void.class);
        // testRestTemplate.postForEntity("/todos", todo, Void.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    public void format_texte_invalide() {
        when(dateGenerator.now()).thenReturn("2025-07-14");
        todo = Todo.creerTodo(UUID.randomUUID().toString(), "Dormir8", "2025-07-20", UUID.randomUUID().toString());
        requestEntity = Utils.createRequest(todo);
        ResponseEntity<String> response = testRestTemplate.exchange("/todos", HttpMethod.POST, requestEntity, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Le texte ne peut contenir de chiffres, ni de carectères spéciaux", response.getBody());
    }

    @Test
    public void date_invalide() {
        when(dateGenerator.now()).thenReturn("2025-07-14");
        todo = Todo.creerTodo(UUID.randomUUID().toString(), "Dormir", "2025-01-01", UUID.randomUUID().toString());
        requestEntity = Utils.createRequest(todo);
        ResponseEntity<String> response = testRestTemplate.exchange("/todos", HttpMethod.POST, requestEntity, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("La date doit être supérieure à la date du jour", response.getBody());
    }
}
