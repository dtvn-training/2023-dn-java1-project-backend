package com.example.project.constants;

import org.springframework.stereotype.Component;

public class Constants {

    // Validation Errors
    public static final String ERROR_EMAIL_REQUIRED = "error.email.required";
    public static final String ERROR_PASSWORD_REQUIRED = "error.password.required";
    public static final String ERROR_FIRSTNAME_REQUIRED = "error.firstname.required";
    public static final String ERROR_LASTNAME_REQUIRED = "error.lastname.required";
    public static final String ERROR_ADDRESS_REQUIRED = "error.address.required";
    public static final String ERROR_PHONE_REQUIRED = "error.phone.required";

    public static final String ERROR_EMAIL_MAX_LENGTH = "error.email.max.length";
    public static final String ERROR_FIRSTNAME_MAX_LENGTH = "error.firstname.max.length";
    public static final String ERROR_LASTNAME_MAX_LENGTH = "error.lastname.max.length";

    public static final String ERROR_EMAIL_INVALID = "error.email.invalid";
    public static final String ERROR_PASSWORD_INVALID = "error.password.invalid";
    public static final String ERROR_FIRSTNAME_INVALID = "error.firstname.invalid";
    public static final String ERROR_LASTNAME_INVALID = "error.lastname.invalid";
    public static final String ERROR_PHONE_INVALID = "error.phone.invalid";
    public static final String ERROR_ADDRESS_INVALID = "error.address.invalid";

    // User Errors
    public static final String ERROR_EMAIL_ALREADY_EXISTS = "error.email.already.exists";
    public static final String USER_NOT_FOUND = "user.not.found";
    public static final String USER_DELETE_SUCCESS = "user.delete.success";
    public static final String USER_ID_INVALID = "user.id.invalid";
    public static final String USER_UPDATE_SUCCESS = "user.update.success";
    public static final String USER_GET_ALL_SUCCESS = "user.get.all.success";
    public static final String USER_IS_DELETED = "user.is.deleted";
    public static final String USER_DELETE_FAIL = "user.delete.fail";
    public static final String USER_REGISTER_SUCCESS = "user.register.success";
    public static final String USER_REGISTER_FAILED = "user.register.failed";
    public static final String USER_UPDATE_FAIL = "user.update.fail";

    // Roles Errors
    public static final String ROLES_GET_ALL_FAILED = "roles.get.all.failed";
    public static final String ROLES_GET_ALL_SUCCESS = "roles.get.all.success";

    public  static final String ERROR_ROLE_NOT_FOUND = "role.not.found";
    public static final String CAMPAIGN_GET_SUCCESS = "campaign.get.success";

    public static final String CAMPAIGN_CREATE_FAILED = "campaign.create.failed";
    public static final String CAMPAIGN_CREATE_SUCCESS = "campaign.create.successs";
    public static final String CAMPAIGN_NOT_FOUND = "campaign.not.found";
    public static final String CAMPAIGN_DELETE_SUCCESS= "campaign.delete.success";
    public static final String CAMPAIGN_ID_INVALID= "campaign.id.invalid";
    public static final String CAMPAIGN_IS_DELETED = "campaign.id.delete";
    public static final String CAMPAIGN_ALREADY_EXISTS = "campaign.already.exits";
    public static final String CAMPAIGN_UPDATE_SUCCESS = "campaign.update.success";
    public static final String CAMPAIGN_UPDATE_FAILED = "campaign.update.failed";
    public static final String CREATIVES_ALREADY_EXISTS = "creativies.already.exists";
    public static final String CREATIVES_NOT_FOUND = "creativies.not.found";
    public static final String STARTDATE_IS_AFTER_ENDDATE = "start.date.is.after.enddate";

    //Firebase Constants
    public static final String FIREBASE_SDK_JSON ="firebase.sdk.json";
    public static final String FIREBASE_BUCKET = "firebase.bucket";
    public static final String FIREBASE_PROJECT_ID ="firebase.id";

    public static final String BEARER_PREFIX = "bearer.prefix ";

}
