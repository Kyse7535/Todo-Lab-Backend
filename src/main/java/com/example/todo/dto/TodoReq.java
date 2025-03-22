package com.example.todo.dto;

import jakarta.validation.constraints.Pattern;

public record TodoReq(
    @Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$",
             message = "L'UUID doit être une chaîne valide de 36 caractères.")
    String uuid,
    @Pattern(regexp = "^[a-zA-Z0-9\\s]+$",
             message = "Le texte ne doit contenir que des lettres, des chiffres et des espaces.")
     String texte,
     @Pattern(regexp = "^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])$",
             message = "La date doit être au format YYYY-MM-DD.")
     String dateLimite,
     @Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$",
             message = "L'UUID doit être une chaîne valide de 36 caractères.")
     String auteurId,
     boolean isCompleted
) {

}
