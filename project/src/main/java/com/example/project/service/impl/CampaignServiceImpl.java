package com.example.project.service.impl;

import static com.example.project.constants.Constants.*;
import static java.net.HttpURLConnection.HTTP_FORBIDDEN;
import static java.net.HttpURLConnection.HTTP_NOT_FOUND;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.project.dto.response.*;
import com.example.project.utlis.validator.CampaignValidator;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.project.exception.ErrorException;
import com.example.project.model.Campaign;
import com.example.project.model.Creatives;
import com.example.project.model.User;
import com.example.project.repository.ICampaignRepository;
import com.example.project.repository.ICreativeRepository;
import com.example.project.service.ICampaignService;
import com.example.project.service.IFirebaseService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CampaignServiceImpl implements ICampaignService {
    private final ICampaignRepository iCampaignRepository;
    private final ICreativeRepository iCreativeRepository;
    private final IFirebaseService iFirebaseService;
    private final ModelMapper mapper = new ModelMapper();
    private final MessageSource messageSource;
    private final CampaignValidator campaignValidator;

    @Override
    public Page<Campaign> getCampaigns(String name, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        return iCampaignRepository.getCampaigns(name, startDate, endDate, pageable);
    }

    @Override
    public Optional<Campaign> findByIdAndDeleteFlagIsFalse(Long id) {
        return iCampaignRepository.findByIdAndDeleteFlagIsFalse(id);
    }

    @Override
    public boolean existsByNameAndDeleteFlagIsFalse(String name) {
        return iCampaignRepository.existsByNameAndDeleteFlagIsFalse(name);
    }

    @Override
    public List<Campaign> findTopCampaigns(Pageable pageable) {
        return iCampaignRepository.findTopCampaigns(pageable);
    }

    @Override
    public Page<CampaignAndImgDTO> getCampaign(String name, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        Page<Campaign> listCampaign = iCampaignRepository.getCampaigns(name, startDate, endDate, pageable);
        List<CampaignAndImgDTO> listCampaignAndCreativesDTO = new ArrayList<>();
        listCampaign.forEach(campaign -> {
            CampaignAndImgDTO campaginAndImgDTO = new CampaignAndImgDTO();
                    Optional<Creatives>  creatives =  iCreativeRepository.findByCampaignIdAndDeleteFlagIsFalse(Optional.ofNullable(campaign));
                    campaginAndImgDTO = mapper.map(campaign, CampaignAndImgDTO.class);
                    campaginAndImgDTO.setImgUrl(creatives.get().getImageUrl());
                    campaginAndImgDTO.setTitle(creatives.get().getTitle());
                    campaginAndImgDTO.setDescription(creatives.get().getDescription());
                    campaginAndImgDTO.setFinalUrl(creatives.get().getFinalUrl());
                    listCampaignAndCreativesDTO.add(campaginAndImgDTO);
                }
        );
        Pageable newPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), listCampaign.getSort());
        Page<CampaignAndImgDTO> page = new PageImpl<>(listCampaignAndCreativesDTO, newPageable, listCampaign.getTotalElements());
        return page;
    }

    @Override
    public CampaignAndCreativesDTO createCampaign(CampaignAndCreativesDTO campaignAndCreativesDTO, User user) {
        campaignValidator.validatorCreate(campaignAndCreativesDTO);
        CampaignDTO campaignDTO = campaignAndCreativesDTO.getCampaignDTO();
        CreativeDTO creativeDTO = campaignAndCreativesDTO.getCreativesDTO();
        Campaign campaignCreated = mapper.map(campaignDTO, Campaign.class);
        campaignCreated.setUserUpdate(user);
        iCampaignRepository.save(campaignCreated);
        Creatives creatives = mapper.map(creativeDTO, Creatives.class);
        creatives.setCampaignId(campaignCreated);
        creatives.setDeleteFlag(false);
        iCreativeRepository.save(creatives);
        return campaignAndCreativesDTO;
    }

    @Override
    public void deleteCampaign(Long campaignId) {
        Optional<Campaign> campaign = iCampaignRepository.findByIdAndDeleteFlagIsFalse(campaignId);
        Optional<Creatives> creatives = iCreativeRepository.findByCampaignIdAndDeleteFlagIsFalse(campaign);
        if (campaign.isPresent()) {
            if (creatives.isPresent())
                creatives.get().setDeleteFlag(true);
            else {
                throw new ErrorException(messageSource.getMessage(CREATIVES_NOT_FOUND, null, LocaleContextHolder.getLocale()), HTTP_FORBIDDEN);
            }
            campaign.get().setDeleteFlag(true);
            iCampaignRepository.save(campaign.get());
        }
    }

    @Override
    public CampaignAndCreativesDTO updateCampaign(Long campaignId, CampaignAndCreativesDTO campaignAndCreativesDTO, MultipartFile file) {
        CampaignDTO campaignDTO = campaignAndCreativesDTO.getCampaignDTO();
        CreativeDTO creativeDTO = campaignAndCreativesDTO.getCreativesDTO();
        try {
            Optional<Campaign> oldCampaign = iCampaignRepository.findByIdAndDeleteFlagIsFalse(campaignId);
            Optional<Creatives> oldCreatives = iCreativeRepository.findByCampaignIdAndDeleteFlagIsFalse(oldCampaign);
            if (oldCampaign.isPresent() && oldCreatives.isPresent()) {
                campaignValidator.validatorCreate(campaignAndCreativesDTO);
                campaignValidator.validatorImage(file);
                //update campaign
                oldCampaign.get().setName(campaignDTO.getName());
                oldCampaign.get().setStatus(campaignDTO.getStatus());
                oldCampaign.get().setBudget(campaignDTO.getBudget());
                oldCampaign.get().setBidAmount(campaignDTO.getBidAmount());
                oldCampaign.get().setStartDate(campaignDTO.getStartDate());
                oldCampaign.get().setEndDate(campaignDTO.getEndDate());
                //update creative
                oldCreatives.get().setTitle(creativeDTO.getTitle());
                oldCreatives.get().setDescription(creativeDTO.getDescription());
                //check if img is change
                if (!file.isEmpty()) {
                    String imgurl = iFirebaseService.uploadFile(file);
                    oldCreatives.get().setImageUrl(imgurl);
                    campaignAndCreativesDTO.getCreativesDTO().setImageUrl(imgurl);
                } else {
                    campaignAndCreativesDTO.getCreativesDTO().setImageUrl(oldCreatives.get().getImageUrl());
                }
                oldCreatives.get().setFinalUrl(creativeDTO.getFinalUrl());
                iCampaignRepository.save(oldCampaign.get());
                iCreativeRepository.save(oldCreatives.get());
                return campaignAndCreativesDTO;
            } else
                throw new ErrorException(messageSource.getMessage(CAMPAIGN_UPDATE_FAILED, null, LocaleContextHolder.getLocale()), HTTP_FORBIDDEN);

        } catch (Exception e) {
            throw new ErrorException(messageSource.getMessage(CAMPAIGN_UPDATE_FAILED, null, LocaleContextHolder.getLocale()), HTTP_FORBIDDEN);
        }
    }

    @Override
    public CampaignAndCreativesDTO getCampaignAndCreativesDTOById(Long campaignId) {
        Campaign campaign = iCampaignRepository.findById(campaignId)
                .orElseThrow(() -> new ErrorException(messageSource.getMessage(CAMPAIGN_NOT_FOUND, null, LocaleContextHolder.getLocale()), HTTP_NOT_FOUND));

        Creatives creatives = iCreativeRepository.findByCampaignId(campaign);
        CampaignDTO campaignDTO = mapToCampaignDTO(campaign);
        CreativeDTO creativeDTO = mapToCreativeDTO(creatives);

        return new CampaignAndCreativesDTO(campaignDTO, creativeDTO);
    }

    private CampaignDTO mapToCampaignDTO(Campaign campaign) {
        return new CampaignDTO(
                campaign.getName(),
                campaign.getStartDate(),
                campaign.getEndDate(),
                campaign.getBudget(),
                campaign.getUsedAmount(),
                campaign.getUsageRate(),
                campaign.getStatus(),
                campaign.getBidAmount());
    }

    private CreativeDTO mapToCreativeDTO(Creatives creatives) {
        return new CreativeDTO(
                creatives.getTitle(),
                creatives.getDescription(),
                creatives.getImageUrl(),
                creatives.getFinalUrl());
    }

    @Override
    public List<BannerDTO> listBannerUrl() {
        List<Campaign> campaigns = null;
        try {
            campaigns = iCampaignRepository.findTopCampaigns(PageRequest.of(0, 5));
        } catch (Exception e) {
            throw new ErrorException(messageSource.getMessage(e.getMessage(), null, LocaleContextHolder.getLocale()), HTTP_FORBIDDEN);
        }
        List<BannerDTO> imgUrl = new ArrayList<>();
        for(int i = 0; i < campaigns.size(); i++){
            Optional<Creatives>  creatives =  iCreativeRepository.findByCampaignIdAndDeleteFlagIsFalse(Optional.ofNullable(campaigns.get(i)));
            if(creatives.isPresent()){
                BannerDTO bannerDTO = new BannerDTO(creatives.get().getCreativeId(),creatives.get().getImageUrl());
                imgUrl.add(bannerDTO);
                iCampaignRepository.save(campaigns.get(i));
            }
        }
        return imgUrl;
    }

    @Override
    public void impression(Long id) {
        Optional<Campaign> campaign = iCampaignRepository.findById(id);
        if (campaign.isEmpty()) {
            throw new ErrorException(messageSource.getMessage(CAMPAIGN_NOT_FOUND, null, LocaleContextHolder.getLocale()), HTTP_FORBIDDEN);
        }
        // Check end conditions
        if (campaign.get().getEndDate().isBefore(LocalDateTime.now())) {
            campaign.get().setStatus(false);
            iCampaignRepository.save(campaign.get());
            throw new ErrorException(messageSource.getMessage(ERROR_DATE, null, LocaleContextHolder.getLocale()), HTTP_FORBIDDEN);
        }
        if(campaign.isPresent()){
            campaign.get().setUsedAmount((campaign.get().getUsedAmount() + campaign.get().getBidAmount()));
            campaign.get().setUsageRate(((campaign.get().getUsedAmount() /  campaign.get().getBudget())) * 100);
            iCampaignRepository.save(campaign.get());
        }
    }


}
