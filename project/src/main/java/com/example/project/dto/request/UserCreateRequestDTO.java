package com.example.project.dto.request;

import lombok.*;

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
