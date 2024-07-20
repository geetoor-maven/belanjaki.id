package com.belanjaki.id.usersmanagement.constant;

import lombok.Getter;

@Getter
public enum RoleType {

    ADMIN("MERCHANT", "Merchant"),
    USER("USER", "User");

    RoleType(String code, String value) {
        this.code = code;
        this.value = value;
    }

    private final String code;
    private final String value;
}
