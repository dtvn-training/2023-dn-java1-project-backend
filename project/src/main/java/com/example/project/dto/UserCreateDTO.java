package com.example.project.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserCreateDTO {
    private  String email;
    private  String firstName;
    private  String lastName;
    private  String password;
    private  String address;
    private  String phone;
    private  Long roleId;
}
