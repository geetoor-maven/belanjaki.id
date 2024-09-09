package com.belanjaki.id.merchant.repository;

import com.belanjaki.id.merchant.model.MstMerchant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MstMerchantRepository extends JpaRepository<MstMerchant, UUID> {

    Optional<MstMerchant> findByEmail(String email);

    List<MstMerchant> findByStatus(String status);

    @Modifying
    @Query(value = "update MstMerchant m set m.status = :status, " +
            "m.updatedDate = :updateDate, m.updatedBy = :updateBy " +
            "where m.merchantId = :merchantId")
    void updateStatusMerchant(@Param("merchantId") UUID merchantId,
                              @Param("status") String status,
                              @Param("updateDate") Date updatedDate,
                              @Param("updateBy") String updateBy);
}
