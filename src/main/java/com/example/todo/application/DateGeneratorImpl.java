package com.example.todo.application;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

@Component
public class DateGeneratorImpl implements DateGenerator {

    @Override
    public String now() {
        return LocalDate.now().toString();
    }

}
