package com.example.project.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    @NotBlank(message = "Email is required")
    @JsonProperty("email")
    private  String email;
    @NotBlank(message = "First name is required")
    @JsonProperty("firstName")
    private  String firstName;
    @NotBlank(message = "Last name is required")
    @JsonProperty("lastName")
    private  String lastName;
    @NotBlank(message = "Address is required")
    @JsonProperty("address")
    private  String address;
    @NotBlank(message = "Phone is required")
    @JsonProperty("phone")
    private  String phone;
    @JsonProperty("password")
    private  String password;
    @NotBlank(message = "Role is required")
    @JsonProperty("role_id")
    private  Long roleId;
}
