package com.example.todo.application;

import java.time.LocalDate;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import static org.springframework.http.ResponseEntity.*;

import com.example.todo.domain.Todo;

@RestController
@RequestMapping("/todos")
final public class ToDoController {
    private final ToDoService service;

    public ToDoController(ToDoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Void> creerTodo(@RequestBody Todo todo) {
        service.creerTodo(todo);
        return status(HttpStatus.CREATED)
                .build();
    }

}
