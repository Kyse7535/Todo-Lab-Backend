package com.example.todo.dto;

import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

public record SignUpReq(
    @NotBlank 
    @Email 
    String email,
    @NotBlank 
    @Pattern(regexp= "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$", 
    message = "Le mot de passe doit contenir au moins 8 caractères, dont une majuscule, une minuscule, un chiffre et un caractère spécial.") 
    String password,
    @NotEmpty
    List<@Valid @Pattern(
            regexp = "^(ROLE_[A-Z]+|[a-zA-Z]+)$",
            message = "Chaque élément doit être soit un rôle (ROLE_XXX), soit une autorité (texte simple sans espace)"
        ) String> authorities,
    @NotBlank
    @Pattern(regexp= "^[A-Za-z0-9-_]+\\.[A-Za-z0-9-_]+\\.[A-Za-z0-9-_]+$", message= "Format captcha non respecte")
     String captcha) {

    public List<SimpleGrantedAuthority> getAuthorities() {
        return this.authorities.stream().map(authority -> new SimpleGrantedAuthority(authority)).toList();
    }
}
