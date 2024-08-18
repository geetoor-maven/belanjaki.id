package com.belanjaki.id.common.util;

import com.belanjaki.id.common.exception.ResourceNotFoundException;
import com.belanjaki.id.usersmanagement.model.MstRole;
import com.belanjaki.id.usersmanagement.repository.MstRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class RoleIdGetUtils {

    @Autowired
    private MstRoleRepository mstRoleRepository;

    public UUID getRoleID(String roleName){

        Optional<MstRole> mstRole = mstRoleRepository.findByRoleName(roleName);
        if (mstRole.isPresent()){
            return mstRole.get().getRoleId();
        }else {
            throw new ResourceNotFoundException("Invalid Creadiantials.");
        }

    }
}
