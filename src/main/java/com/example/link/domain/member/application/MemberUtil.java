package com.example.link.domain.member.application;

import java.util.regex.Pattern;

public class MemberUtil {
    public static final String PHONE_NUMBER_PATTERN = "^01(?:0|1|[6-9])(\\d{3}|\\d{4})(\\d{4})$";
    public static final String EMAIL_PATTERN = "^[a-zA-Z0-9_!#$%&'*+/=?{|}~^.-]+@[a-zA-Z0-9.-]+$";
    public static boolean isValidPhoneNumber(String phoneNumber) {
        return Pattern.compile(PHONE_NUMBER_PATTERN).matcher(phoneNumber).matches();
    }
    public static boolean isValidEmail(String email) {
        return Pattern.compile(EMAIL_PATTERN).matcher(email).matches();
    }
}
