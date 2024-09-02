package com.belanjaki.id.administrator.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseLoginWithOTPAdminDTO {
    private String email;
    private String token;
}
