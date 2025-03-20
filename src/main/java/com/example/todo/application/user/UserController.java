package com.example.todo.application.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.springframework.http.ResponseEntity.status;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.todo.domain.SignedUser;
import com.example.todo.domain.User;


@RestController
@RequestMapping("/users")
public class UserController {
    
    private final UserServiceImpl service;

    public UserController(UserServiceImpl service) {
        this.service = service;
    }

    @PostMapping 
    public ResponseEntity<SignedUser> signin(@RequestBody User user) throws Exception {
        return status(HttpStatus.OK).body(service.login(user));
    }

    @PostMapping("/sign-up")
    public ResponseEntity<Void> signup(@RequestBody User user) {
        UserDetails userDetails = 
        new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), user.getAuthorities());
        
        service.createUser(userDetails);
        return status(HttpStatus.CREATED).build();
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<String> handler(Exception e) {
        return status(HttpStatus.BAD_REQUEST).body(e.getMessage()) ;
    }
    
}
