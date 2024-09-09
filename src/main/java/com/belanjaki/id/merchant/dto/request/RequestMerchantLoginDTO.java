package com.belanjaki.id.merchant.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Data
public class RequestMerchantLoginDTO {
    @NotBlank(message = "email tidak boleh kosong")
    @Email(message = "masukkan format email yang benar")
    private String email;

    @NotBlank(message = "password tidak boleh kosong")
    @Size(min = 8, message = "password harus 8 karakter atau lebih")
    private String password;
}
