package com.belanjaki.id.merchant.controller;

import com.belanjaki.id.common.constant.BasePath;
import com.belanjaki.id.merchant.dto.request.RequestCreateMerchantDTO;
import com.belanjaki.id.merchant.service.MstMerchantService;
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
public class MstMerchantController {

    private final MstMerchantService mstMerchantService;

    @PostMapping(
            path = BasePath.MERCHANT + "/create",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> createUser(@Valid @RequestBody RequestCreateMerchantDTO dto){
        return new ResponseEntity<>(mstMerchantService.createMerchant(dto), HttpStatus.OK);
    }

}
