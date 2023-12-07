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
public class UserCreateRequestDTO {
    @NotBlank(message = "Email is required")
    private  String email;
    @NotBlank(message = "First name is required")
    private  String firstName;
    @NotBlank(message = "Last name is required")
    private  String lastName;
    @NotBlank(message = "Password is required")
    private  String password;
    @NotBlank(message = "Address is required.")
    private  String address;
    private  String phone;
    @NotBlank(message = "Role is required.")
    private  Long roleId;
}
