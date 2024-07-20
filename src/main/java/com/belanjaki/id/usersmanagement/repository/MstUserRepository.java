package com.belanjaki.id.usersmanagement.repository;

import com.belanjaki.id.usersmanagement.model.MstUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MstUserRepository extends JpaRepository<MstUser, UUID> {
    Optional<MstUser> findByEmail(String email);
}
