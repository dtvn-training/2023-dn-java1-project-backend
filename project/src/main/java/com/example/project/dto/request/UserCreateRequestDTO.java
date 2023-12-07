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
    private  String email;
    private  String firstName;
    private  String lastName;
    private  String password;
    private  String address;
    private  String phone;
    private  Long roleId;
}
