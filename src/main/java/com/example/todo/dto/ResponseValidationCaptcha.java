package com.example.todo.dto;

public class ResponseValidationCaptcha implements ValidationCaptcha{
    private boolean success;
    @Override
    public boolean isSuccess() {
        return success;
    }
    
}

interface ValidationCaptcha {
    public boolean isSuccess();
}
