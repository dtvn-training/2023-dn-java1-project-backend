package com.example.project.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class CampaignAndImgDTO {
    private Long id;
    private String name;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Double budget;
    private Double usedAmount;
    private Float usageRate;
    private Boolean status;
    private Double bidAmount;
    private String imgUrl;
    private String title;
    private String description;
    private String finalUrl;
}
