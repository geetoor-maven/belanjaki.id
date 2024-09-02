package com.belanjaki.id.administrator.repository;

import com.belanjaki.id.administrator.model.MstAdministrator;
import com.belanjaki.id.administrator.model.MstOtpAdminAuth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.UUID;

@Repository
public interface MstOtpAdminAuthRepository extends JpaRepository<MstOtpAdminAuth, UUID> {

    MstOtpAdminAuth findByMstAdministrator(MstAdministrator mstAdministrator);

    @Modifying
    @Query(value = "update MstOtpAdminAuth m set m.secretOtp = :secretOtp, m.updatedDate = :updateDate where m.otpAuthId = :otpAuthId")
    void updateOtpSecretKey(@Param("secretOtp") String secretOtp, @Param("updateDate") Date updatedDate, @Param("otpAuthId") UUID otpAuthId);

}
