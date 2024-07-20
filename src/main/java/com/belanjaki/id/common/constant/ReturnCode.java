package com.belanjaki.id.common.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ReturnCode {

    SUCCESSFULLY_REGISTER(200, "Successfully Register");

    private final int statusCode;
    private final String message;
}
