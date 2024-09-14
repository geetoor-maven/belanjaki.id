package com.belanjaki.id.usersmanagement.service;

import ch.qos.logback.core.util.StringUtil;
import com.belanjaki.id.common.ResourceApp;
import com.belanjaki.id.common.ResourceLabel;
import com.belanjaki.id.common.constant.ReturnCode;
import com.belanjaki.id.common.dto.BaseResponse;
import com.belanjaki.id.common.dto.Meta;
import com.belanjaki.id.common.dto.ResponseDTO;
import com.belanjaki.id.usersmanagement.dto.user.request.RequestUpdateUserAddressDTO;
import com.belanjaki.id.usersmanagement.dto.user.response.ResponseUserAddressDTO;
import com.belanjaki.id.usersmanagement.dto.user.response.ResponseUserDTO;
import com.belanjaki.id.usersmanagement.model.MstUser;
import com.belanjaki.id.usersmanagement.model.MstUserAddress;
import com.belanjaki.id.usersmanagement.repository.MstUserAddressRepository;
import com.belanjaki.id.usersmanagement.repository.MstUserRepository;
import com.belanjaki.id.usersmanagement.validator.UserValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class MstUserService {

    private final MstUserRepository mstUserRepository;
    private final MstUserAuthService authService;
    private final MstUserAddressRepository mstUserAddressRepository;
    private final ResourceLabel resourceLabel;
    private final ResourceApp resourceApp;
    private final UserValidator userValidator;

    public Object getUserInfo(){
        MstUser userLogin = authService.getUserLogin();
        MstUserAddress mstUserAddress = mstUserAddressRepository.findByMstUser(userLogin);
        ResponseUserAddressDTO responseDTO = createResponseObjectUserAddress(mstUserAddress);
        ResponseUserDTO responseUserDTO = createResponseObjectUser(userLogin, responseDTO);
        BaseResponse<ResponseUserDTO> baseResponse = new BaseResponse<>(responseUserDTO, new Meta(ReturnCode.SUCCESSFULLY_GET_DATA.getStatusCode(), ReturnCode.SUCCESSFULLY_GET_DATA.getMessage()));
        return baseResponse.getCustomizeResponse("user");
    }

    @Transactional
    public Object updatePhotoUser(MultipartFile file) {
        MstUser userLogin = authService.getUserLogin();
        userValidator.validateFileUploadPhotoUser(file);

        String path = resourceApp.getValue("path.user.photo");
        String pathFile = path + userLogin.getUserId();
        Path thePath = Paths.get(pathFile);
        String fileName = userLogin.getUserId() + "_" + StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

        try {
            // Buat direktori jika belum ada
            if (Files.notExists(thePath)) Files.createDirectory(thePath);

            // Hapus file lama
            Files.newDirectoryStream(thePath, userLogin.getUserId() + "_*").forEach(entry -> {
                try {
                    Files.deleteIfExists(entry);
                    log.info("File lama {} dihapus.", entry.getFileName());
                } catch (IOException e) {
                    log.error("Gagal menghapus file lama: {}", e.getMessage());
                }
            });

            // Simpan file baru
            Files.copy(file.getInputStream(), thePath.resolve(fileName));
        } catch (IOException e) {
            throw new RuntimeException("Gagal memproses file: " + e.getMessage(), e);
        }

        userLogin.setImgProfile(fileName);
        userLogin.setUrlProfile(thePath + "/" + fileName);

        mstUserRepository.save(userLogin);

        ResponseDTO responseDTO = ResponseDTO.builder()
                .message(resourceLabel.getBodyLabel("msg.success.save"))
                .build();

        BaseResponse<ResponseDTO> baseResponse = new BaseResponse<>(responseDTO, new Meta(ReturnCode.SUCCESSFULLY_UPDATE_PHOTO.getStatusCode(), ReturnCode.SUCCESSFULLY_UPDATE_PHOTO.getMessage()));
        return baseResponse.getCustomizeResponse("update_success");
    }


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
        ResponseUserAddressDTO responseDTO = createResponseObjectUserAddress(mstUserAddress);
        BaseResponse<ResponseUserAddressDTO> baseResponse = new BaseResponse<>(responseDTO, new Meta(ReturnCode.SUCCESSFULLY_GET_DATA.getStatusCode(), ReturnCode.SUCCESSFULLY_GET_DATA.getMessage()));
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

    private ResponseUserDTO createResponseObjectUser(MstUser user, ResponseUserAddressDTO dto){
        return  ResponseUserDTO.builder()
                .name(user.getName())
                .email(user.getEmail())
                .numberPhone(user.getNumberPhone())
                .responseUserAddressDTO(dto)
                .build();
    }
    private ResponseUserAddressDTO createResponseObjectUserAddress(MstUserAddress userAddress){
        return ResponseUserAddressDTO.builder()
                .street(userAddress.getStreet())
                .city(userAddress.getCity())
                .state(userAddress.getState())
                .postalCode(userAddress.getPostalCode())
                .build();
    }
}
