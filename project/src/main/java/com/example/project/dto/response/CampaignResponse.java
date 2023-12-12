package com.example.project.dto.response;

import com.example.project.model.Campaign;
import com.example.project.model.User;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CampaignResponse {
    private Long id;
    private String name;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Double budget;
    private Double usedAmount;
    private Double usageRate;
    private Boolean status;
    private Double bidAmount;

    public static CampaignResponse mapCampaign(Campaign campaign) {
        CampaignResponse campaignResponse = CampaignResponse.builder()
                .id(campaign.getId())
                .name(campaign.getName())
                .startDate(campaign.getStartDate())
                .endDate(campaign.getEndDate())
                .budget(campaign.getBudget())
                .usedAmount(campaign.getUsedAmount())
                .usageRate(campaign.getUsageRate())
                .status(campaign.getStatus())
                .bidAmount(campaign.getBidAmount())
                .build();
        return campaignResponse;
    }
}
