package com.belanjaki.id.usersmanagement.service;

import com.belanjaki.id.common.ResourceLabel;
import com.belanjaki.id.common.constant.ReturnCode;
import com.belanjaki.id.common.dto.BaseResponse;
import com.belanjaki.id.common.dto.Meta;
import com.belanjaki.id.common.dto.ResponseDTO;
import com.belanjaki.id.usersmanagement.dto.user.request.RequestUpdateUserAddressDTO;
import com.belanjaki.id.usersmanagement.dto.user.response.ResponseUpdateUserAddressDTO;
import com.belanjaki.id.usersmanagement.model.MstUser;
import com.belanjaki.id.usersmanagement.model.MstUserAddress;
import com.belanjaki.id.usersmanagement.repository.MstUserAddressRepository;
import com.belanjaki.id.usersmanagement.repository.MstUserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class MstUserService {

    private final MstUserRepository mstUserRepository;
    private final AuthService authService;
    private final MstUserAddressRepository mstUserAddressRepository;
    private final ResourceLabel resourceLabel;

    @Transactional
    public Object updateUserAddress(RequestUpdateUserAddressDTO dto){

        MstUser userLogin = authService.getUserLogin();
        MstUserAddress mstUserAddress = mstUserAddressRepository.findByMstUser(userLogin);
        if (mstUserAddress == null){
            mstUserAddressRepository.save(createObjectUserAddress(dto, userLogin));
        }else {
            mstUserAddressRepository.updateUserAddress(dto);
        }
        ResponseDTO responseDTO = ResponseDTO.builder()
                .message(resourceLabel.getBodyLabel("msg.success.save"))
                .build();

        BaseResponse<ResponseDTO> baseResponse = new BaseResponse<>(responseDTO, new Meta(ReturnCode.SUCCESSFULLY_UPDATE_ADDRESS.getStatusCode(), ReturnCode.SUCCESSFULLY_UPDATE_ADDRESS.getMessage()));
        return baseResponse.getCustomizeResponse("update_success");
    }

    public Object findUserAddress(){
        MstUser userLogin = authService.getUserLogin();
        MstUserAddress mstUserAddress = mstUserAddressRepository.findByMstUser(userLogin);
        ResponseUpdateUserAddressDTO responseDTO = createResponseObjectUserAddress(mstUserAddress);
        BaseResponse<ResponseUpdateUserAddressDTO> baseResponse = new BaseResponse<>(responseDTO, new Meta(ReturnCode.SUCCESSFULLY_GET_DATA.getStatusCode(), ReturnCode.SUCCESSFULLY_GET_DATA.getMessage()));
        return baseResponse.getCustomizeResponse("user_address");
    }

    private MstUserAddress createObjectUserAddress(RequestUpdateUserAddressDTO dto, MstUser mstUser){
        MstUserAddress mstUserAddress = MstUserAddress.builder()
                .addressId(UUID.randomUUID())
                .mstUser(mstUser)
                .street(dto.getStreet())
                .city(dto.getCity())
                .state(dto.getState())
                .postalCode(dto.getPostalCode())
                .build();
        mstUserAddress.setCreatedBy(mstUser.getName());
        mstUserAddress.setUpdatedBy(mstUser.getName());
        return mstUserAddress;
    }

    private ResponseUpdateUserAddressDTO createResponseObjectUserAddress(MstUserAddress userAddress){
        return ResponseUpdateUserAddressDTO.builder()
                .street(userAddress.getStreet())
                .city(userAddress.getCity())
                .state(userAddress.getState())
                .postalCode(userAddress.getPostalCode())
                .build();
    }
}
