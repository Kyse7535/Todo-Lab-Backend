package com.example.todo.application.user;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.example.todo.domain.User;

@Repository
public class StubMemoryUserRepository implements UserRepository{
    private Map<String, User> registry = new HashMap<>();

    @Override
    public Optional<User> findByEmail(String email) {
        return registry.values().stream().filter(u -> u.getEmail().equals(email)).findFirst();
    }

    @Override
    public void createUser(User user) {
        registry.putIfAbsent(user.getId(), user);
    }

    @Override
    public void updateUser(User user) {
        registry.put(user.getId(), user);
    }

    @Override
    public void deleteUser(String email) {
        registry.values().removeIf(u -> u.getEmail().equals(email));
    }
    
}
