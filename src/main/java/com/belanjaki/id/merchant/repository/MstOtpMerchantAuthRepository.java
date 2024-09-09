package com.belanjaki.id.merchant.repository;

import com.belanjaki.id.merchant.model.MstMerchant;
import com.belanjaki.id.merchant.model.MstOtpMerchantAuth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.UUID;

@Repository
public interface MstOtpMerchantAuthRepository extends JpaRepository<MstOtpMerchantAuth, UUID> {

    MstOtpMerchantAuth findByMstMerchant(MstMerchant mstMerchant);

    @Modifying
    @Query(value = "update MstOtpMerchantAuth m set m.secretOtp = :secretOtp, m.updatedDate = :updateDate where m.otpAuthId = :otpMerchantAuthId")
    void updateOtpSecretKey(@Param("secretOtp") String secretOtp, @Param("updateDate") Date updatedDate, @Param("otpMerchantAuthId") UUID otpMerchantAuthId);
}
