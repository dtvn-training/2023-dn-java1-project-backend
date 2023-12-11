package com.example.project.service;

import com.example.project.dto.response.CampaignAndCreativesDTO;
import com.example.project.dto.response.CampaignDTO;
import com.example.project.model.Campaign;
import com.example.project.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface ICampaignService {
    public Page<CampaignDTO> getCampaign(String name, Pageable pageable);
    public void deleteCampaign(int campaignId);
    public CampaignAndCreativesDTO updateCampaign(Integer campaignId, CampaignAndCreativesDTO campaignAndCreativesDTO, MultipartFile file);
    public CampaignAndCreativesDTO createCampaign(CampaignAndCreativesDTO campaignAndCreativesDTO, User user);
    public boolean isInteger(String number);
    public Campaign maptoEntity(CampaignDTO campaignDTO);
}
