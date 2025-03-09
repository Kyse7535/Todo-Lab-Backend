package com.example.todo.application;

import java.time.LocalDate;
import java.util.List;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

import com.example.todo.domain.Todo;

@Service
public class ToDoServiceImpl implements ToDoService {
    private final ToDoRepository repository;

    public ToDoServiceImpl(ToDoRepository repository) {
        this.repository = repository;
    }

    @Override
    public void creerTodo(Todo todo) {
        isGoodFormat(todo);
        repository.save(todo);
    }

    @Override
    public void modifierTodo(Todo todo) {
        isGoodFormat(todo);
        repository.modifierTodo(todo);
    }

    @Override
    public void supprimerTodo(String todoId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'supprimerTodo'");
    }

    @Override
    public List<Todo> getTodos(String auteurId) {
        return repository.getAll(auteurId);
    }

    private void isGoodFormat(Todo todo) {
        Boolean isTexteNotContainSpecialCharacter = Pattern.matches("^[a-zA-Z ]+$", todo.getTexte());
        Boolean isDateLimiteBeforeNow = LocalDate.parse(todo.getDateLimite()).isBefore(LocalDate.now());
        if (!isTexteNotContainSpecialCharacter) {
            throw new RuntimeException("Le texte ne peut contenir de chiffres, ni de carectères spéciaux");
        }
        if (isDateLimiteBeforeNow) {
            throw new RuntimeException("La date doit être supérieure à la date du jour");
        }
    }

}
