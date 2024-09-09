package com.belanjaki.id.merchant.controller;

import com.belanjaki.id.common.constant.BasePath;
import com.belanjaki.id.merchant.dto.request.RequestApproveMerchantDTO;
import com.belanjaki.id.merchant.dto.request.RequestCreateMerchantDTO;
import com.belanjaki.id.merchant.service.MstMerchantService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Object> createMerchant(@Valid @RequestBody RequestCreateMerchantDTO dto){
        return new ResponseEntity<>(mstMerchantService.createMerchant(dto), HttpStatus.OK);
    }

    @GetMapping(
            path = BasePath.MERCHANT + "/list"
    )
    public ResponseEntity<Object> getListMerchantsByStatus(@RequestParam(value = "status", required = false) String status,
                                                            @RequestParam(defaultValue = "1") int page,
                                                            @RequestParam(defaultValue = "10") int size){
        return new ResponseEntity<>(mstMerchantService.getListMerchantsByStatus(status, page, size), HttpStatus.OK);
    }

    @PutMapping(
            path = BasePath.MERCHANT + "/update-status",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> updateStatusMerchant(@Valid @RequestBody RequestApproveMerchantDTO dto){
        return new ResponseEntity<>(mstMerchantService.approveMerchant(dto), HttpStatus.OK);
    }

}
