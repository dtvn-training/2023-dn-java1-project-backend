package com.example.project.utlis.validator;

import com.example.project.dto.response.CampaignAndCreativesDTO;
import com.example.project.dto.response.CampaignDTO;
import com.example.project.dto.response.CreativeDTO;
import com.example.project.exception.ErrorException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.example.project.constants.Constants.*;
import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;
@Component
@RequiredArgsConstructor
public class CampaignValidator {
    private final MessageSource messageSource;
    //private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public void validatorCreate(CampaignAndCreativesDTO campaignAndCreativesDTO) {
        CampaignDTO campaignDTO = campaignAndCreativesDTO.getCampaignDTO();
        CreativeDTO creativeDTO = campaignAndCreativesDTO.getCreativesDTO();
        validatorName(campaignDTO.getName());
        validatorBidAmount(campaignDTO);
        validatorBudget(campaignDTO);
        validatorTitle(creativeDTO.getTitle());
        validatorDescription(creativeDTO.getDescription());
    }

    public void validatorName(String name){
        if(name == null || name.isEmpty()){
            throw new ErrorException(messageSource.getMessage(ERROR_CAMPAIGN_NAME_REQUIRED, null, LocaleContextHolder.getLocale()), HTTP_BAD_REQUEST);
        }
    }

    private void validatorBidAmount( CampaignDTO campaignDTO){
        if(campaignDTO.getBidAmount() == null){
            campaignDTO.setBidAmount(0.0);
        }
    }

    private void validatorBudget(CampaignDTO campaignDTO) {
        if(campaignDTO.getBudget() == null){
            campaignDTO.setBudget(0.0);
        }
    }

    private void validatorTitle(String title){
        if(title == null||  title.isEmpty() ){
            throw new ErrorException(messageSource.getMessage(ERROR_TITLE_REQUIRED, null, LocaleContextHolder.getLocale()), HTTP_BAD_REQUEST);
        }
    }

    private void validatorDescription(String description){
        if(description == null || description.isEmpty()){
            throw new ErrorException(messageSource.getMessage(ERROR_DESCRIPTION_REQUIRED, null, LocaleContextHolder.getLocale()), HTTP_BAD_REQUEST);
        }
    }

    public void validatorImage(MultipartFile file){
        try {
            BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(file.getBytes()));
            if (bufferedImage == null) {
                throw new ErrorException(messageSource.getMessage(NOT_AN_IMAGE, null, LocaleContextHolder.getLocale()), HTTP_BAD_REQUEST);
            }
        } catch (IOException e) {
            throw new ErrorException(messageSource.getMessage(IMAGE_CHECK_ERROR, null, LocaleContextHolder.getLocale()), HTTP_BAD_REQUEST);
        }
    }
}
