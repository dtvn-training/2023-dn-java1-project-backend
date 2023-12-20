package com.example.project.dto.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreativeDTO {
    private String title;
    private String description;
    private String imageUrl;
    private String finalUrl;
}
