package com.belanjaki.id.common.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ReturnCode {

    FAILED_DATA_ALREADY_EXISTS(409, "Data duplikat"),
    SUCCESSFULLY_REGISTER(200, "Successfully Register");

    private final int statusCode;
    private final String message;
}
