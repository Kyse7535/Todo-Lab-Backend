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
        Boolean isTexteNotContainSpecialCharacter = Pattern.matches("^[a-zA-Z ]+$", todo.getTexte());
        Boolean isDateLimiteBeforeNow = LocalDate.parse(todo.getDate()).isBefore(LocalDate.now());
        if (!isTexteNotContainSpecialCharacter) {
            throw new RuntimeException("Le texte ne peut contenir de chiffres, ni de carectères spéciaux");
        }
        if (isDateLimiteBeforeNow) {
            throw new RuntimeException("La date doit être supérieure à la date du jour");
        }
        repository.save(todo);
    }

    @Override
    public Todo modifierTodo(Todo todo) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'modifierTodo'");
    }

    @Override
    public void supprimerTodo(String todoId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'supprimerTodo'");
    }

    @Override
    public List<Todo> getTodos() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getTodos'");
    }

}
