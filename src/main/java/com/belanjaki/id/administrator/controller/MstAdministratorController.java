package com.belanjaki.id.administrator.controller;

import com.belanjaki.id.administrator.dto.request.RequestCreateAdminDTO;
import com.belanjaki.id.administrator.service.MstAdministratorService;
import com.belanjaki.id.common.constant.BasePath;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Administrator API", description = "Administrator Rest API")
@RestController
@AllArgsConstructor
public class MstAdministratorController {

    private final MstAdministratorService mstAdministratorService;

    @PostMapping(
            path = BasePath.ADMIN + "/create",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> createAdministrator(@Valid @RequestBody RequestCreateAdminDTO dto){
        return new ResponseEntity<>(mstAdministratorService.createAdmin(dto), HttpStatus.OK);
    }
}
