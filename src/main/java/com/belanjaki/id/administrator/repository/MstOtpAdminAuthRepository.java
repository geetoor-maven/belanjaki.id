package com.belanjaki.id.administrator.repository;

import com.belanjaki.id.administrator.model.MstAdministrator;
import com.belanjaki.id.administrator.model.MstOtpAdminAuth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MstOtpAdminAuthRepository extends JpaRepository<MstOtpAdminAuth, UUID> {

    MstOtpAdminAuth findByMstAdministrator(MstAdministrator mstAdministrator);

}
