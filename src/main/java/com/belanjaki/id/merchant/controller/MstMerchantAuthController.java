package com.belanjaki.id.merchant.controller;

import com.belanjaki.id.common.constant.BasePath;
import com.belanjaki.id.merchant.dto.request.RequestMerchantLoginDTO;
import com.belanjaki.id.merchant.service.MstMerchantAuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Merchant API", description = "Merchant Rest API")
@RestController
@AllArgsConstructor
public class MstMerchantAuthController {

    private final MstMerchantAuthService mstMerchantAuthService;

    @PostMapping(
            path = BasePath.MERCHANT + "/login",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> loginMerchant(@Valid @RequestBody RequestMerchantLoginDTO dto){
        return new ResponseEntity<>(mstMerchantAuthService.loginMerchant(dto), HttpStatus.OK);
    }



}
