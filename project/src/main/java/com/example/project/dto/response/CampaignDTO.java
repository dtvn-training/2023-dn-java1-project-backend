package com.example.project.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CampaignDTO{
    private String name;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Double budget;
    private Double usedAmount;
    private Double usageRate;
    private Boolean status;
    private Double bidAmount;
}
