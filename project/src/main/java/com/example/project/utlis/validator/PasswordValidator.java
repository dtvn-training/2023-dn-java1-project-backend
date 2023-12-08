package com.example.project.utlis.validator;

import com.example.project.service.ErrorService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.example.project.constants.FieldValueLengthConstants.MAX_PASSWORD_LENGTH;
import static com.example.project.constants.FieldValueLengthConstants.MIN_PASSWORD_LENGTH;

@RequiredArgsConstructor
public class PasswordValidator {
    private final ErrorService errorService;

    public static PasswordValidationResult validatePassword(String password) {
        PasswordValidationResult result = new PasswordValidationResult();
        if (password.isEmpty()) {
            result.addError("Password is required");
        }

        if (password.length() < MIN_PASSWORD_LENGTH) {
            result.addError("Password should be at least 8 characters long");
        }

        if (password.length() >= MAX_PASSWORD_LENGTH) {
            result.addError("Password must only be a maximum of 60 characters long");
        }
        return result;
    }

    @Getter
    public static class PasswordValidationResult {
        private final List<String> errors;

        public PasswordValidationResult() {
            this.errors = new ArrayList<>();
        }

        public void addError(String error) {
            errors.add(error);
        }

        public boolean isValid() {
            return errors.isEmpty();
        }
    }
}
