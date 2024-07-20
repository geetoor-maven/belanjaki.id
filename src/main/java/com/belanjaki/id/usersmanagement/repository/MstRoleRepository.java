package com.belanjaki.id.usersmanagement.repository;

import com.belanjaki.id.usersmanagement.model.MstRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MstRoleRepository extends JpaRepository<MstRole, UUID> {

}
