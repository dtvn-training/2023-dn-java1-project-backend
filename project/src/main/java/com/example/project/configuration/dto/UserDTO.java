package com.example.project.configuration.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.sql.Date;
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    @JsonProperty("email")
    private String email;
    @JsonProperty("firstName")
    private String firstName;
    @JsonProperty("lastName")
    private String lastName;
    @NotBlank(message = "phone number is required")
    @JsonProperty("phone")
    private String phone;
    @NotBlank(message = "address is required")
    private String address;
    @NotNull(message = "Role id is required")
    @JsonProperty("role_id")
    private Long roleId;
}

