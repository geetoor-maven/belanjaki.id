package com.belanjaki.id.administrator.service;

import com.belanjaki.id.administrator.dto.request.RequestLoginAdminDTO;
import com.belanjaki.id.administrator.dto.request.RequestValidationOTPAdminDTO;
import com.belanjaki.id.administrator.dto.response.ResponseLoginWithOTPAdminDTO;
import com.belanjaki.id.administrator.model.MstAdministrator;
import com.belanjaki.id.administrator.model.MstOtpAdminAuth;
import com.belanjaki.id.administrator.repository.MstOtpAdminAuthRepository;
import com.belanjaki.id.administrator.validator.AdminValidator;
import com.belanjaki.id.common.ResourceLabel;
import com.belanjaki.id.common.constant.ReturnCode;
import com.belanjaki.id.common.constant.RoleEnum;
import com.belanjaki.id.common.dto.BaseResponse;
import com.belanjaki.id.common.dto.Meta;
import com.belanjaki.id.common.dto.ResponseMessageAndEmailDTO;
import com.belanjaki.id.common.util.OtpUtils;
import com.belanjaki.id.common.util.RoleIdGetUtils;
import com.belanjaki.id.jwt.JWTUtils;
import com.belanjaki.id.usersmanagement.service.AuthService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthAdministratorService {

    private final MstOtpAdminAuthRepository mstOtpAdminAuthRepository;
    private final AuthService authService;
    private final AdminValidator adminValidator;
    private final ResourceLabel resourceLabel;
    private final PasswordEncoder passwordEncoder;
    private final JWTUtils jwtUtils;
    private final RoleIdGetUtils roleIdGetUtils;


    @Transactional
    public Object loginAdministrator(RequestLoginAdminDTO dto){
        MstAdministrator mstAdministrator = adminValidator.validateLoginAdmin(dto);
        handleOtpAdminAuth(mstAdministrator);
        ResponseMessageAndEmailDTO responseMessageAndEmailDTO = createObjectMessageAndEmailDTO(mstAdministrator);
        BaseResponse<ResponseMessageAndEmailDTO> baseResponse = new BaseResponse<>(responseMessageAndEmailDTO, new Meta(ReturnCode.SUCCESSFULLY_OTP_SEND.getStatusCode(), ReturnCode.SUCCESSFULLY_OTP_SEND.getMessage()));
        return baseResponse.getCustomizeResponse("otp_send");
    }

    @Transactional
    public Object validateOtpAfterLoginAdmin(RequestValidationOTPAdminDTO dto){
        adminValidator.validateEmailAndNumberAdminWithOtp(dto);

        final UserDetails userDetails = authService.loadUserByUsernameRole(dto.getEmail(), RoleEnum.ADMIN.getRoleName());
        final String jwt = jwtUtils.generateToken(userDetails, roleIdGetUtils.getRoleID(RoleEnum.ADMIN.getRoleName()));

        ResponseLoginWithOTPAdminDTO responseLoginWithOTPAdminDTO = createObjectResponseLoginWithOTPAdmin(jwt, dto.getEmail());
        BaseResponse<ResponseLoginWithOTPAdminDTO> baseResponse = new BaseResponse<>(responseLoginWithOTPAdminDTO, new Meta(ReturnCode.SUCCESSFULLY_LOGIN.getStatusCode(), ReturnCode.SUCCESSFULLY_LOGIN.getMessage()));
        return baseResponse.getCustomizeResponse("login_admin");
    }


    private void handleOtpAdminAuth(MstAdministrator mstAdministrator){
        MstOtpAdminAuth mstOtpAdminAuth = mstOtpAdminAuthRepository.findByMstAdministrator(mstAdministrator);
        if (mstOtpAdminAuth == null){
            mstOtpAdminAuthRepository.save(createObjectMstOtpAdminAuth(mstAdministrator));
        }else {
            String otpBeforeHash = OtpUtils.generateOtp();
            log.info("Generated OTP: {}", otpBeforeHash);
            String secretOtp = passwordEncoder.encode(otpBeforeHash);
            mstOtpAdminAuthRepository.updateOtpSecretKey(secretOtp, new Date(), mstOtpAdminAuth.getOtpAuthId());
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

    private ResponseLoginWithOTPAdminDTO createObjectResponseLoginWithOTPAdmin(String jwt, String email){
        return ResponseLoginWithOTPAdminDTO.builder()
                .email(email)
                .token(jwt)
                .build();
    }
}
