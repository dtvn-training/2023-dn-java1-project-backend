package com.example.project.payload.response;


import com.example.project.model.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ListUserResponse extends BaseResponse {
    private  String email;
    private  String firstName;
    private  String lastName;
    private  String address;
    private  String phone;
    @JsonProperty("role_id")
    private  Long roleId;

    public static ListUserResponse mapUser(User user){
        ListUserResponse userResponse = ListUserResponse.builder()
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .address(user.getAddress())
                .phone(user.getPhone())
                .roleId(user.getRole().getId())
                .build();
        userResponse.setCreatedAt(user.getCreatedAt());
        userResponse.setUpdatedAt(user.getUpdatedAt());
        return userResponse;
    }

    private List<ListUserResponse> userResponses;
    private int totalPage;
}

