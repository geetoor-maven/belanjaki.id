package com.belanjaki.id.usersmanagement.repository;

import com.belanjaki.id.usersmanagement.dto.user.request.RequestUpdateUserAddressDTO;
import com.belanjaki.id.usersmanagement.model.MstUser;
import com.belanjaki.id.usersmanagement.model.MstUserAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MstUserAddressRepository extends JpaRepository<MstUserAddress, UUID> {

    MstUserAddress findByMstUser(MstUser mstUser);

    @Modifying
    @Query(value = "update MstUserAddress m set m.street = :#{#dto.street}, m.city = :#{#dto.city}, m.state = :#{#dto.state}, m.postalCode = :#{#dto.postalCode}")
    void updateUserAddress(@Param("dto")RequestUpdateUserAddressDTO dto);

}
