package com.belanjaki.id.merchant.validator;


import com.belanjaki.id.common.ResourceLabel;
import com.belanjaki.id.common.exception.ItemAlreadyExistException;
import com.belanjaki.id.merchant.dto.request.RequestCreateMerchantDTO;
import com.belanjaki.id.merchant.model.MstMerchant;
import com.belanjaki.id.merchant.repository.MstMerchantRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class MerchantValidator {

    private final MstMerchantRepository mstMerchantRepository;
    private final ResourceLabel resourceLabel;


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
