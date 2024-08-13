package com.belanjaki.id.usersmanagement.dto.user.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseLoginWithOtpDTO {
    private String email;
    private String token;
}
