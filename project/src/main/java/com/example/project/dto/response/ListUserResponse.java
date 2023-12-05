package com.example.project.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ListUserResponse {
    private List<UserResponse> listUser;
    private int totalPage;
}
