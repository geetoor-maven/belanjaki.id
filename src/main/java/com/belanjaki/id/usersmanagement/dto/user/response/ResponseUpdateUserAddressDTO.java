package com.belanjaki.id.usersmanagement.dto.user.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Builder
@Data
public class ResponseUpdateUserAddressDTO {
    private String street;
    private String city;
    private String state;
    private String postalCode;
}
