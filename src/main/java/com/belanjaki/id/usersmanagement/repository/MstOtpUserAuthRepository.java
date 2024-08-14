package com.belanjaki.id.usersmanagement.repository;

import com.belanjaki.id.usersmanagement.model.MstOtpUserAuth;
import com.belanjaki.id.usersmanagement.model.MstUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.UUID;

@Repository
public interface MstOtpUserAuthRepository extends JpaRepository<MstOtpUserAuth, UUID> {

    MstOtpUserAuth findByMstUser(MstUser mstUser);

    @Modifying
    @Query(value = "update MstOtpUserAuth m set m.secretOtp = :secretOtp, m.updatedDate = :updateDate where m.otpAuthId = :otpUserAuthId")
    void updateOtpSecretKey(@Param("secretOtp") String secretOtp, @Param("updateDate") Date updatedDate, @Param("otpUserAuthId") UUID otpUserAuthId);
}
