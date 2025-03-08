package com.example.todo.application;

import java.util.List;

import com.example.todo.domain.Todo;

public interface ToDoRepository {
    public void save(Todo todo);

    public void modifierTodo(Todo todo);

    public void delete(String todoId);

    public List<Todo> getAll();
}
