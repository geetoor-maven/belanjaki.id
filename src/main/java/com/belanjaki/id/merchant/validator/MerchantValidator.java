package com.belanjaki.id.merchant.validator;


import com.belanjaki.id.common.ResourceLabel;
import com.belanjaki.id.common.exception.ItemAlreadyExistException;
import com.belanjaki.id.common.exception.NeedApprovalException;
import com.belanjaki.id.common.exception.ResourceNotFoundException;
import com.belanjaki.id.common.util.OtpUtils;
import com.belanjaki.id.merchant.constant.MerchantStatus;
import com.belanjaki.id.merchant.dto.request.RequestCreateMerchantDTO;
import com.belanjaki.id.merchant.dto.request.RequestMerchantLoginDTO;
import com.belanjaki.id.merchant.dto.request.RequestValidateOtpMerchantDTO;
import com.belanjaki.id.merchant.model.MstMerchant;
import com.belanjaki.id.merchant.model.MstOtpMerchantAuth;
import com.belanjaki.id.merchant.repository.MstMerchantRepository;
import com.belanjaki.id.merchant.repository.MstOtpMerchantAuthRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@AllArgsConstructor
public class MerchantValidator {

    private final MstMerchantRepository mstMerchantRepository;
    private final MstOtpMerchantAuthRepository mstOtpMerchantAuthRepository;
    private final ResourceLabel resourceLabel;
    private final PasswordEncoder passwordEncoder;


    public void validateOtpMerchant(RequestValidateOtpMerchantDTO dto){
        MstMerchant mstMerchant = mstMerchantRepository.findByEmail(dto.getEmail()).orElseThrow(
                () -> new ResourceNotFoundException(resourceLabel.getBodyLabel("merchant.find.email.not.found")));
        MstOtpMerchantAuth auth = mstOtpMerchantAuthRepository.findByMstMerchant(mstMerchant);
        if (auth != null) {
            String otpSecretKey = auth.getSecretOtp();
            boolean otpMatch = passwordEncoder.matches(dto.getOtp(), otpSecretKey);
            if (!otpMatch || OtpUtils.isMoreThanOneMinute(auth.getUpdatedDate())){
                throw new ResourceNotFoundException(resourceLabel.getBodyLabel("admin.otp.validation.not.valid"));
            }
        }else {
            throw new ResourceNotFoundException(resourceLabel.getBodyLabel("admin.otp.validation.not.valid"));
        }
    }

    public MstMerchant getMerchantWithId(String merchantId) {
        return mstMerchantRepository.findById(UUID.fromString(merchantId)).orElseThrow(() ->
                new ResourceNotFoundException(resourceLabel.getBodyLabel("merchant.not.found")));
    }

    public MstMerchant getMerchantWithValidatorLoginAndNoNeedForApp(RequestMerchantLoginDTO dto) {
        MstMerchant mstMerchant = mstMerchantRepository.findByEmail(dto.getEmail()).orElseThrow(() ->
                new ResourceNotFoundException(resourceLabel.getBodyLabel("merchant.find.email.not.found")));
        if (mstMerchant.getStatus().equalsIgnoreCase(MerchantStatus.FOR_APPROVAL.getCode())){
            throw new NeedApprovalException(resourceLabel.getBodyLabel("merchant.is.not.yet.approved"));
        }
        boolean isPasswordMatch = passwordEncoder.matches(dto.getPassword(), mstMerchant.getPassword());
        if (!isPasswordMatch){
            throw new ResourceNotFoundException(resourceLabel.getBodyLabel("user.password.validation.not.valid"));
        }
        return mstMerchant;
    }

    public void validateMerchantIfHasBeenRegister(RequestCreateMerchantDTO dto){
        mstMerchantRepository.findByEmail(dto.getEmail()).ifPresent(user -> {
            throw new ItemAlreadyExistException(resourceLabel.getBodyLabel("merchant.create.duplicate.email"));
        });
    }

    public List<MstMerchant> getMerchantList(String status){
        List<MstMerchant> mstMerchants;

        if (status == null || status.isEmpty()){
            mstMerchants = mstMerchantRepository.findAll();
        }else {
            mstMerchants = mstMerchantRepository.findByStatus(status);
        }
        return mstMerchants;
    }

}
