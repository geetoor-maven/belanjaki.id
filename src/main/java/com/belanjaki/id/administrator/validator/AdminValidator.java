package com.belanjaki.id.administrator.validator;

import com.belanjaki.id.administrator.dto.request.RequestCreateAdminDTO;
import com.belanjaki.id.administrator.repository.MstAdministratorRepository;
import com.belanjaki.id.common.ResourceLabel;
import com.belanjaki.id.common.exception.ItemAlreadyExistException;
import com.belanjaki.id.common.exception.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class AdminValidator {

    private final MstAdministratorRepository mstAdministratorRepository;
    private final ResourceLabel resourceLabel;

    public void validateIfAdminEmailHasBeenRegister(RequestCreateAdminDTO dto){
        mstAdministratorRepository.findByEmail(dto.getEmail()).ifPresent(user -> {
            throw new ItemAlreadyExistException(resourceLabel.getBodyLabel("admin.create.duplicate.email"));
        });
    }

}
