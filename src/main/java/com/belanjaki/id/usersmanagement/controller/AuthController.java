package com.belanjaki.id.usersmanagement.controller;

import com.belanjaki.id.common.constant.BasePath;
import com.belanjaki.id.usersmanagement.dto.user.request.RequestCreateUserDTO;
import com.belanjaki.id.usersmanagement.dto.user.request.RequestLoginUserDTO;
import com.belanjaki.id.usersmanagement.dto.user.request.RequestOtpDTO;
import com.belanjaki.id.usersmanagement.service.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Auth API", description = "Auth User API")
@RestController
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping(
            path = BasePath.USER + "/validate-otp",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> validateOtpAfterLogin(@Valid @RequestBody RequestOtpDTO dto){
        return new ResponseEntity<>(authService.validateOtpAfterLogin(dto), HttpStatus.OK);
    }

    @PostMapping(
            path = BasePath.USER + "/resend-otp",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> resendOtp(@Valid @RequestBody RequestLoginUserDTO dto){
        return new ResponseEntity<>(authService.resendOtp(dto), HttpStatus.OK);
    }

    @PostMapping(
            path = BasePath.USER + "/login",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> loginUser(@Valid @RequestBody RequestLoginUserDTO dto){
        return new ResponseEntity<>(authService.loginUser(dto), HttpStatus.OK);
    }

    @PostMapping(
            path = BasePath.USER + "/create",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> createUser(@Valid @RequestBody RequestCreateUserDTO dto){
        return new ResponseEntity<>(authService.registerUser(dto), HttpStatus.OK);
    }
}
