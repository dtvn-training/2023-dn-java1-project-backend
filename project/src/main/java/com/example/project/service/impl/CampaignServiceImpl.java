package com.example.project.service.impl;

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
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

import static com.example.project.constants.Constants.*;
import static java.net.HttpURLConnection.HTTP_FORBIDDEN;

@Service
@RequiredArgsConstructor
public class CampaignServiceImpl implements ICampaignService {

    private final ICampaignRepository iCampaignRepository;
    private final ICreativeRepository iCreativeRepository;
    private final IFirebaseService iFirebaseService;
    private final ModelMapper mapper = new ModelMapper();
    private final MessageSource messageSource;


    @Override
    public Page<CampaignDTO> getCampaign(String name, Pageable pageable) {
        if(name == null || name.isEmpty()){
            Page<Campaign> listCampaign = iCampaignRepository.getAllCampaign(pageable);
            return listCampaign.map(campaign -> mapper.map(campaign, CampaignDTO.class ));
        } else {
            Page<Campaign> allCampaign = iCampaignRepository.findByName(name,pageable);
            return allCampaign.map(campaign -> mapper.map(campaign, CampaignDTO.class ));
        }
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
    @Override
    public CampaignAndCreativesDTO createCampaign(CampaignAndCreativesDTO campaignAndCreativesDTO, User user) {
        CampaignDTO campaignDTO = campaignAndCreativesDTO.getCampaignDTO();
        CreativeDTO creativeDTO = campaignAndCreativesDTO.getCreativesDTO();
        Campaign campaignCreated =  new Campaign();
        campaignCreated = mapper.map(campaignDTO, Campaign.class);
        campaignCreated.setUsageRate(0.0);
        campaignCreated.setUsedAmount(0.0);
        if(campaignCreated.getBidAmount() == null){
            campaignCreated.setBidAmount(0.0);
        }
        if(campaignCreated.getBudget() == null){
            campaignCreated.setBudget(0.0);
        }
        if(campaignCreated.getStatus() == null){
            campaignCreated.setStatus(true);
        }
        campaignCreated.setUser_update(user);
        iCampaignRepository.save(campaignCreated);
        Creatives creatives = new Creatives();
        creatives = mapper.map(creativeDTO, Creatives.class);
        System.out.println(campaignCreated);
        iCampaignRepository.save(campaignCreated);
        creatives.setCampaignId(campaignCreated);
        creatives.setDeleteFlag(false);
        Creatives creativesCreated = iCreativeRepository.save(creatives);
        return campaignAndCreativesDTO;
    }

    @Override
    public boolean isInteger(String number) {
        try {
            Integer.parseInt(number);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public Campaign maptoEntity(CampaignDTO campaignDTO) {
        return null;
    }

}
