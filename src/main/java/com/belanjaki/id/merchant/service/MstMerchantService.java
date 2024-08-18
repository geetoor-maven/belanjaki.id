package com.belanjaki.id.merchant.service;

import com.belanjaki.id.merchant.repository.MstMerchantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MstMerchantService {

    private final MstMerchantRepository mstMerchantRepository;

    public Object createMerchant(){

        return new Object();
    }
}
