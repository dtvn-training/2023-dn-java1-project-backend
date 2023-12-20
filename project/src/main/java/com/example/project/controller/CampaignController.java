package com.example.project.controller;

import com.example.project.dto.response.*;
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

import javax.imageio.ImageIO;
import javax.validation.Valid;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.example.project.constants.Constants.*;
import static com.example.project.constants.FieldValueLengthConstants.DEFAULT_PAGE_NUMBER;
import static com.example.project.constants.FieldValueLengthConstants.DEFAULT_PAGE_SIZE;
import static java.net.HttpURLConnection.*;

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
    @GetMapping("")
    public ResponseEntity<ResponseMessage<Page<CampaignAndImgDTO>>> getCampaigns(@RequestParam(value = "keySearch", required = false) String keySearch,
                                                                                 @RequestParam(value = "page", required = false) int page,
                                                                                 @RequestParam(value = "limit", required = false) int limit,
                                                                                 @RequestParam(value = "startDate", required = false) String startDate,
                                                                                 @RequestParam(value = "endDate", required = false) String endDate){
        LocalDateTime startDateTime = null;
        LocalDateTime endDateTime = null;

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            if (startDate != null && !startDate.isEmpty()) {
                Date parsedSDate = dateFormat.parse(startDate);
                startDateTime = parsedSDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            }
            if (endDate != null && !endDate.isEmpty()) {
                Date parsedEDate = dateFormat.parse(endDate);
                endDateTime = parsedEDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            }

        } catch (ParseException e) {
            return ResponseEntity.badRequest().body(new ResponseMessage<>(e.getMessage(), HTTP_BAD_REQUEST));
        }
        Pageable pageable = PageRequest.of(page, limit, Sort.by("createdAt").ascending());
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseMessage<>(messageSource.getMessage(CAMPAIGN_GET_SUCCESS, null, LocaleContextHolder.getLocale()), HTTP_OK,campaignService.getCampaign(keySearch,startDateTime, endDateTime, pageable)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCampaign(@PathVariable Long id) {
        try {
            CampaignAndCreativesDTO campaignAndCreativesDTO = campaignService.getCampaignAndCreativesDTOById(id);
            return ResponseEntity.ok(campaignAndCreativesDTO);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseMessage<>(e.getMessage(), HTTP_BAD_REQUEST));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseMessage<CampaignDTO>> deleteCampaign(@PathVariable Long id){
        try{
            Optional<Campaign> campaign = campaignRepository.findByIdAndDeleteFlagIsFalse(id);
            if(campaign.isPresent()){
                if(campaign.get().isDeleteFlag()){
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
            @RequestPart(value = "data", required = true) CampaignAndCreativesDTO campaignAndCreativesDTO) {
        CreativeDTO creatives = campaignAndCreativesDTO.getCreativesDTO();
        String creativesName = creatives.getTitle();
        Optional<Campaign> oldCampaign = campaignRepository.findByIdAndDeleteFlagIsFalse(id);
        //check campaign is present
        if(!oldCampaign.isPresent()){
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseMessage<>(messageSource.getMessage(CAMPAIGN_NOT_FOUND, null, LocaleContextHolder.getLocale()), HTTP_BAD_REQUEST));
        }
        Optional<Creatives> oldCreate = creativeRepository.findByCampaignIdAndDeleteFlagIsFalse(oldCampaign);
        //check if creatives is not present
        if(!oldCreate.isPresent()){
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseMessage<>(messageSource.getMessage(CREATIVES_NOT_FOUND, null, LocaleContextHolder.getLocale()), HTTP_BAD_REQUEST));
        }
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
                        .body(new ResponseMessage<>(messageSource.getMessage(START_DATE_IS_AFTER_END_DATE, null, LocaleContextHolder.getLocale()), HTTP_BAD_REQUEST));
            }
        } else {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseMessage<>(messageSource.getMessage(CREATIVES_ALREADY_EXISTS, null, LocaleContextHolder.getLocale()), HTTP_BAD_REQUEST));
        }
    }

    @PostMapping(value = "", consumes = {"multipart/form-data"})
    public ResponseEntity<ResponseMessage<CampaignAndCreativesDTO>> createCampaign(
            @RequestPart("file") MultipartFile file,
            @RequestPart("data") CampaignAndCreativesDTO campaignAndCreativesDTO,
            @RequestHeader("Authorization") String bearerToken) throws IOException {
        bearerToken = bearerToken.replace(messageSource.getMessage(BEARER_PREFIX, null, LocaleContextHolder.getLocale()), "");
        final String currenAccount = jwtService.findUsername(bearerToken);
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
        boolean isExistCampaign = campaignRepository.existsByNameAndDeleteFlagIsFalse(campaignAndCreativesDTO.getCampaignDTO().getName());
        if(isExistCampaign){
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseMessage<>(messageSource.getMessage(CAMPAIGN_ALREADY_EXISTS, null, LocaleContextHolder.getLocale()), HTTP_BAD_REQUEST));
        }
        boolean isExistCreative = creativeRepository.existsByTitleAndDeleteFlagIsFalse(campaignAndCreativesDTO.getCreativesDTO().getTitle());
        if(!isExistCreative){
            CampaignDTO campaignDTO = campaignAndCreativesDTO.getCampaignDTO();
            if (campaignDTO.getEndDate().isAfter(campaignDTO.getStartDate())) {
                try {
                    BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(file.getBytes()));
                    if (bufferedImage == null) {
                        return ResponseEntity.status(HttpStatus.OK)
                                .body(new ResponseMessage<>(messageSource.getMessage(NOT_AN_IMAGE, null, LocaleContextHolder.getLocale()), HTTP_BAD_REQUEST));
                    }
                } catch (IOException e) {
                    return ResponseEntity.status(HttpStatus.OK)
                            .body(new ResponseMessage<>(messageSource.getMessage(IMAGE_CHECK_ERROR, null, LocaleContextHolder.getLocale()), HTTP_BAD_REQUEST));
                }
                //check size image
                if (file.getSize() > 10 * 1024 * 1024) {
                    return ResponseEntity.status(HttpStatus.OK)
                            .body(new ResponseMessage<>(messageSource.getMessage(IMAGE_MAX_SIZE, null, LocaleContextHolder.getLocale()), HTTP_BAD_REQUEST));
                }
                // endDateTime after startDateTime
                String imgurl = firebaseService.uploadFile(file);
                campaignAndCreativesDTO.getCreativesDTO().setImageUrl(imgurl);
                campaignService.createCampaign(campaignAndCreativesDTO, user);
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseMessage<>(messageSource.getMessage(CAMPAIGN_CREATE_SUCCESS, null, LocaleContextHolder.getLocale()), HTTP_OK,campaignAndCreativesDTO));
            } else {
                // endDateTime not after startDateTime
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseMessage<>(messageSource.getMessage(START_DATE_IS_AFTER_END_DATE, null, LocaleContextHolder.getLocale()), HTTP_BAD_REQUEST));
            }
        } else{
            return  ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseMessage<>(messageSource.getMessage(CREATIVES_ALREADY_EXISTS, null, LocaleContextHolder.getLocale()), HTTP_BAD_REQUEST));

        }
    }

    @GetMapping("/banner")
    public ResponseEntity<ResponseMessage<List<BannerDTO>>> showBanner(){
        try{
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseMessage<>(messageSource.getMessage(GET_TOP_BANNER_SUCCESS, null, LocaleContextHolder.getLocale()), HTTP_OK, campaignService.listBannerUrl()));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseMessage<>(messageSource.getMessage(GET_TOP_BANNER_FAIL,null, LocaleContextHolder.getLocale()), HTTP_BAD_REQUEST));
        }
    }

    @GetMapping("/impression")
    public ResponseEntity<?> impression(@RequestParam(value = "id", required = true) Long id) {
        try{
            campaignService.impression(id);
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseMessage<>(messageSource.getMessage(IMPRESSION_FAIL,null, LocaleContextHolder.getLocale()), HTTP_BAD_REQUEST));
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseMessage<>(messageSource.getMessage(IMPRESSION_SUCCESS, null, LocaleContextHolder.getLocale()), HTTP_BAD_REQUEST));
    }
}
