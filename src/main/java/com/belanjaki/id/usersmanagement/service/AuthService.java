package com.belanjaki.id.usersmanagement.service;

import com.belanjaki.id.common.ResourceLabel;
import com.belanjaki.id.common.constant.ReturnCode;
import com.belanjaki.id.common.dto.BaseResponse;
import com.belanjaki.id.common.dto.Meta;
import com.belanjaki.id.common.exception.ItemAlreadyExistException;
import com.belanjaki.id.usersmanagement.dto.user.RequestCreateUserDTO;
import com.belanjaki.id.usersmanagement.model.MstUser;
import com.belanjaki.id.usersmanagement.repository.MstUserRepository;
import com.belanjaki.id.usersmanagement.validator.UserValidator;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@AllArgsConstructor
@Service
public class AuthService {

    private final MstUserRepository mstUserRepository;
    private final ResourceLabel resourceLabel;
    private final UserValidator userValidator;

    @Transactional
    public Object registerUser(RequestCreateUserDTO dto){

        // Check if email already exists
        mstUserRepository.findByEmail(dto.getEmail()).ifPresent(user -> {
            throw new ItemAlreadyExistException(resourceLabel.getBodyLabel("user.create.duplicate.email"));
        });

        // Create new user using builder pattern
        MstUser mstUser = MstUser.builder()
                .userId(UUID.randomUUID())
                .name(dto.getName())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .numberPhone(dto.getNumberPhone())
                .build();
        mstUser.setCreatedBy(dto.getName());
        mstUser.setUpdatedBy(dto.getName());

        // save users
        mstUserRepository.save(mstUser);

        BaseResponse<MstUser> baseResponse = new BaseResponse<>(mstUser, new Meta(ReturnCode.SUCCESSFULLY_REGISTER.getStatusCode(), ReturnCode.SUCCESSFULLY_REGISTER.getMessage(), ""));
        return baseResponse.getCustomizeResponse("user_create");
    }
}
