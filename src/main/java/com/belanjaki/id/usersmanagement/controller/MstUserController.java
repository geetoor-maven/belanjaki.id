package com.belanjaki.id.usersmanagement.controller;


import com.belanjaki.id.common.constant.BasePath;
import com.belanjaki.id.usersmanagement.dto.user.request.RequestUpdateUserAddressDTO;
import com.belanjaki.id.usersmanagement.service.MstUserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User API", description = "User API")
@RestController
@AllArgsConstructor
public class MstUserController {

    private final MstUserService mstUserService;

    @PutMapping(
            path = BasePath.USER + "/update-address",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> validateOtpAfterLogin(@Valid @RequestBody RequestUpdateUserAddressDTO dto){
        return new ResponseEntity<>(mstUserService.updateUserAddress(dto), HttpStatus.OK);
    }

    @GetMapping(
            path = BasePath.USER + "/user-address"
    )
    public ResponseEntity<Object> findUserAddress(){
        return new ResponseEntity<>(mstUserService.findUserAddress(), HttpStatus.OK);
    }
}
