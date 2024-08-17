package com.belanjaki.id.usersmanagement.dto.user.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Data
public class RequestUpdateUserAddressDTO {

    @NotBlank(message = "nama jalan tidak boleh kosong")
    @Size(max = 200, message = "nama jalan tidak lebih dari 200 digit")
    private String street;

    @NotBlank(message = "nama kota tidak boleh kosong")
    @Size(max = 100, message = "nama kota tidak lebih dari 100 digit")
    private String city;

    @NotBlank(message = "nama negara tidak boleh kosong")
    @Size(max = 100, message = "nama negara tidak lebih dari 100 digit")
    private String state;

    @NotBlank(message = "kode pos tidak boleh kosong")
    @Size(max = 10, message = "nama kota tidak lebih dari 10 digit")
    private String postalCode;

}
