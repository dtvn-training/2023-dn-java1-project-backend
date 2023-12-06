package com.example.project.dto.request;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ConfigurationProperties
public class UserCreateRequestDTO {
    @NotBlank(message = "Email is required")
    @Pattern(regexp = "${EMAIL_REGEX}", message = "Invalid email format")
    private  String email;
    @NotBlank(message = "First name is required")
    @Pattern(regexp = "${NAME_REGEX}", message = "Invalid first name")

    private  String firstName;
    @NotBlank(message = "Last name is required")
    @Pattern(regexp = "${NAME_REGEX}", message = "Invalid last name")
    private  String lastName;
    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password should be at least 8 characters long")
    private  String password;
    @NotBlank(message = "Address is required.")
    private  String address;
    @NotBlank(message = "Phone number is required.")
    @Pattern(regexp ="${PHONE_NUMBER_REGEX}", message = "Invalid Phone number.")
    private  String phone;
    @NotBlank(message = "Role is required.")
    private  Long roleId;
}
