package com.example.project.payload.response;

import com.example.project.model.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse extends BaseResponse {
    private  Long id;
    private  String email;
    private  String firstName;
    private  String lastName;
    private  String address;
    private  String phone;
    private  Long roleId;

    public static UserResponse mapUser(User user) {
        UserResponse userResponse = UserResponse.builder()
                .id(user.getId())
                        .email(user.getEmail())
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .roleId(user.getRole().getId())
                        .address(user.getAddress())
                        .phone(user.getPhone())
                        .build();
        userResponse.setCreatedAt(user.getCreatedAt());
        userResponse.setUpdatedAt(user.getUpdatedAt());
        return userResponse;
    }
}
