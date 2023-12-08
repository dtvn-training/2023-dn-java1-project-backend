package com.example.project.utlis.validator;
import com.example.project.dto.request.UserCreateRequestDTO;
import com.example.project.exception.ErrorException;
import com.example.project.repository.IUserRepository;
import com.example.project.service.ErrorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import static com.example.project.constants.FieldValueLengthConstants.MAX_EMAIL_LENGTH;
import static com.example.project.constants.FieldValueLengthConstants.MAX_FIRSTNAME_LENGTH;
import static com.example.project.constants.FieldValueLengthConstants.MAX_LASTNAME_LENGTH;
import static com.example.project.constants.RegularConstants.*;
import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;

@Component
@RequiredArgsConstructor
public class UserValidator {
    private final IUserRepository iUserRepository;

    private final ErrorService errorService;
    public void validateRegisterRequest(UserCreateRequestDTO request) {
        validateEmail(request.getEmail());
        validatePassword(request.getPassword());
        validateName(request.getFirstName(), "firstName");
        validateName(request.getLastName(), "lastName");
        validatePhoneNumber(request.getPhone());
        validateAddress(request.getAddress());
    }

    private void validateEmail(String email) {
        if (email == null){
            throw new ErrorException(errorService.processErrorEmailRequired(), HTTP_BAD_REQUEST);
        }
        if (email.length() >= MAX_EMAIL_LENGTH){
            throw new ErrorException(errorService.processErrorEmailLength(), HTTP_BAD_REQUEST);
        }
        if (!email.matches(EMAIL_REGEX)) {
            throw new ErrorException(errorService.processErrorEmailInvalid(), HTTP_BAD_REQUEST);
        }
    }

        private void validatePassword(String password) {
            if (password == null){
                throw new ErrorException(errorService.processErrorPasswordRequired(), HTTP_BAD_REQUEST);
            }
            PasswordValidator.PasswordValidationResult validationResult = PasswordValidator.validatePassword(password);
            if (!validationResult.isValid()) {
                List<String> errors = validationResult.getErrors();
                String formattedErrors = String.join(", ", errors);
                throw new ErrorException(errorService.processErrorPasswordInvalid(), HTTP_BAD_REQUEST,formattedErrors);
            }
        }

    private void validateName(String name, String fieldName) {
        if (name == null && fieldName.contains("firstName")){
            throw new ErrorException(errorService.processErrorFirstNamedRequired(), HTTP_BAD_REQUEST);
        }
        if (name == null && fieldName.contains("lastName")){
            throw new ErrorException(errorService.processErrorLastNameRequired(), HTTP_BAD_REQUEST);
        }
        assert name != null;
        if (name.length() > MAX_FIRSTNAME_LENGTH && fieldName.contains("firstName")){
            throw new ErrorException(errorService.processErrorFirstNameLength(), HTTP_BAD_REQUEST);
        }
        if (name.length() > MAX_LASTNAME_LENGTH && fieldName.contains("lastName")){
            throw new ErrorException(errorService.processErrorLastNameLength(), HTTP_BAD_REQUEST);
        }

        if (fieldName.contains("firstName") && !name.matches(NAME_REGEX)) {
            throw new ErrorException(errorService.processErrorFirstNameInvalid(), HTTP_BAD_REQUEST);
        }
        if (fieldName.contains("lastName") && !name.matches(NAME_REGEX)) {
            throw new ErrorException(errorService.processErrorLastNameInvalid(), HTTP_BAD_REQUEST);
        }
    }

    private void validatePhoneNumber(String phoneNumber) {
        if (phoneNumber == null){
            throw new ErrorException(errorService.processErrorPhoneRequired(), HTTP_BAD_REQUEST);
        }
        if (!phoneNumber.matches(PHONE_NUMBER_REGEX)) {
            throw new ErrorException(errorService.processErrorPhoneInvalId(), HTTP_BAD_REQUEST);
        }
    }

    private void validateAddress(String address) {
        if (address == null) {
            throw new ErrorException(errorService.processErrorAddressRequired() , HTTP_BAD_REQUEST);
        }
        if (!address.matches(ADDRESS_REGEX)) {
            throw new ErrorException(errorService.processErrorAddressInvalid(), HTTP_BAD_REQUEST);
        }
    }
}
