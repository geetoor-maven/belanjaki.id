package com.belanjaki.id.usersmanagement.dto.user.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Builder
@Data
public class ResponseUserDTO {
    private String name;
    private String email;
    private String numberPhone;
    private String imgUrl;
    @JsonProperty("address")
    private ResponseUserAddressDTO responseUserAddressDTO;
}
