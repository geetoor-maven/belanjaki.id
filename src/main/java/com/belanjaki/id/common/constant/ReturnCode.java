package com.belanjaki.id.common.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ReturnCode {

    SUCCESSFULLY_LOGIN(200, "You have successfully login."),
    SUCCESSFULLY_OTP_SEND(200, "Otp send successfully"),
    SUCCESSFULLY_REGISTER(200, "You have successfully registered."),
    SUCCESSFULLY_UPDATE_ADDRESS(200, "You have successfully update address."),
    SUCCESSFULLY_UPDATE_PHOTO(200, "You have successfully update photo."),
    SUCCESSFULLY_GET_DATA(200, "Successfully get data"),
    SUCCESSFULLY_UPDATED(200, "Successfully update data"),

    BAD_REQUEST(400, "Bad Request"),
    FAILED_BAD_REQUEST(400, "Request Tidak Sesuai"),
    UNAUTHORIZED(401, "Invalid credentials"),
    ACCESS_DENIED(403, "Access Denied"),
    NOT_ADMIN(403, "Your are not administrator"),
    NOT_USER(403, "Your are not user"),
    DATA_NOT_FOUND(404, "Not found."),
    SOMETHING_WRONG(409, "Something wrong"),
    FAILED_DATA_ALREADY_EXISTS(409, "Duplicate registration: You are already registered.");

    private final int statusCode;
    private final String message;
}
