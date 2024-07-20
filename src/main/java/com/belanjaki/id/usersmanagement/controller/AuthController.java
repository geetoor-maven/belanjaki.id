package com.belanjaki.id.usersmanagement.controller;

import com.belanjaki.id.common.constant.BasePath;
import com.belanjaki.id.usersmanagement.dto.user.RequestCreateUserDTO;
import com.belanjaki.id.usersmanagement.service.AuthService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping(
            path = BasePath.USER + "/create",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> createUser(@Valid @RequestBody RequestCreateUserDTO dto){
        return new ResponseEntity<>(authService.registerUser(dto), HttpStatus.OK);
    }
}
