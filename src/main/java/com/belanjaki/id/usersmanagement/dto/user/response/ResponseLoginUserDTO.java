package com.belanjaki.id.usersmanagement.dto.user.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseLoginUserDTO {
    private String message;
    private String email;
}
