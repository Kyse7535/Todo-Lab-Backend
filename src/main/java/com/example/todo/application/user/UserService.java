package com.example.todo.application.user;

import com.example.todo.domain.SignedUser;
import com.example.todo.domain.User;

public interface UserService {
    public SignedUser login(User user);
}
