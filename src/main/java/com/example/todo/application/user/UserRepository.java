package com.example.todo.application.user;

import java.util.Optional;

import com.example.todo.domain.User;

public interface  UserRepository {
    public Optional<User> findByEmail(String email);
    public void createUser(User user);
    public void updateUser(User user);
    public void deleteUser(String email);
}
