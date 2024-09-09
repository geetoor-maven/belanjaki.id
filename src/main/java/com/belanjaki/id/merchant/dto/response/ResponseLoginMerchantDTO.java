package com.belanjaki.id.merchant.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseLoginMerchantDTO {
    private String message;
    private String email;
}
