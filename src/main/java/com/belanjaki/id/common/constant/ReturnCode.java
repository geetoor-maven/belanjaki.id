package com.belanjaki.id.common.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ReturnCode {

    SUCCESSFULLY_LOGIN(200, "You have successfully login."),
    SUCCESSFULLY_OTP_SEND(200, "Otp send successfully"),
    SUCCESSFULLY_REGISTER(200, "You have successfully registered."),

    BAD_REQUEST(400, "Bad Request"),
    FAILED_BAD_REQUEST(400, "Request Tidak Sesuai"),
    DATA_NOT_FOUND(404, "Not found."),
    FAILED_DATA_ALREADY_EXISTS(409, "Duplicate registration: You are already registered.");

    private final int statusCode;
    private final String message;
}
