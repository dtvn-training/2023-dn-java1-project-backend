package com.example.project.service;

import com.example.project.constants.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
@Service
public class ErrorService {
    private final Constants constants;
    private final MessageSource messageSource;
    public ErrorService(Constants constants, MessageSource messageSource) {
        this.constants = constants;
        this.messageSource = messageSource;
    }

    public String processErrorEmailRequired(){
        return getMessage(constants.ERROR_EMAIL_REQUIRED);
    }

    public String processErrorPasswordRequired(){
        return getMessage(constants.ERROR_PASSWORD_REQUIRED);
    }

    public String processErrorFirstNamedRequired(){
        return getMessage(constants.ERROR_FIRSTNAME_REQUIRED);
    }

    public String processErrorLastNameRequired(){
        return getMessage(constants.ERROR_LASTNAME_REQUIRED);
    }
    public String processErrorAddressRequired(){
        return getMessage(constants.ERROR_ADDRESS_REQUIRED);
    }

    public String processErrorPhoneRequired(){
        return getMessage(constants.ERROR_PHONE_REQUIRED);
    }

    public String processErrorEmailLength(){
        return getMessage(constants.ERROR_EMAIL_MAX_LENGTH);
    }
    public String processErrorFirstNameLength(){
        return getMessage(constants.ERROR_FIRSTNAME_MAX_LENGTH);
    }
    public String processErrorLastNameLength(){
        return getMessage(constants.ERROR_LASTNAME_MAX_LENGTH);
    }
    public String processErrorAddressLength(){
        return getMessage(constants.ERROR_ADDRESS_MAX_LENGTH);
    }
    public String processErrorPhoneLength(){
        return getMessage(constants.ERROR_PHONE_MAX_LENGTH);
    }
    public String processErrorEmailInvalid(){
        return getMessage(constants.ERROR_EMAIL_INVALID);
    }

    public String processErrorPasswordInvalid(){
        return getMessage(constants.ERROR_PASSWORD_INVALID);
    }
    public String processErrorFirstNameInvalid(){
        return getMessage(constants.ERROR_FIRSTNAME_INVALID);
    }
    public String processErrorLastNameInvalid(){
        return getMessage(constants.ERROR_LASTNAME_INVALID);
    }

    public String processErrorPhoneInvalId(){
        return getMessage(constants.ERROR_PHONE_INVALID);
    }
    public String processErrorAddressInvalid(){
        return getMessage(constants.ERROR_ADDRESS_INVALID);
    }
    public String processErrorEmailAlreadyExist(){
        return getMessage(constants.ERROR_EMAIL_ALREADY_EXISTS);
    }
    public String processNotFound() {
        return getMessage(constants.USER_NOT_FOUND);
    }


    public String processIdInvalid() {
        return getMessage(constants.USER_ID_INVALID);
    }
    public String processUserGetAllSuccess() {
        return getMessage(constants.USER_GET_ALL_SUCCESS);
    }

    public String processRoleGetAllSuccess() {
        return getMessage(constants.ROLES_GET_ALL_SUCCESS);
    }

    public String processRoleGetAllFail() {
        return getMessage(constants.ROLES_GET_ALL_FAILED);
    }

    public String processUserIsDelete() {
        return getMessage(constants.USER_IS_DELETED);
    }

    public String processDeleteSuccess() {
        return getMessage(constants.USER_DELETE_SUCCESS);
    }
    public String processUserDeleteFail() {
        return getMessage(constants.USER_DELETE_FAIL);
    }
    public String processUserRegisterSuccess() {
        return getMessage(constants.USER_REGISTER_SUCCESS);
    }
    public String processUserRegisterFail() {
        return getMessage(constants.USER_REGISTER_FAILED);
    }

    public String processUpdateSuccess() {
        return getMessage(constants.USER_UPDATE_SUCCESS);
    }

    public String processUpdateFail() {
        return getMessage(constants.USER_UPDATE_FAIL);
    }

    public String processGetCampaignSuccess(){
        return getMessage(constants.CAMPAIGN_GET_SUCCESS);
    }

    public String processCreateCampaignFail(){
        return getMessage(constants.CAMPAIGN_CREATE_FAILED);
    }

    public String processCreateCampaignSuccess(){
        return getMessage(constants.CAMPAIGN_CREATE_SUCCESS);
    }

    public String processCampaignNotFound(){
        return getMessage(constants.CAMPAIGN_NOT_FOUND);
    }

    public String processDeleteCampaignSuccess(){
        return getMessage(constants.CAMPAIGN_DELETE_SUCCESS);
    }

    public String processCampaignIdInvalid(){
        return getMessage(constants.CAMPAIGN_ID_INVALID);
    }

    public String processCampaignIdDelete(){
        return getMessage(constants.CAMPAIGN_IS_DELETED);
    }

    public String processCampaignAlreadyExit(){
        return getMessage(constants.CAMPAIGN_ALREADY_EXISTS);
    }

    public String processCampaignUpdateSuccess(){
        return getMessage(constants.CAMPAIGN_UPDATE_SUCCESS);
    }

    public String processCampaignUpdateFail(){
        return getMessage(constants.CAMPAIGN_UPDATE_FAILED);
    }

    public String processCreativeAlreadyExit(){
        return getMessage(constants.CREATIVES_ALREADY_EXISTS);
    }

    public String processCreativenNotFound(){
        return getMessage(constants.CREATIVES_NOT_FOUND);
    }

    public String processCheckTime(){
        return getMessage(constants.STARTDATE_IS_AFTER_ENDDATE);
    }
    private String getMessage(String messageKey) {
        return messageSource.getMessage(messageKey, null, LocaleContextHolder.getLocale());
    }
}
