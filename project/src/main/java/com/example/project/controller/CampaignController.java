package com.example.project.controller;

import com.example.project.dto.response.CampaignAndCreativesDTO;
import com.example.project.dto.response.CampaignDTO;
import com.example.project.dto.response.CreativeDTO;
import com.example.project.exception.ResponseMessage;
import com.example.project.model.Campaign;
import com.example.project.model.Creatives;
import com.example.project.model.User;
import com.example.project.repository.ICampaignRepository;
import com.example.project.repository.ICreativeRepository;
import com.example.project.repository.IUserRepository;
import com.example.project.service.ICampaignService;
import com.example.project.service.impl.FirebaseServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.project.constants.Constants.CREATIVES_ALREADY_EXISTS;
import static com.example.project.constants.Constants.STARTDATE_IS_AFTER_ENDDATE;
import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;
import static java.net.HttpURLConnection.HTTP_OK;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/campaigns")
public class CampaignController {
    private final ICampaignService iCampaignService;
    private final ICampaignRepository iCampaignRepository;
    private final ICreativeRepository iCreativeRepository;
    private final FirebaseServiceImpl firebaseService;
    private final IUserRepository iUserRepository;
    @GetMapping("/getCampaign")
    public ResponseEntity<ResponseMessage<Page<CampaignDTO>>> getCampaign(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "page") int page,
            @RequestParam(value = "limit") int limit){
        Pageable pageable = PageRequest.of(page, limit);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseMessage<>(CAMPAIGN_GET_SUCCESS, HTTP_OK,iCampaignService.getCampaign(name,pageable)));
    }

    @PatchMapping("/deleteCampagign")
    public ResponseEntity<ResponseMessage<CampaignDTO>> deleteCampagin(
            @RequestParam(value = "id", required = true) String strCampaginId){
        try{

            Integer campaignId = Integer.parseInt(strCampaginId);
            Optional<Campaign> campaign = iCampaignRepository.findByIdAndDeleteFlagIsFalse(campaignId);
            if(campaign.isPresent()){
                if(campaign.get().isDeleteFlag() == true){
                    return ResponseEntity.status(HttpStatus.OK)
                            .body(new ResponseMessage<>(CAMPAGIGN_IS_DELETED, HTTP_BAD_REQUEST));
                } else {
                    iCampaignService.deleteCampaign(campaignId);
                    return ResponseEntity.status(HttpStatus.OK)
                            .body(new ResponseMessage<>(CAMPAIGN_DELETE_SUCCESS, HTTP_OK));
                }
            }else{
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseMessage<>(CAMPAIGN_NOT_FOUND, HTTP_BAD_REQUEST));
            }
        } catch (NumberFormatException e){
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseMessage<>(CAMPAIGN_ID_INVALID, HTTP_BAD_REQUEST));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseMessage<>(ACCOUNT_DELETE_FAILD, HTTP_BAD_REQUEST));
        }
    }

    @PutMapping("/updateCampagin")
    public ResponseEntity<ResponseMessage<CampaignAndCreativesDTO>> updateCampaign(
            @RequestParam(value = "id", required = true) String strCampaignId,
            @RequestPart(value = "file", required = true) MultipartFile file,
            @RequestPart(value = "data", required = true) CampaignAndCreativesDTO campaignAndCreativesDTO) throws IOException {
        List<String> listMessage = new ArrayList<>();
        if(iCampaignService.isInteger(strCampaignId)){
            Integer campaignId = Integer.parseInt(strCampaignId);
            CreativeDTO creatives = campaignAndCreativesDTO.getCreativesDTO();
            String creativesName = creatives.getName();
            Optional<Campaign> oldCampaign = iCampaignRepository.findByIdAndDeleteFlagIsFalse(campaignId);

            //check campagin is present
            if(oldCampaign.isPresent()){
                //if campagin present: true
                Optional<Creatives> oldCreate = iCreativeRepository.findByCampaignIdAndDeleteFlagIsFalse(oldCampaign);
                //check if creatives is present
                if(oldCreate.isPresent()){
                    //check if creatives is present: true
                    if(creatives.getName().equals(oldCreate.get().getName())
                            || !iCreativeRepository.existsByTitleAndDeleteFlagIsFalse(creativesName)){
                        CampaignDTO campaignDTO = campaignAndCreativesDTO.getCampaignDTO();
                        if (campaignDTO.getEndDate().isAfter(campaignDTO.getStartDate())) {
                            // endDateTime after startDateTime
                            iCampaignService.updateCampaign(campaignId,campaignAndCreativesDTO, file);
                            return ResponseEntity.status(HttpStatus.OK)
                                    .body(new ResponseMessage<>(CAMPAGIGN_UPDATE_SUCCESS, HTTP_OK, campaignAndCreativesDTO));
                        } else {
                            // endDateTime not after startDateTime
                            return ResponseEntity.status(HttpStatus.OK)
                                    .body(new ResponseMessage<>(STARTDATE_IS_AFTER_ENDDATE, HTTP_BAD_REQUEST));
                        }
                    } else {
                        return ResponseEntity.status(HttpStatus.OK)
                                .body(new ResponseMessage<>(CREATIVES_ALREADY_EXISTS, HTTP_BAD_REQUEST));
                    }
                    //check if creatives is present: false
                } else {
                    return ResponseEntity.status(HttpStatus.OK)
                            .body(new ResponseMessage<>(CREATIVES_NOT_FOUND, HTTP_BAD_REQUEST));
                }
                //if campagin present: false
            } else {
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseMessage<>(CAMPAIGN_NOT_FOUND, HTTP_BAD_REQUEST));
            }
        } else{
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseMessage<>(CAMPAIGN_ID_INVALID, HTTP_BAD_REQUEST));
        }
    }

    @PostMapping(value = "/",consumes = {"multipart/form-data"})
    public ResponseEntity<ResponseMessage<CampaignAndCreativesDTO>> createCampaign(
            @RequestPart("file") MultipartFile file,
            @RequestPart("data") CampaignAndCreativesDTO campaignAndCreativesDTO) throws IOException {
        Pageable pageable = PageRequest.of(Integer.parseInt(DEFAULT_PAGE_NUMBER), Integer.parseInt(DEFAULT_PAGE_SIZE));
        Page<User> userPage;
        try{
            userPage = iUserRepository.findNameOrEmail(currenAccount, pageable);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseMessage<>(AppConstants.ACCOUNT_NOT_FOUND, HTTP_BAD_REQUEST));
        }
        List<User> accounts = accountPage.getContent();
        User account = accounts.get(0);
        //check if campagin exist
        boolean isExistCampaign = iCampaignRepository.existsByNameAndDeleteFlagIsFalse(campaignAndCreativesDTO.getCampaignDTO().getName());
        if(!isExistCampaign){
            boolean isExistCreative = iCreativeRepository.existsByTitleAndDeleteFlagIsFalse(campaignAndCreativesDTO.getCreativesDTO().getName());
            if(!isExistCreative){
                CampaignDTO campaignDTO = campaignAndCreativesDTO.getCampaignDTO();
                if (campaignDTO.getEndDate().isAfter(campaignDTO.getStartDate())) {
                    // endDateTime after startDateTime
                    String imgurl = firebaseService.uploadFile(file);
                    campaignAndCreativesDTO.getCreativesDTO().setFinalUrl(imgurl);
                    iCampaignService.createCampaign(campaignAndCreativesDTO, account);
                    return ResponseEntity.status(HttpStatus.OK)
                            .body(new ResponseMessage<>(CAMPAIGN_CREATE_SUCCESS, HTTP_OK,campaignAndCreativesDTO));
                } else {
                    // endDateTime not after startDateTime
                    return ResponseEntity.status(HttpStatus.OK)
                            .body(new ResponseMessage<>(STARTDATE_IS_AFTER_ENDDATE, HTTP_BAD_REQUEST));
                }
            } else{
                return  ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseMessage<>(CREATIVES_ALREADY_EXISTS, HTTP_BAD_REQUEST));
            }
        } else{
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseMessage<>(CAMPAGIGN_ALREADY_EXISTS, HTTP_BAD_REQUEST));
        }
    }
}
