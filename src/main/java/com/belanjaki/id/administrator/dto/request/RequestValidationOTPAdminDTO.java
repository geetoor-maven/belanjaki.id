package com.belanjaki.id.administrator.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Data
public class RequestValidationOTPAdminDTO {

    @NotBlank(message = "Admin number tidak boleh kosong")
    @Pattern(regexp = "\\d+", message = "Admin number hanya boleh mengandung angka")
    private String adminNumber;

    @NotBlank(message = "email tidak boleh kosong")
    @Email(message = "masukkan format email yang benar")
    private String email;

    @Size(min = 6, max = 6, message = "otp harus 6 digit")
    private String otp;
}
