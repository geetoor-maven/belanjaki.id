package com.belanjaki.id.administrator.validator;

import com.belanjaki.id.administrator.dto.request.RequestCreateAdminDTO;
import com.belanjaki.id.administrator.dto.request.RequestLoginAdminDTO;
import com.belanjaki.id.administrator.model.MstAdministrator;
import com.belanjaki.id.administrator.repository.MstAdministratorRepository;
import com.belanjaki.id.common.ResourceLabel;
import com.belanjaki.id.common.exception.ItemAlreadyExistException;
import com.belanjaki.id.common.exception.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class AdminValidator {

    private final MstAdministratorRepository mstAdministratorRepository;
    private final ResourceLabel resourceLabel;
    private final PasswordEncoder passwordEncoder;

    public void validateIfAdminEmailHasBeenRegister(RequestCreateAdminDTO dto){
        mstAdministratorRepository.findByEmail(dto.getEmail()).ifPresent(user -> {
            throw new ItemAlreadyExistException(resourceLabel.getBodyLabel("admin.create.duplicate.email"));
        });
    }

    public MstAdministrator validateLoginAdmin(RequestLoginAdminDTO dto){
        MstAdministrator mstAdministrator = mstAdministratorRepository.findByEmail(dto.getEmail()).orElseThrow(() -> new ResourceNotFoundException(resourceLabel.getBodyLabel("admin.find.email.not.found")));
        boolean pwdMatch = passwordEncoder.matches(dto.getPassword(), mstAdministrator.getPassword());
        if (!pwdMatch){
            throw new ResourceNotFoundException(resourceLabel.getBodyLabel("user.password.validation.not.valid"));
        }
        if (!dto.getAdminNumber().equalsIgnoreCase(mstAdministrator.getAdminNumber())){
            throw new ResourceNotFoundException(resourceLabel.getBodyLabel("admin.validate.number.not.valid"));
        }
        return mstAdministrator;
    }

}
