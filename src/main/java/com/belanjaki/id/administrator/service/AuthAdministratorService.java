package com.belanjaki.id.administrator.service;

import com.belanjaki.id.administrator.dto.request.RequestLoginAdminDTO;
import com.belanjaki.id.administrator.repository.MstAdministratorRepository;
import com.belanjaki.id.administrator.validator.AdminValidator;
import com.belanjaki.id.common.ResourceLabel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthAdministratorService {

    private final MstAdministratorRepository mstAdministratorRepository;
    private final AdminValidator adminValidator;
    private final ResourceLabel resourceLabel;


    public Object loginAdministrator(RequestLoginAdminDTO dto){
        adminValidator.validateLoginAdmin(dto);

        return new Object();
    }

}
