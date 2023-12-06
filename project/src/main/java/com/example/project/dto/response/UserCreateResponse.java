package com.example.project.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateResponse {
    private  String email;
    private  String firstName;
    private  String lastName;
    private  String address;
    private  String phone;
    private  Long roleId;
}
