package com.belanjaki.id.common.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseMessageAndEmailDTO {
    private String message;
    private String email;
}
