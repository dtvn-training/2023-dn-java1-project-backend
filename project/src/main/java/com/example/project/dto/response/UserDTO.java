package com.example.project.dto.response;


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
public class UserDTO extends BaseResponse{
    private Long id;
    private  String email;
    private  String firstName;
    private  String lastName;
    private  String address;
    private  String phone;
    private  Long roleId;
}
