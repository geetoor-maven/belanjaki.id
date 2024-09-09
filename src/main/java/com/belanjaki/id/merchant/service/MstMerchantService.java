package com.belanjaki.id.merchant.service;

import com.belanjaki.id.common.ResourceLabel;
import com.belanjaki.id.common.constant.BasePath;
import com.belanjaki.id.common.constant.ReturnCode;
import com.belanjaki.id.common.constant.RoleEnum;
import com.belanjaki.id.common.dto.BaseResponse;
import com.belanjaki.id.common.dto.Meta;
import com.belanjaki.id.common.dto.PaginationResponse;
import com.belanjaki.id.merchant.constant.MerchantStatus;
import com.belanjaki.id.merchant.dto.request.RequestCreateMerchantDTO;
import com.belanjaki.id.merchant.dto.response.ResponseCreateMerchantDTO;
import com.belanjaki.id.merchant.dto.response.ResponseMerchantListDTO;
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
import java.util.List;
import java.util.Optional;
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
        MstRole mstRole = mstRoleRepository.findByRoleName(RoleEnum.MERCHANT.getRoleName()).orElseThrow(() -> new ResolutionException(resourceLabel.getBodyLabel("role.find.not.found")));

        MstMerchant mstMerchant = createObjectMerchant(dto, mstRole);
        mstMerchantRepository.save(mstMerchant);

        ResponseCreateMerchantDTO responseCreateMerchantDTO = createObjectResponseMerchantCreate(mstMerchant);
        BaseResponse<ResponseCreateMerchantDTO> baseResponse = new BaseResponse<>(responseCreateMerchantDTO, new Meta(ReturnCode.SUCCESSFULLY_REGISTER.getStatusCode(), ReturnCode.SUCCESSFULLY_REGISTER.getMessage()));
        return baseResponse.getCustomizeResponse("merchant_create");
    }


    public Object getListMerchantsByStatus(String status, int page, int size){

        List<MstMerchant> mstMerchants = merchantValidator.getMerchantList(status);
        List<ResponseMerchantListDTO> responseMerchantListDTOS =
                mstMerchants.stream()
                        .map(this::convertToDTO)
                        .toList();

        int totalItems = responseMerchantListDTOS.size();
        int totalPages = (int) Math.ceil((double) totalItems / size);
        int fromIndex = (page - 1) * size;
        int toIndex = Math.min(fromIndex + size, totalItems);

        List<ResponseMerchantListDTO> merchantListDTOS = responseMerchantListDTOS.subList(fromIndex, toIndex);

        String statusQueryParam = Optional.ofNullable(status)
                .filter(s -> !s.isEmpty())
                .map(s -> "&status=" + s)
                .orElse("");
        String nextPageUrl = (page < totalPages)
                ? String.format("%s/list?page=%d%s", BasePath.MERCHANT, page + 1, statusQueryParam)
                : null;
        String prevPageUrl = (page > 1)
                ? String.format("%s/list?page=%d%s", BasePath.MERCHANT, page - 1, statusQueryParam)
                : null;

        PaginationResponse<ResponseMerchantListDTO> paginationResponse = new PaginationResponse<ResponseMerchantListDTO>(merchantListDTOS, totalItems, size, page, totalPages, nextPageUrl, prevPageUrl);
        BaseResponse<PaginationResponse<ResponseMerchantListDTO>> baseResponse = new BaseResponse<>(paginationResponse, new Meta(ReturnCode.SUCCESSFULLY_GET_DATA.getStatusCode(), ReturnCode.SUCCESSFULLY_GET_DATA.getMessage()));
        return baseResponse.getCustomizeResponse("merchants");
    }

    public Object getMerchant(UUID merchantID){
        return new Object();
    }

    private ResponseMerchantListDTO convertToDTO(MstMerchant mstMerchant){
        return new ResponseMerchantListDTO(
                mstMerchant.getMerchantName(),
                mstMerchant.getEmail(),
                mstMerchant.getNumberPhone(),
                MerchantStatus.getMerchantStatus(mstMerchant.getStatus())
        );
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
                .status(MerchantStatus.FOR_APPROVAL.getCode())
                .desc(dto.getDesc())
                .build();
        mstMerchant.setCreatedBy(dto.getMerchantName());
        mstMerchant.setUpdatedBy(dto.getMerchantName());
        return mstMerchant;
    }
}
