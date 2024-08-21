package com.belanjaki.id.administrator.repository;

import com.belanjaki.id.administrator.model.MstAdministrator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MstAdministratorRepository extends JpaRepository<MstAdministrator, UUID> {
    Optional<MstAdministrator> findByEmail(String email);
}
