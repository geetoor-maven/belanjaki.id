package com.belanjaki.id.merchant.repository;

import com.belanjaki.id.merchant.model.MstMerchant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MstMerchantRepository extends JpaRepository<MstMerchant, UUID> {
    Optional<MstMerchant> findByEmail(String email);
}
