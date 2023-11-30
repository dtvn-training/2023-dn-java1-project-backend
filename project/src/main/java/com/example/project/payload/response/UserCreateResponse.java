package com.example.project.payload.response;

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
    @JsonProperty("role_id")
    private  Long roleId;
//
//    public static UserResponse mapUser(User user){
//        UserResponse userResponse = UserResponse.builder()
//                .email(user.getEmail())
//                .firstName(user.getFirstName())
//                .lastName(user.getLastName())
//                .address(user.getAddress())
//                .phone(user.getPhone())
//                .password(user.getPassword())
//                .roleId(user.getRole().getId())
//                .build();
//        userResponse.setCreatedAt(user.getCreatedAt());
//        userResponse.setUpdatedAt(user.getUpdatedAt());
//        return userResponse;
//    }
//
//    private List<UserResponse> userResponses;
//    private int totalPage;
}
