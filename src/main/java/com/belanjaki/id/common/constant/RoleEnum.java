package com.belanjaki.id.common.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RoleEnum {

    ADMIN(1, "ADMIN"),
    MERCHANT(2, "MERCHANT"),
    USER(3, "USER");

    private final int code;
    private final String roleName;
}
