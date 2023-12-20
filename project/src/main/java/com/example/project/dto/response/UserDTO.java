package com.example.project.dto.response;

import lombok.*;
import java.time.LocalDateTime;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO{
    private Long id;
    private  String email;
    private  String firstName;
    private  String lastName;
    private  String address;
    private  String phone;
    private  Long roleId;
    private LocalDateTime updatedAt;
}
