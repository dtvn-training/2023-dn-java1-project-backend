package com.example.project.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CampaignAndCreativesDTO {
    private CampaignDTO campaignDTO;
    private CreativeDTO creativesDTO;
}
