package com.example.project.service;

import com.example.project.dto.response.CampaignAndImgDTO;
import com.example.project.dto.response.CampaignAndCreativesDTO;
import com.example.project.model.Campaign;
import com.example.project.model.User;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface ICampaignService {
    public Page<CampaignAndImgDTO> getCampaign(String name, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
    public void deleteCampaign(Long campaignId);
    public CampaignAndCreativesDTO updateCampaign(Long campaignId, CampaignAndCreativesDTO campaignAndCreativesDTO, MultipartFile file);
    public CampaignAndCreativesDTO createCampaign(CampaignAndCreativesDTO campaignAndCreativesDTO, User user);
    Campaign getCampaignByID(Long id) throws Exception;
}
