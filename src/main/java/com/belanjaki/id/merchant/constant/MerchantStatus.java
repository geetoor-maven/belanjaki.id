package com.belanjaki.id.merchant.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MerchantStatus {

    APPROVE(0, "APPROVE"),
    FOR_APPROVAL(1, "FOR_APP"),
    ACTIVE(2, "ACTIVE"),
    NON_ACTIVE(3, "NON_ACTIVE");

    private final int code;
    private final String merchantStatus;
}
