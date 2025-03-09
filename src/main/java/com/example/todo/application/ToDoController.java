package com.example.todo.application;

import static org.springframework.http.ResponseEntity.status;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @PutMapping
    public ResponseEntity<Void> modifierTodo(@RequestBody Todo todo) {
        service.modifierTodo(todo);
        return status(HttpStatus.OK)
                .build();
    }

    @GetMapping
    public ResponseEntity<List<Todo>> getTodos() {
        return status(HttpStatus.OK).body(service.getTodos());
    }

}
