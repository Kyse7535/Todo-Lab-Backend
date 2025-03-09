package com.example.todo.application;

import java.util.List;

import com.example.todo.domain.Todo;

public interface ToDoService {
    public void creerTodo(Todo todo);

    public void modifierTodo(Todo todo);

    public void supprimerTodo(String todoId);

    public List<Todo> getTodos(String auteurId);
}
