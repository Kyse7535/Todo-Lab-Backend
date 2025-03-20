package com.example.todo.application.user;

import java.util.List;
import java.util.Map;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

import com.example.todo.application.SecurityConfig;
import com.example.todo.domain.SignedUser;
import com.example.todo.domain.User;

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
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'userExists'");
    }

    @Override
    public SignedUser login(User user) {
       UserDetails userDetails = this.loadUserByUsername(user.getEmail());
       if (!passwordEncoder.matches(user.getPassword(), userDetails.getPassword())) {
        throw new RuntimeException("Email et/ou password incorrect");
       }
       List<String> authorities = userDetails.getAuthorities().stream().map(a -> a.getAuthority()).toList();
       Map<String, String> tokens;
    try {
        tokens = SecurityConfig.generateTokens(user.getEmail(), authorities);
    } catch (Exception e) {
        throw new RuntimeException("Erreur de login");
    }
       SignedUser signedUser = new SignedUser(user.getEmail(), tokens.get("accessToken"), tokens.get("refreshToken"));
       return signedUser;
    }
}
