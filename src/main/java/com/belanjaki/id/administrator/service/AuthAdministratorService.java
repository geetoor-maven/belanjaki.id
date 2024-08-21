package com.belanjaki.id.administrator.service;

import com.belanjaki.id.administrator.dto.request.RequestLoginAdminDTO;
import com.belanjaki.id.administrator.model.MstAdministrator;
import com.belanjaki.id.administrator.model.MstOtpAdminAuth;
import com.belanjaki.id.administrator.repository.MstAdministratorRepository;
import com.belanjaki.id.administrator.repository.MstOtpAdminAuthRepository;
import com.belanjaki.id.administrator.validator.AdminValidator;
import com.belanjaki.id.common.ResourceLabel;
import com.belanjaki.id.common.constant.ReturnCode;
import com.belanjaki.id.common.dto.BaseResponse;
import com.belanjaki.id.common.dto.Meta;
import com.belanjaki.id.common.dto.ResponseMessageAndEmailDTO;
import com.belanjaki.id.common.util.OtpUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthAdministratorService {

    private final MstAdministratorRepository mstAdministratorRepository;
    private final MstOtpAdminAuthRepository mstOtpAdminAuthRepository;
    private final AdminValidator adminValidator;
    private final ResourceLabel resourceLabel;
    private final PasswordEncoder passwordEncoder;


    @Transactional
    public Object loginAdministrator(RequestLoginAdminDTO dto){
        MstAdministrator mstAdministrator = adminValidator.validateLoginAdmin(dto);
        handleOtpAdminAuth(mstAdministrator);
        ResponseMessageAndEmailDTO responseMessageAndEmailDTO = createObjectMessageAndEmailDTO(mstAdministrator);
        BaseResponse<ResponseMessageAndEmailDTO> baseResponse = new BaseResponse<>(responseMessageAndEmailDTO, new Meta(ReturnCode.SUCCESSFULLY_OTP_SEND.getStatusCode(), ReturnCode.SUCCESSFULLY_OTP_SEND.getMessage()));
        return baseResponse.getCustomizeResponse("otp_send");
    }



    private void handleOtpAdminAuth(MstAdministrator mstAdministrator){
        MstOtpAdminAuth mstOtpAdminAuth = mstOtpAdminAuthRepository.findByMstAdministrator(mstAdministrator);
        if (mstOtpAdminAuth == null){
            mstOtpAdminAuthRepository.save(createObjectMstOtpAdminAuth(mstAdministrator));
        }else {

        }
    }

    private MstOtpAdminAuth createObjectMstOtpAdminAuth(MstAdministrator mstAdministrator){
        String otpBeforeHash = OtpUtils.generateOtp();
        log.info("otp admin {} = ", otpBeforeHash);
        MstOtpAdminAuth otpAdminAuth = MstOtpAdminAuth.builder()
                .otpAuthId(UUID.randomUUID())
                .mstAdministrator(mstAdministrator)
                .secretOtp(passwordEncoder.encode(otpBeforeHash))
                .build();
        otpAdminAuth.setCreatedBy(mstAdministrator.getAdminName());
        otpAdminAuth.setUpdatedBy(mstAdministrator.getAdminName());
        return otpAdminAuth;
    }

    private ResponseMessageAndEmailDTO createObjectMessageAndEmailDTO(MstAdministrator mstAdministrator){
        return ResponseMessageAndEmailDTO.builder()
                .message(resourceLabel.getBodyLabel("admin.login.send.otp.success"))
                .email(mstAdministrator.getEmail())
                .build();
    }
}
