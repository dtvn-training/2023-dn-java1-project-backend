package com.example.project.utlis.validator;
;
import com.example.project.constants.Constants;
import com.example.project.dto.request.UserCreateRequestDTO;
import com.example.project.exception.ErrorException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import java.util.List;

import static com.example.project.constants.Constants.*;
import static com.example.project.constants.FieldValueLengthConstants.MAX_EMAIL_LENGTH;
import static com.example.project.constants.FieldValueLengthConstants.MAX_FIRSTNAME_LENGTH;
import static com.example.project.constants.FieldValueLengthConstants.MAX_LASTNAME_LENGTH;
import static com.example.project.constants.RegularConstants.*;
import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;

@Component
@RequiredArgsConstructor
public class UserValidator {
    private final MessageSource messageSource;
    public void validateCreateRequest(UserCreateRequestDTO request) {
        validateEmail(request.getEmail());
        validatePassword(request.getPassword());
        validateName(request.getFirstName(), "firstName");
        validateName(request.getLastName(), "lastName");
        validatePhoneNumber(request.getPhone());
        validateAddress(request.getAddress());
    }

    private void validateEmail(String email) {
        if (email == null){
            throw new ErrorException(messageSource.getMessage(ERROR_EMAIL_REQUIRED, null, LocaleContextHolder.getLocale()), HTTP_BAD_REQUEST);
        }
        if (email.length() >= MAX_EMAIL_LENGTH){
            throw new ErrorException(messageSource.getMessage(ERROR_EMAIL_MAX_LENGTH, null, LocaleContextHolder.getLocale()), HTTP_BAD_REQUEST);
        }
        if (!email.matches(EMAIL_REGEX)) {
            throw new ErrorException(messageSource.getMessage(ERROR_EMAIL_INVALID, null, LocaleContextHolder.getLocale()), HTTP_BAD_REQUEST);
        }
    }

        private void validatePassword(String password) {
            if (password == null){
                throw new ErrorException(messageSource.getMessage(ERROR_PASSWORD_REQUIRED, null, LocaleContextHolder.getLocale()), HTTP_BAD_REQUEST);
            }
            PasswordValidator.PasswordValidationResult validationResult = PasswordValidator.validatePassword(password);
            if (!validationResult.isValid()) {
                List<String> errors = validationResult.getErrors();
                String formattedErrors = String.join(", ", errors);
                throw new ErrorException(messageSource.getMessage(ERROR_PASSWORD_INVALID, null, LocaleContextHolder.getLocale()), HTTP_BAD_REQUEST,formattedErrors);
            }
        }

    private void validateName(String name, String fieldName) {
        if (name == null && fieldName.contains("firstName")){
            throw new ErrorException(messageSource.getMessage(ERROR_FIRSTNAME_REQUIRED, null, LocaleContextHolder.getLocale()), HTTP_BAD_REQUEST);
        }
        if (name == null && fieldName.contains("lastName")){
            throw new ErrorException(messageSource.getMessage(ERROR_LASTNAME_REQUIRED, null, LocaleContextHolder.getLocale()), HTTP_BAD_REQUEST);
        }
        assert name != null;
        if (name.length() > MAX_FIRSTNAME_LENGTH && fieldName.contains("firstName")){
            throw new ErrorException(messageSource.getMessage(ERROR_FIRSTNAME_MAX_LENGTH, null, LocaleContextHolder.getLocale()), HTTP_BAD_REQUEST);
        }
        if (name.length() > MAX_LASTNAME_LENGTH && fieldName.contains("lastName")){
            throw new ErrorException(messageSource.getMessage(ERROR_LASTNAME_MAX_LENGTH, null, LocaleContextHolder.getLocale()), HTTP_BAD_REQUEST);
        }

        if (fieldName.contains("firstName") && !name.matches(NAME_REGEX)) {
            throw new ErrorException(messageSource.getMessage(ERROR_FIRSTNAME_INVALID, null, LocaleContextHolder.getLocale()), HTTP_BAD_REQUEST);
        }
        if (fieldName.contains("lastName") && !name.matches(NAME_REGEX)) {
            throw new ErrorException(messageSource.getMessage(ERROR_LASTNAME_INVALID, null, LocaleContextHolder.getLocale()), HTTP_BAD_REQUEST);
        }
    }

    private void validatePhoneNumber(String phoneNumber) {
        if (phoneNumber == null){
            throw new ErrorException(messageSource.getMessage(ERROR_PHONE_REQUIRED, null, LocaleContextHolder.getLocale()), HTTP_BAD_REQUEST);
        }
        if (!phoneNumber.matches(PHONE_NUMBER_REGEX)) {
            throw new ErrorException(messageSource.getMessage(ERROR_PHONE_INVALID, null, LocaleContextHolder.getLocale()), HTTP_BAD_REQUEST);
        }
    }

    private void validateAddress(String address) {
        if (address == null) {
            throw new ErrorException(messageSource.getMessage(ERROR_ADDRESS_REQUIRED, null, LocaleContextHolder.getLocale()) , HTTP_BAD_REQUEST);
        }
        if (!address.matches(ADDRESS_REGEX)) {
            throw new ErrorException(messageSource.getMessage(ERROR_ADDRESS_INVALID, null, LocaleContextHolder.getLocale()), HTTP_BAD_REQUEST);
        }
    }
}
