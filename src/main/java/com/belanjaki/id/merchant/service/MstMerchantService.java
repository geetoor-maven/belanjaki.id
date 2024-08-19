package com.belanjaki.id.merchant.service;

import com.belanjaki.id.common.ResourceLabel;
import com.belanjaki.id.common.constant.ReturnCode;
import com.belanjaki.id.common.constant.RoleEnum;
import com.belanjaki.id.common.dto.BaseResponse;
import com.belanjaki.id.common.dto.Meta;
import com.belanjaki.id.merchant.constant.MerchantStatus;
import com.belanjaki.id.merchant.dto.request.RequestCreateMerchantDTO;
import com.belanjaki.id.merchant.dto.response.ResponseCreateMerchantDTO;
import com.belanjaki.id.merchant.model.MstMerchant;
import com.belanjaki.id.merchant.repository.MstMerchantRepository;
import com.belanjaki.id.merchant.validator.MerchantValidator;
import com.belanjaki.id.usersmanagement.model.MstRole;
import com.belanjaki.id.usersmanagement.repository.MstRoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.lang.module.ResolutionException;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class MstMerchantService {

    private final BCryptPasswordEncoder passwordEncoder;
    private final MstMerchantRepository mstMerchantRepository;
    private final MstRoleRepository mstRoleRepository;
    private final MerchantValidator merchantValidator;
    private final ResourceLabel resourceLabel;

    public Object createMerchant(RequestCreateMerchantDTO dto){
        merchantValidator.validateMerchantIfHasBeenRegister(dto);
        MstRole mstRole = mstRoleRepository.findByRoleName(RoleEnum.USER.getRoleName()).orElseThrow(() -> new ResolutionException(resourceLabel.getBodyLabel("role.find.not.found")));

        MstMerchant mstMerchant = createObjectMerchant(dto, mstRole);
        mstMerchantRepository.save(mstMerchant);

        ResponseCreateMerchantDTO responseCreateMerchantDTO = createObjectResponseMerchantCreate(mstMerchant);
        BaseResponse<ResponseCreateMerchantDTO> baseResponse = new BaseResponse<>(responseCreateMerchantDTO, new Meta(ReturnCode.SUCCESSFULLY_REGISTER.getStatusCode(), ReturnCode.SUCCESSFULLY_REGISTER.getMessage()));
        return baseResponse.getCustomizeResponse("merchant_create");
    }


    private ResponseCreateMerchantDTO createObjectResponseMerchantCreate(MstMerchant mstMerchant){
        return ResponseCreateMerchantDTO.builder()
                .merchantName(mstMerchant.getMerchantName())
                .email(mstMerchant.getEmail())
                .build();
    }

    private MstMerchant createObjectMerchant(RequestCreateMerchantDTO dto, MstRole mstRole){
        MstMerchant mstMerchant = MstMerchant.builder()
                .merchantId(UUID.randomUUID())
                .mstRole(mstRole)
                .merchantName(dto.getMerchantName())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .numberPhone(dto.getNumberPhone())
                .status(MerchantStatus.FOR_APPROVAL.getMerchantStatus())
                .desc(dto.getDesc())
                .build();
        mstMerchant.setCreatedBy(dto.getMerchantName());
        mstMerchant.setUpdatedBy(dto.getMerchantName());
        return mstMerchant;
    }
}
