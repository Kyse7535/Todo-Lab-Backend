package com.example.todo.application;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.example.todo.application.todo.ToDoRepository;
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
        registry.remove(todoId);
    }

    @Override
    public List<Todo> getAll(String auteurId) {
        return registry.values().stream().filter(t -> t.getAuteurId().equals(auteurId)).toList();
    }

    @Override
    public void modifierTodo(Todo todo) {
        registry.put(todo.getId(), todo);
    }

    @Override
    public void clear() {
        registry.clear();

    }

}
