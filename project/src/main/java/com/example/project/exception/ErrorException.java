package com.example.project.exception;

import lombok.Getter;

@Getter
public class ErrorException extends RuntimeException{
    private final String errors;
    public ErrorException(String message) {
        super(message);
        this.errors = message;
    }
    public ErrorException(String message, String errors) {
        super(message);
        this.errors = errors;
    }
}
