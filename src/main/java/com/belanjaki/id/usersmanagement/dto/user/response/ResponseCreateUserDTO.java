package com.belanjaki.id.usersmanagement.dto.user.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Data
public class ResponseCreateUserDTO {
    private String name;
    private String email;
}
