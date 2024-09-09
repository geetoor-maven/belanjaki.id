package com.belanjaki.id.merchant.service;

import com.belanjaki.id.common.ResourceLabel;
import com.belanjaki.id.common.constant.ReturnCode;
import com.belanjaki.id.common.dto.BaseResponse;
import com.belanjaki.id.common.dto.Meta;
import com.belanjaki.id.common.util.OtpUtils;
import com.belanjaki.id.merchant.dto.request.RequestMerchantLoginDTO;
import com.belanjaki.id.merchant.dto.response.ResponseLoginMerchantDTO;
import com.belanjaki.id.merchant.model.MstMerchant;
import com.belanjaki.id.merchant.model.MstOtpMerchantAuth;
import com.belanjaki.id.merchant.repository.MstMerchantRepository;
import com.belanjaki.id.merchant.repository.MstOtpMerchantAuthRepository;
import com.belanjaki.id.merchant.validator.MerchantValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class MstMerchantAuthService {

    @Autowired
    private MstMerchantRepository mstMerchantRepository;
    @Autowired
    private MstOtpMerchantAuthRepository mstOtpMerchantAuthRepository;
    @Autowired
    private MerchantValidator merchantValidator;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ResourceLabel resourceLabel;

    @Transactional
    public Object loginMerchant(RequestMerchantLoginDTO dto){
        MstMerchant mstMerchant = merchantValidator.getMerchantWithValidatorLogin(dto);
        handleOtpMerchant(mstMerchant);
        ResponseLoginMerchantDTO responseLoginMerchantDTO = createObjectResponseLoginMerchant(mstMerchant.getEmail());
        BaseResponse<ResponseLoginMerchantDTO> baseResponse = new BaseResponse<>(responseLoginMerchantDTO, new Meta(ReturnCode.SUCCESSFULLY_OTP_SEND.getStatusCode(), ReturnCode.SUCCESSFULLY_OTP_SEND.getMessage()));
        return baseResponse.getCustomizeResponse("otp_sent");
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
}
