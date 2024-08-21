package com.belanjaki.id.administrator.service;

import com.belanjaki.id.administrator.dto.request.RequestCreateAdminDTO;
import com.belanjaki.id.administrator.dto.response.ResponseCreateAdminDTO;
import com.belanjaki.id.administrator.model.MstAdministrator;
import com.belanjaki.id.administrator.repository.MstAdministratorRepository;
import com.belanjaki.id.administrator.validator.AdminValidator;
import com.belanjaki.id.common.ResourceLabel;
import com.belanjaki.id.common.constant.ReturnCode;
import com.belanjaki.id.common.constant.RoleEnum;
import com.belanjaki.id.common.dto.BaseResponse;
import com.belanjaki.id.common.dto.Meta;
import com.belanjaki.id.usersmanagement.model.MstRole;
import com.belanjaki.id.usersmanagement.repository.MstRoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.lang.module.ResolutionException;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class MstAdministratorService {

    private final MstAdministratorRepository mstAdministratorRepository;
    private final MstRoleRepository mstRoleRepository;
    private final AdminValidator adminValidator;
    private final ResourceLabel resourceLabel;
    private final PasswordEncoder passwordEncoder;

    public Object createAdmin(RequestCreateAdminDTO dto){
        adminValidator.validateIfAdminEmailHasBeenRegister(dto);
        MstRole mstRole = mstRoleRepository.findByRoleName(RoleEnum.ADMIN.getRoleName()).orElseThrow(() -> new ResolutionException(resourceLabel.getBodyLabel("role.find.not.found")));
        MstAdministrator mstAdministrator = createObjectMstAdmin(dto, mstRole);
        mstAdministratorRepository.save(mstAdministrator);

        ResponseCreateAdminDTO createAdminDTO = createObjectResponseCreateAdmin(mstAdministrator);
        BaseResponse<ResponseCreateAdminDTO> baseResponse = new BaseResponse<>(createAdminDTO, new Meta(ReturnCode.SUCCESSFULLY_REGISTER.getStatusCode(), ReturnCode.SUCCESSFULLY_REGISTER.getMessage()));
        return baseResponse.getCustomizeResponse("admin_create");
    }

    private ResponseCreateAdminDTO createObjectResponseCreateAdmin(MstAdministrator mstAdministrator){
        return ResponseCreateAdminDTO.builder()
                .adminName(mstAdministrator.getAdminName())
                .email(mstAdministrator.getEmail())
                .build();
    }

    private MstAdministrator createObjectMstAdmin(RequestCreateAdminDTO dto, MstRole role){
        MstAdministrator mstAdministrator = MstAdministrator.builder()
                .adminId(UUID.randomUUID())
                .adminName(dto.getAdminName())
                .adminNumber(dto.getAdminNumber())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .numberPhone(dto.getNumberPhone())
                .mstRole(role)
                .build();
        mstAdministrator.setCreatedBy("belanjaki.id");
        mstAdministrator.setUpdatedBy("belanjaki.id");
        return mstAdministrator;
    }
}
