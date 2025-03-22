package com.example.todo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record SignInReq(
    @NotBlank 
    @Email 
    String email,
    @NotBlank 
    @Pattern(regexp= "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$", 
    message = "Le mot de passe doit contenir au moins 8 caractères, dont une majuscule, une minuscule, un chiffre et un caractère spécial.") 
    String password,
    @NotBlank
    @Pattern(regexp= "^[A-Za-z0-9-_]+\\.[A-Za-z0-9-_]+\\.[A-Za-z0-9-_]+$", message= "Format captcha non respecte")
     String captcha) {

}
