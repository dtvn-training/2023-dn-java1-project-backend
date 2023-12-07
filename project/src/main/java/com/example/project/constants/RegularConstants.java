package com.example.project.constants;

import lombok.Getter;

@Getter
public class RegularConstants {
    public static final String PHONE_NUMBER_REGEX = "^\\(?(\\+\\d{1,3})?\\)?[-.\\s]?\\d{1,3}[-.\\s]?\\d{3,5}[-.\\s]?\\d{4}(?:\\s?(\\w{1,10})\\s?(\\d{1,6}))?$";
    public static final String EMAIL_REGEX = "^[a-zA-Z0-9.!#$%&’*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$";
    public static final String NAME_REGEX = "^(?!\\s)(?!.*\\s$)[\\p{L}]+(?:\\s[\\p{L}]+)*$";
    public static final String ADDRESS_REGEX = "^[\\p{L}0-9\\s,'-.]+";
    public static final String PASSWORD_REGEX = "/^.{9,}$/";
}

