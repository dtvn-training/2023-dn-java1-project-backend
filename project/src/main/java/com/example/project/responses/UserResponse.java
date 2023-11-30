package com.example.project.responses;

import com.example.project.model.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse extends BaseResponse {
    private  String email;
    private  String firstName;
    private  String lastName;
    private  String address;
    private  String phone;
    private  String password;
    @JsonProperty("role_id")
    private  Long roleId;

    public static UserResponse mapUser(User user){
        UserResponse userResponse = UserResponse.builder()
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .address(user.getAddress())
                .phone(user.getPhone())
                .password(user.getPassword())
                .roleId(user.getRole().getId())
                .build();
        userResponse.setCreatedAt(user.getCreatedAt());
        userResponse.setUpdatedAt(user.getUpdatedAt());
        return userResponse;
    }

    private List<UserResponse> userResponses;
    private int totalPage;
}
