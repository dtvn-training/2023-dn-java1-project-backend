package com.example.project.service;

import com.example.project.dto.response.BannerDTO;
import com.example.project.dto.response.CampaignAndImgDTO;
import com.example.project.dto.response.CampaignAndCreativesDTO;
import com.example.project.model.Campaign;
import com.example.project.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.multipart.MultipartFile;

public interface ICampaignService {
    Page<CampaignAndImgDTO> getCampaign(String name, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
    void deleteCampaign(Long campaignId);
    CampaignAndCreativesDTO updateCampaign(Long campaignId, CampaignAndCreativesDTO campaignAndCreativesDTO, MultipartFile file);
    CampaignAndCreativesDTO createCampaign(CampaignAndCreativesDTO campaignAndCreativesDTO, User user);
    CampaignAndCreativesDTO getCampaignAndCreativesDTOById(Long id);

    List<BannerDTO> listBannerUrl();
    void impression(Long campaignId);


    Page<Campaign> getCampaigns(
            @Param("name") String name,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            Pageable pageable);

    Optional<Campaign> findByIdAndDeleteFlagIsFalse(Long id);
    boolean existsByNameAndDeleteFlagIsFalse(String name);
    List<Campaign> findTopCampaigns(Pageable pageable);
}
