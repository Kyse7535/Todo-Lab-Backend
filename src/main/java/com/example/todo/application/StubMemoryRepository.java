package com.example.todo.application;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.example.todo.domain.Todo;

@Repository
public class StubMemoryRepository implements ToDoRepository {

    private Map<String, Todo> registry = new HashMap<>();

    @Override
    public void save(Todo todo) {
        registry.putIfAbsent(todo.getId(), todo);
    }

    @Override
    public void delete(String todoId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

    @Override
    public List<Todo> getAll() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAll'");
    }

    @Override
    public void modifierTodo(Todo todo) {
        registry.put(todo.getId(), todo);
    }

}
