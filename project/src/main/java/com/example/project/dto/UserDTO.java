package com.example.project.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    @JsonProperty("email")
    @NotBlank(message = "Email is required")
    private  String email;
    @NotBlank(message = "First name is required")
    private  String firstName;
    @NotBlank(message = "Last name is required")
    private  String lastName;
    @JsonProperty("address")
    private  String address;
    private String password;
    @JsonProperty("phone")
    private  String phone;
    @NotNull(message = "Role id is required")
    @JsonProperty("role_id")
    private  Long roleId;
}
