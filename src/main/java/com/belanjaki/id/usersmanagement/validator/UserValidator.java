package com.belanjaki.id.usersmanagement.validator;

import com.belanjaki.id.common.ResourceLabel;
import com.belanjaki.id.common.exception.ItemAlreadyExistException;
import com.belanjaki.id.common.exception.OtpValidationException;
import com.belanjaki.id.common.exception.ResourceNotFoundException;
import com.belanjaki.id.common.util.OtpUtils;
import com.belanjaki.id.usersmanagement.dto.user.request.RequestCreateUserDTO;
import com.belanjaki.id.usersmanagement.dto.user.request.RequestLoginUserDTO;
import com.belanjaki.id.usersmanagement.dto.user.request.RequestOtpDTO;
import com.belanjaki.id.usersmanagement.model.MstOtpUserAuth;
import com.belanjaki.id.usersmanagement.model.MstUser;
import com.belanjaki.id.usersmanagement.repository.MstOtpUserAuthRepository;
import com.belanjaki.id.usersmanagement.repository.MstUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@AllArgsConstructor
public class UserValidator {

    private final MstUserRepository mstUserRepository;
    private final MstOtpUserAuthRepository mstOtpUserAuthRepository;
    private final ResourceLabel resourceLabel;
    private final BCryptPasswordEncoder passwordEncoder;

    public void validateFileUploadPhotoUser(MultipartFile file){
        if (file == null || file.isEmpty()){
            throw new ResourceNotFoundException(resourceLabel.getBodyLabel("photo.not.added.img"));
        }

        if (file.getSize() > 2 * 1024 * 1024) {
            throw new IllegalArgumentException(resourceLabel.getBodyLabel("photo.file.size.two.mega.byte"));
        }

        String contentType = file.getContentType();
        if (!"image/jpeg".equals(contentType) && !"image/png".equals(contentType)) {
            throw new IllegalArgumentException(resourceLabel.getBodyLabel("photo.only.jpg.png.format"));
        }
    }

    public void createUserValidatorIfHasBeenRegister(RequestCreateUserDTO dto){
        mstUserRepository.findByEmail(dto.getEmail()).ifPresent(user -> {
            throw new ItemAlreadyExistException(resourceLabel.getBodyLabel("user.create.duplicate.email"));
        });
    }

    public MstUser getUserWithValidate(RequestLoginUserDTO dto){
        MstUser mstUser = mstUserRepository.findByEmail(dto.getEmail()).orElseThrow(() -> new ResourceNotFoundException(resourceLabel.getBodyLabel("user.find.email.not.found")));
        boolean pwdMatch = passwordEncoder.matches(dto.getPassword(), mstUser.getPassword());
        if (!pwdMatch){
            throw new ResourceNotFoundException(resourceLabel.getBodyLabel("user.password.validation.not.valid"));
        }
        return mstUser;
    }

    public void validateUserEmailWithOtp(RequestOtpDTO dto){
        MstUser mstUser = mstUserRepository.findByEmail(dto.getEmail()).orElseThrow(() -> new ResourceNotFoundException(resourceLabel.getBodyLabel("user.find.email.not.found")));
        MstOtpUserAuth mstOtpUserAuth = mstOtpUserAuthRepository.findByMstUser(mstUser);
        if (mstOtpUserAuth != null){
            String otpSecretKey = mstOtpUserAuth.getSecretOtp();
            boolean otpMatch = passwordEncoder.matches(dto.getOtp(), otpSecretKey);

            if (!otpMatch || OtpUtils.isMoreThanOneMinute(mstOtpUserAuth.getUpdatedDate())){
                throw new OtpValidationException(resourceLabel.getBodyLabel("user.otp.validation.not.valid"));
            }
        }
    }
}
