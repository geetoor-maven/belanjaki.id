package com.belanjaki.id.merchant.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Data
public class RequestApproveMerchantDTO {
    @NotBlank(message = "id merchant tidak boleh kosong")
    private String idMerchant;
}
