package com.mkit.mshanoi.domain.repository;

/**
 * Created by MinhDN on 1/2/2018.
 */

public class ServiceUrl {
    public static final String GET_TOKEN_DEV = "/vnptline/rest/token/get";

    // user
    public static final String CHECK_USER = "/vnptline/rest/user/check";
    public static final String LOGIN = "/vnptline/rest/user/login";
    public static final String CHANGE_PASSWORD = "/vnptline/rest/user/password/change";
    public static final String FORGOT_PASSWORD = "/vnptline/rest/user/password/forgot";
    public static final String REGISTER_USER = "/vnptline/rest/user/registration";
    // end

    // otp
    public static final String GET_OTP = "/vnptline/rest/otp/send";
    public static final String VERIFY_OTP = "/vnptline/rest/otp/verify";
    // end
}
