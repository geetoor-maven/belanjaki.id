package com.belanjaki.id.merchant.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

@Getter
@AllArgsConstructor
public enum MerchantStatus {

    APPROVE("APPROVE", "Approved"),
    FOR_APPROVAL("FOR_APP", "For Approved"),
    ACTIVE("ACTIVE", "Active"),
    NON_ACTIVE("NON_ACTIVE", "Non-Active");

    private final String code;
    private final String merchantStatus;

    public static String getMerchantStatus(String code) {
        String value = "-";
        List<MerchantStatus> accountingEnums = new ArrayList<>(EnumSet.allOf(MerchantStatus.class));
        for (MerchantStatus accountingEnum : accountingEnums) {
            if (accountingEnum.getCode().equalsIgnoreCase(code)) {
                value = accountingEnum.getMerchantStatus();
                break;
            }
        }
        return value;
    }

}
