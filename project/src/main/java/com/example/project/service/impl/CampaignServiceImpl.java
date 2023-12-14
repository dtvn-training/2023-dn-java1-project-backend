package com.example.project.service.impl;

import static com.example.project.constants.Constants.CAMPAIGN_ID_INVALID;
import static com.example.project.constants.Constants.CAMPAIGN_UPDATE_FAILED;
import static com.example.project.constants.Constants.CREATIVES_NOT_FOUND;
import static java.net.HttpURLConnection.HTTP_FORBIDDEN;
import static java.net.HttpURLConnection.HTTP_NOT_FOUND;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.project.dto.response.CampaignAndImgDTO;
import com.example.project.dto.response.CampaignAndCreativesDTO;
import com.example.project.dto.response.CampaignDTO;
import com.example.project.dto.response.CreativeDTO;
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

    @Override
    public Page<CampaignAndImgDTO> getCampaign(String name, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        Page<Campaign> listCampaign = iCampaignRepository.getCampaign(name,startDate,endDate, pageable);
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
        // Tạo một đối tượng Pageable mới với tổng số phần tử tính toán
        Pageable newPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), listCampaign.getSort());

        // Tạo đối tượng PageImpl với danh sách và Pageable mới
        Page<CampaignAndImgDTO> page = new PageImpl<>(listCampaignAndCreativesDTO, newPageable, listCampaign.getTotalElements());
        return page;
    }
    @Override
    public CampaignAndCreativesDTO createCampaign(CampaignAndCreativesDTO campaignAndCreativesDTO, User user) {
        CampaignDTO campaignDTO = campaignAndCreativesDTO.getCampaignDTO();
        CreativeDTO creativeDTO = campaignAndCreativesDTO.getCreativesDTO();
        Campaign campaignCreated = mapper.map(campaignDTO, Campaign.class);
        campaignCreated.setUsageRate(0.0);
        campaignCreated.setUsedAmount(0.0);
        campaignCreated.setUserID(user);
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
        if(creatives.isPresent())
            creatives.get().setDeleteFlag(true);
        else {
            throw new RuntimeException(CREATIVES_NOT_FOUND);
        }
        campaign.get().setDeleteFlag(true);
        iCampaignRepository.save(campaign.get());
    }
    @Override
    public CampaignAndCreativesDTO updateCampaign(Long campaignId, CampaignAndCreativesDTO campaignAndCreativesDTO, MultipartFile file ) {
        CampaignDTO campaignDTO = campaignAndCreativesDTO.getCampaignDTO();
        CreativeDTO creativeDTO = campaignAndCreativesDTO.getCreativesDTO();
        try{
            Optional<Campaign> oldCampaign = iCampaignRepository.findByIdAndDeleteFlagIsFalse(campaignId);
            Optional<Creatives> oldCreate = iCreativeRepository.findByCampaignIdAndDeleteFlagIsFalse(oldCampaign)  ;
            if(oldCampaign.isPresent()){
                //update campaign
            oldCampaign.get().setStatus(campaignDTO.getStatus());
            oldCampaign.get().setBudget(campaignDTO.getBudget());
            oldCampaign.get().setBidAmount(campaignDTO.getBidAmount());
            oldCampaign.get().setStartDate(campaignDTO.getStartDate());
            oldCampaign.get().setEndDate(campaignDTO.getEndDate());
            //update creative
            oldCreate.get().setTitle(creativeDTO.getTitle());
            oldCreate.get().setDescription(creativeDTO.getDescription());
            //check if img is change
            if(!file.isEmpty()){
                String imgurl = iFirebaseService.uploadFile(file);
                oldCreate.get().setImageUrl(imgurl);
                campaignAndCreativesDTO.getCreativesDTO().setImageUrl(imgurl);
            } else {
                campaignAndCreativesDTO.getCreativesDTO().setImageUrl(oldCreate.get().getImageUrl());
            }
            oldCreate.get().setFinalUrl(creativeDTO.getFinalUrl());
            iCampaignRepository.save(oldCampaign.get());
            iCreativeRepository.save(oldCreate.get());
            return campaignAndCreativesDTO;
            } else throw new ErrorException(messageSource.getMessage(CAMPAIGN_UPDATE_FAILED, null, LocaleContextHolder.getLocale()), HTTP_FORBIDDEN);

        } catch (Exception e){
            throw new ErrorException(messageSource.getMessage(CAMPAIGN_UPDATE_FAILED, null, LocaleContextHolder.getLocale()), HTTP_FORBIDDEN);
        }
    }
}
