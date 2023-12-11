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
import com.example.project.service.IFirebaseService;
import com.example.project.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.project.constants.Constants.*;
import static com.example.project.constants.FieldValueLengthConstants.DEFAULT_PAGE_NUMBER;
import static com.example.project.constants.FieldValueLengthConstants.DEFAULT_PAGE_SIZE;
import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;
import static java.net.HttpURLConnection.HTTP_OK;

@CrossOrigin
@RestController
@RequestMapping("/api/campaigns")
@RequiredArgsConstructor
public class CampaignController {
    private final ICampaignService campaignService;
    private final ICampaignRepository campaignRepository;
    private final ICreativeRepository creativeRepository;
    private final IUserRepository iUserRepository;
    private final JwtService jwtService;
    private final IFirebaseService firebaseService;
    private final MessageSource messageSource;
    @GetMapping("/")
    public ResponseEntity<ResponseMessage<Page<CampaignDTO>>> getCampaigns(@RequestParam(value = "keySearch", required = false) String keySearch, @RequestParam("page") int page, @RequestParam("limit") int limit){
        Pageable pageable = PageRequest.of(page, limit, Sort.by("createdAt").ascending());
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseMessage<>(messageSource.getMessage(CAMPAIGN_GET_SUCCESS, null, LocaleContextHolder.getLocale()), HTTP_OK,campaignService.getCampaign(keySearch,pageable)));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ResponseMessage<CampaignDTO>> deleteCampaign(@PathVariable Long id){
        try{
            Optional<Campaign> campaign = campaignRepository.findByIdAndDeleteFlagIsFalse(id);
            if(campaign.isPresent()){
                if(campaign.get().isDeleteFlag() == true){
                    return ResponseEntity.status(HttpStatus.OK)
                            .body(new ResponseMessage<>(messageSource.getMessage(CAMPAIGN_IS_DELETED, null, LocaleContextHolder.getLocale()), HTTP_BAD_REQUEST));
                } else {
                    campaignService.deleteCampaign(id);
                    return ResponseEntity.status(HttpStatus.OK)
                            .body(new ResponseMessage<>(messageSource.getMessage(CAMPAIGN_DELETE_SUCCESS, null, LocaleContextHolder.getLocale()), HTTP_OK));
                }
            }else{
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseMessage<>(messageSource.getMessage(CAMPAIGN_NOT_FOUND, null, LocaleContextHolder.getLocale()), HTTP_BAD_REQUEST));
            }
        } catch (NumberFormatException e){
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseMessage<>(messageSource.getMessage(CAMPAIGN_ID_INVALID, null, LocaleContextHolder.getLocale()), HTTP_BAD_REQUEST));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseMessage<>(messageSource.getMessage(USER_DELETE_FAIL, null, LocaleContextHolder.getLocale()), HTTP_BAD_REQUEST));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseMessage<CampaignAndCreativesDTO>> updateCampaign(
            @Valid @PathVariable Long id,
            @RequestPart(value = "file", required = true) MultipartFile file,
            @RequestPart(value = "data", required = true) CampaignAndCreativesDTO campaignAndCreativesDTO) throws IOException {
        List<String> listMessage = new ArrayList<>();

            CreativeDTO creatives = campaignAndCreativesDTO.getCreativesDTO();
            String creativesName = creatives.getTitle();
            Optional<Campaign> oldCampaign = campaignRepository.findByIdAndDeleteFlagIsFalse(id);

            //check campagin is present
            if(oldCampaign.isPresent()){
                //if campagin present: true
                Optional<Creatives> oldCreate = creativeRepository.findByCampaignIdAndDeleteFlagIsFalse(oldCampaign);
                //check if creatives is present
                if(oldCreate.isPresent()){
                    //check if creatives is present: true
                    if(creatives.getTitle().equals(oldCreate.get().getTitle())
                            || !creativeRepository.existsByTitleAndDeleteFlagIsFalse(creativesName)){
                        CampaignDTO campaignDTO = campaignAndCreativesDTO.getCampaignDTO();
                        if (campaignDTO.getEndDate().isAfter(campaignDTO.getStartDate())) {
                            // endDateTime after startDateTime
                            campaignService.updateCampaign(id,campaignAndCreativesDTO, file);
                            return ResponseEntity.status(HttpStatus.OK)
                                    .body(new ResponseMessage<>(messageSource.getMessage(CAMPAIGN_UPDATE_SUCCESS, null, LocaleContextHolder.getLocale()), HTTP_OK, campaignAndCreativesDTO));
                        } else {
                            // endDateTime not after startDateTime
                            return ResponseEntity.status(HttpStatus.OK)
                                    .body(new ResponseMessage<>(messageSource.getMessage(STARTDATE_IS_AFTER_ENDDATE, null, LocaleContextHolder.getLocale()), HTTP_BAD_REQUEST));
                        }
                    } else {
                        return ResponseEntity.status(HttpStatus.OK)
                                .body(new ResponseMessage<>(messageSource.getMessage(CREATIVES_ALREADY_EXISTS, null, LocaleContextHolder.getLocale()), HTTP_BAD_REQUEST));
                    }
                    //check if creatives is present: false
                } else {
                    return ResponseEntity.status(HttpStatus.OK)
                            .body(new ResponseMessage<>(messageSource.getMessage(CREATIVES_NOT_FOUND, null, LocaleContextHolder.getLocale()), HTTP_BAD_REQUEST));
                }
                //if campagin present: false
            } else {
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseMessage<>(messageSource.getMessage(CAMPAIGN_NOT_FOUND, null, LocaleContextHolder.getLocale()), HTTP_BAD_REQUEST));
            }
    }

    @PostMapping(value = "/", consumes = {"multipart/form-data"})
    public ResponseEntity<ResponseMessage<CampaignAndCreativesDTO>> createCampaign(
            @RequestPart("file") MultipartFile file,
            @RequestPart("data") CampaignAndCreativesDTO campaignAndCreativesDTO,
            @RequestHeader("Authorization") String bearerToken) throws IOException {
        bearerToken = bearerToken.replace(messageSource.getMessage(BEARER_PREFIX, null, LocaleContextHolder.getLocale()), "");
        final String currenAccount = jwtService.extractUsername(bearerToken);
        Pageable pageable = PageRequest.of(DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE);
        Page<User> accountPage;
        try{
            accountPage = iUserRepository.findNameOrEmail(currenAccount, pageable);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseMessage<>(messageSource.getMessage(USER_NOT_FOUND, null, LocaleContextHolder.getLocale()), HTTP_BAD_REQUEST));
        }
        List<User> users = accountPage.getContent();
        User user = users.get(0);
        //check if campaign exist
        boolean isExistCampaign = campaignRepository.existsByNameAndDeleteFlagIsFalse(campaignAndCreativesDTO.getCampaignDTO().getName());
        if(!isExistCampaign){
            boolean isExistCreative = creativeRepository.existsByTitleAndDeleteFlagIsFalse(campaignAndCreativesDTO.getCreativesDTO().getTitle());
            if(!isExistCreative){
                CampaignDTO campaignDTO = campaignAndCreativesDTO.getCampaignDTO();
                if (campaignDTO.getEndDate().isAfter(campaignDTO.getStartDate())) {
                    // endDateTime after startDateTime
                    String imgurl = firebaseService.uploadFile(file);
                    campaignAndCreativesDTO.getCreativesDTO().setImageUrl(imgurl);
                    campaignService.createCampaign(campaignAndCreativesDTO, user);
                    return ResponseEntity.status(HttpStatus.OK)
                            .body(new ResponseMessage<>(messageSource.getMessage(CAMPAIGN_CREATE_SUCCESS, null, LocaleContextHolder.getLocale()), HTTP_OK,campaignAndCreativesDTO));
                } else {
                    // endDateTime not after startDateTime
                    return ResponseEntity.status(HttpStatus.OK)
                            .body(new ResponseMessage<>(messageSource.getMessage(STARTDATE_IS_AFTER_ENDDATE, null, LocaleContextHolder.getLocale()), HTTP_BAD_REQUEST));
                }
            } else{
                return  ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseMessage<>(messageSource.getMessage(CREATIVES_ALREADY_EXISTS, null, LocaleContextHolder.getLocale()), HTTP_BAD_REQUEST));
            }
        } else{
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseMessage<>(messageSource.getMessage(CAMPAIGN_ALREADY_EXISTS, null, LocaleContextHolder.getLocale()), HTTP_BAD_REQUEST));
        }
    }


}
