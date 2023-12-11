package com.example.project.dto.response;//package com.dtvn.springbootproject.dto.responseDtos.Creative;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreativeDTO {
    private String name;
    private String description;
    private String finalUrl;
}
