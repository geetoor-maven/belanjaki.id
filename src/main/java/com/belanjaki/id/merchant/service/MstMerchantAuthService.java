package com.belanjaki.id.merchant.service;

import com.belanjaki.id.common.ResourceLabel;
import com.belanjaki.id.common.constant.ReturnCode;
import com.belanjaki.id.common.constant.RoleEnum;
import com.belanjaki.id.common.dto.BaseResponse;
import com.belanjaki.id.common.dto.Meta;
import com.belanjaki.id.common.util.OtpUtils;
import com.belanjaki.id.common.util.RoleIdGetUtils;
import com.belanjaki.id.jwt.JWTUtils;
import com.belanjaki.id.merchant.dto.request.RequestMerchantLoginDTO;
import com.belanjaki.id.merchant.dto.request.RequestValidateOtpMerchantDTO;
import com.belanjaki.id.merchant.dto.response.ResponseLoginMerchantDTO;
import com.belanjaki.id.merchant.model.MstMerchant;
import com.belanjaki.id.merchant.model.MstOtpMerchantAuth;
import com.belanjaki.id.merchant.repository.MstMerchantRepository;
import com.belanjaki.id.merchant.repository.MstOtpMerchantAuthRepository;
import com.belanjaki.id.merchant.validator.MerchantValidator;
import com.belanjaki.id.usersmanagement.dto.user.response.ResponseLoginWithOtpDTO;
import com.belanjaki.id.usersmanagement.service.MstUserAuthService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class MstMerchantAuthService {

    private final MstUserAuthService authService;
    private final MstOtpMerchantAuthRepository mstOtpMerchantAuthRepository;
    private final MerchantValidator merchantValidator;
    private final PasswordEncoder passwordEncoder;
    private final ResourceLabel resourceLabel;
    private final JWTUtils jwtUtils;
    private final RoleIdGetUtils roleIdGetUtils;

    @Transactional
    public Object loginMerchant(RequestMerchantLoginDTO dto){
        MstMerchant mstMerchant = merchantValidator.getMerchantWithValidatorLoginAndNoNeedForApp(dto);
        handleOtpMerchant(mstMerchant);
        ResponseLoginMerchantDTO responseLoginMerchantDTO = createObjectResponseLoginMerchant(mstMerchant.getEmail());
        BaseResponse<ResponseLoginMerchantDTO> baseResponse = new BaseResponse<>(responseLoginMerchantDTO, new Meta(ReturnCode.SUCCESSFULLY_OTP_SEND.getStatusCode(), ReturnCode.SUCCESSFULLY_OTP_SEND.getMessage()));
        return baseResponse.getCustomizeResponse("otp_sent");
    }


    public Object validateOtpMerchant(RequestValidateOtpMerchantDTO dto){
        merchantValidator.validateOtpMerchant(dto);
        final UserDetails userDetails = authService.loadUserByUsernameRole(dto.getEmail(), RoleEnum.MERCHANT.getRoleName());
        final String jwt = jwtUtils.generateToken(userDetails, roleIdGetUtils.getRoleID(RoleEnum.MERCHANT.getRoleName()));
        ResponseLoginWithOtpDTO responseLoginWithOtpDTO = createObjectResponseLoginWithOtp(jwt, dto.getEmail());
        BaseResponse<ResponseLoginWithOtpDTO> baseResponse = new BaseResponse<>(responseLoginWithOtpDTO, new Meta(ReturnCode.SUCCESSFULLY_LOGIN.getStatusCode(), ReturnCode.SUCCESSFULLY_LOGIN.getMessage()));
        return baseResponse.getCustomizeResponse("login");
    }

    public void handleOtpMerchant(MstMerchant mstMerchant){
        MstOtpMerchantAuth auth = mstOtpMerchantAuthRepository.findByMstMerchant(mstMerchant);
        if (auth == null){
            mstOtpMerchantAuthRepository.save(createObjectMstOtpMerchantAuth(mstMerchant));
        }else {
            String otpBeforeHash = OtpUtils.generateOtp();
            log.info("otp{} = ", otpBeforeHash);
            String secretOtp = passwordEncoder.encode(otpBeforeHash);
            mstOtpMerchantAuthRepository.updateOtpSecretKey(secretOtp, new Date(), auth.getOtpAuthId());
        }
    }

    private MstOtpMerchantAuth createObjectMstOtpMerchantAuth(MstMerchant mstMerchant){
        String otpBeforeHash = OtpUtils.generateOtp();
        log.info("otp{} = ", otpBeforeHash);
        MstOtpMerchantAuth auth = MstOtpMerchantAuth.builder()
                .otpAuthId(UUID.randomUUID())
                .mstMerchant(mstMerchant)
                .secretOtp(passwordEncoder.encode(otpBeforeHash))
                .build();
        auth.setCreatedBy(mstMerchant.getMerchantName());
        auth.setUpdatedBy(mstMerchant.getMerchantName());
        return auth;
    }

    private ResponseLoginMerchantDTO createObjectResponseLoginMerchant(String email){
        return ResponseLoginMerchantDTO.builder()
                .email(email)
                .message(resourceLabel.getBodyLabel("user.login.send.otp.success"))
                .build();
    }

    private ResponseLoginWithOtpDTO createObjectResponseLoginWithOtp(String jwt, String email){
        return ResponseLoginWithOtpDTO.builder()
                .email(email)
                .token(jwt)
                .build();
    }
}
