package com.belanjaki.id.administrator.controller;

import com.belanjaki.id.administrator.dto.request.RequestLoginAdminDTO;
import com.belanjaki.id.administrator.service.AuthAdministratorService;
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

@Tag(name = "Auth Administrator API", description = "Auth Administrator Rest API")
@RestController
@AllArgsConstructor
public class AuthAdministratorController {

    private final AuthAdministratorService authAdministratorService;

    @PostMapping(
            path = BasePath.ADMIN + "/login",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> createAdministrator(@Valid @RequestBody RequestLoginAdminDTO dto){
        return new ResponseEntity<>(authAdministratorService.loginAdministrator(dto), HttpStatus.OK);
    }

}
