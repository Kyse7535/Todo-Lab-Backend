package com.example.todo.application.user;

import com.example.todo.dto.SignInReq;
import com.example.todo.dto.SignedUser;

public interface UserService {
    public SignedUser login(SignInReq user);
    public SignedUser createUserFromGoogleAccount(String email);
}
