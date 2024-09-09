package com.belanjaki.id.merchant.validator;


import com.belanjaki.id.common.ResourceLabel;
import com.belanjaki.id.common.exception.ItemAlreadyExistException;
import com.belanjaki.id.common.exception.ResourceNotFoundException;
import com.belanjaki.id.merchant.dto.request.RequestCreateMerchantDTO;
import com.belanjaki.id.merchant.dto.request.RequestMerchantLoginDTO;
import com.belanjaki.id.merchant.model.MstMerchant;
import com.belanjaki.id.merchant.repository.MstMerchantRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class MerchantValidator {

    private final MstMerchantRepository mstMerchantRepository;
    private final ResourceLabel resourceLabel;
    private final PasswordEncoder passwordEncoder;

    public MstMerchant getMerchantWithValidatorLogin(RequestMerchantLoginDTO dto) {
        MstMerchant mstMerchant = mstMerchantRepository.findByEmail(dto.getEmail()).orElseThrow(() ->
                new ResourceNotFoundException(resourceLabel.getBodyLabel("merchant.find.email.not.found")));
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
