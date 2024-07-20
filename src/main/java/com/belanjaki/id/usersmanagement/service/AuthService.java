package com.belanjaki.id.usersmanagement.service;

import com.belanjaki.id.common.ResourceLabel;
import com.belanjaki.id.common.constant.ReturnCode;
import com.belanjaki.id.common.dto.BaseResponse;
import com.belanjaki.id.common.dto.Meta;
import com.belanjaki.id.common.exception.ItemAlreadyExistException;
import com.belanjaki.id.usersmanagement.dto.user.RequestCreateUserDTO;
import com.belanjaki.id.usersmanagement.model.MstUser;
import com.belanjaki.id.usersmanagement.repository.MstUserRepository;
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

    @Transactional
    public Object registerUser(RequestCreateUserDTO dto){

        Optional<MstUser> emailExist = mstUserRepository.findByEmail(dto.getEmail());
        if (emailExist.isPresent()){
            throw new ItemAlreadyExistException(resourceLabel.getBodyLabel("user.create.duplicate.email"));
        }

        MstUser mstUser = new MstUser();
        mstUser.setUserId(UUID.randomUUID());
        mstUser.setName(dto.getName());
        mstUser.setEmail(dto.getEmail());
        mstUser.setPassword(dto.getPassword());
        mstUser.setNumberPhone(dto.getNumberPhone());
        mstUser.setCreatedBy(dto.getName());
        mstUser.setUpdatedBy(dto.getName());

        BaseResponse<MstUser> baseResponse = new BaseResponse<>(mstUser, new Meta(ReturnCode.SUCCESSFULLY_REGISTER.getStatusCode(), ReturnCode.SUCCESSFULLY_REGISTER.getMessage(), ""));
        return baseResponse.getCustomizeResponse("user_create");
    }
}
