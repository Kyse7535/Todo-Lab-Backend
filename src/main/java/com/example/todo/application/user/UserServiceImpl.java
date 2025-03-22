package com.example.todo.application.user;

import java.util.List;
import java.util.Map;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

import com.example.todo.application.SecurityConfig;
import com.example.todo.domain.User;
import com.example.todo.dto.SignInReq;
import com.example.todo.dto.SignedUser;

@Service
public class UserServiceImpl implements UserDetailsManager, UserService{
    
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repository.findByEmail(username).orElse(null);
        if (user == null) {
            throw new RuntimeException("Utilisateur inexistant");
        }
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), user.getAuthorities());
    }

    @Override
    public void createUser(UserDetails user) {
        List<String> authorities = user.getAuthorities().stream().map(a -> a.getAuthority()).toList();
        User userToSave = new User(user.getUsername(), passwordEncoder.encode(user.getPassword()), authorities);
        repository.createUser(userToSave);
    }

    @Override
    public void updateUser(UserDetails userDetails) {
        User userExisting = repository.findByEmail(userDetails.getUsername()).orElse(null);
        if (userExisting == null) {
            throw new RuntimeException("Utilisateur inexistant");
        }
        List<String> authorities = userDetails.getAuthorities().stream().map(a -> a.getAuthority()).toList();
        userExisting.setEmail(userDetails.getUsername());
        userExisting.setPassword(passwordEncoder.encode(userDetails.getPassword()));
        userExisting.setAuthorities(authorities);
        repository.updateUser(userExisting);
    }

    

    @Override
    public void deleteUser(String email) {
       repository.deleteUser(email);
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'changePassword'");
    }

    @Override
    public boolean userExists(String username) {
        User user = repository.findByEmail(username).orElse(null);
        return user != null;
    }

    @Override
    public SignedUser login(SignInReq user) {
       UserDetails userDetails = this.loadUserByUsername(user.email());
       if (!passwordEncoder.matches(user.password(), userDetails.getPassword())) {
        throw new RuntimeException("Email et/ou password incorrect");
       }
       List<String> authorities = userDetails.getAuthorities().stream().map(a -> a.getAuthority()).toList();
       Map<String, String> tokens;
    try {
        tokens = SecurityConfig.generateTokens(user.email(), authorities);
    } catch (Exception e) {
        throw new RuntimeException("Erreur de login");
    }
       SignedUser signedUser = new SignedUser(user.email(), tokens.get("accessToken"), tokens.get("refreshToken"), false);
       return signedUser;
    }

    @Override
    public SignedUser  createUserFromGoogleAccount(String email) {
        if (this.userExists(email)) {
            return new SignedUser(email, "", "", false);
        }
        User user = new User(email, "", List.of("ROLE_USER"));
        repository.createUser(user);
        return new SignedUser(email, "", "", true);
    }
}
