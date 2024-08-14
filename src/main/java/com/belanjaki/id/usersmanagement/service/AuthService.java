package com.belanjaki.id.usersmanagement.service;

import com.belanjaki.id.common.ResourceLabel;
import com.belanjaki.id.common.constant.ReturnCode;
import com.belanjaki.id.common.constant.RoleEnum;
import com.belanjaki.id.common.dto.BaseResponse;
import com.belanjaki.id.common.dto.Meta;
import com.belanjaki.id.common.exception.ResourceNotFoundException;
import com.belanjaki.id.common.util.OtpUtils;
import com.belanjaki.id.jwt.JWTUtils;
import com.belanjaki.id.usersmanagement.dto.user.request.RequestCreateUserDTO;
import com.belanjaki.id.usersmanagement.dto.user.request.RequestLoginUserDTO;
import com.belanjaki.id.usersmanagement.dto.user.request.RequestOtpDTO;
import com.belanjaki.id.usersmanagement.dto.user.response.ResponseCreateUserDTO;
import com.belanjaki.id.usersmanagement.dto.user.response.ResponseLoginUserDTO;
import com.belanjaki.id.usersmanagement.dto.user.response.ResponseLoginWithOtpDTO;
import com.belanjaki.id.usersmanagement.model.MstOtpUserAuth;
import com.belanjaki.id.usersmanagement.model.MstRole;
import com.belanjaki.id.usersmanagement.model.MstUser;
import com.belanjaki.id.usersmanagement.repository.MstOtpUserAuthRepository;
import com.belanjaki.id.usersmanagement.repository.MstRoleRepository;
import com.belanjaki.id.usersmanagement.repository.MstUserRepository;
import com.belanjaki.id.usersmanagement.validator.UserValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.lang.module.ResolutionException;
import java.security.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthService implements UserDetailsService {

    private final MstUserRepository mstUserRepository;
    private final MstRoleRepository mstRoleRepository;
    private final MstOtpUserAuthRepository mstOtpUserAuthRepository;
    private final ResourceLabel resourceLabel;
    private final UserValidator userValidator;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JWTUtils jwtUtils;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        MstUser mstUser = mstUserRepository.findByEmail(username).orElseThrow(() -> new ResourceNotFoundException(resourceLabel.getBodyLabel("user.find.email.not.found")));
        return new org.springframework.security.core.userdetails.User(mstUser.getEmail(), mstUser.getPassword(), new ArrayList<>());
    }

    @Transactional
    public Object registerUser(RequestCreateUserDTO dto){
        // validate if user has been register
        userValidator.createUserValidatorIfHasBeenRegister(dto);

        MstRole mstRole = mstRoleRepository.findByRoleName(RoleEnum.USER.getRoleName()).orElseThrow(() -> new ResolutionException(resourceLabel.getBodyLabel("role.find.not.found")));
        // Create new user using builder pattern
        MstUser mstUser = createObjectUser(dto, mstRole);
        // save users
        mstUserRepository.save(mstUser);
        ResponseCreateUserDTO responseCreateUserDTO = createObjectResponseUserCreate(mstUser);
        BaseResponse<ResponseCreateUserDTO> baseResponse = new BaseResponse<>(responseCreateUserDTO, new Meta(ReturnCode.SUCCESSFULLY_REGISTER.getStatusCode(), ReturnCode.SUCCESSFULLY_REGISTER.getMessage()));
        return baseResponse.getCustomizeResponse("user_create");

    }

    @Transactional
    public Object loginUser(RequestLoginUserDTO dto){
        log.info("User try {} to login ", dto.getEmail());
        MstUser userWithValidate = userValidator.getUserWithValidate(dto);

        MstOtpUserAuth mstOtpUserAuth = mstOtpUserAuthRepository.findByMstUser(userWithValidate);
        if (mstOtpUserAuth == null){
            mstOtpUserAuthRepository.save(createObjectOtpUserAuth(userWithValidate));
        }else {
            String otpBeforeHash = OtpUtils.generateOtp();
            log.info("otp{} = ", otpBeforeHash);
            String secretOtp = passwordEncoder.encode(otpBeforeHash);
            mstOtpUserAuthRepository.updateOtpSecretKey(secretOtp, new Date(),mstOtpUserAuth.getOtpAuthId());
        }

        ResponseLoginUserDTO responseLoginUserDTO = createObjectResponseUserLogin(dto.getEmail());
        BaseResponse<ResponseLoginUserDTO> baseResponse = new BaseResponse<>(responseLoginUserDTO, new Meta(ReturnCode.SUCCESSFULLY_OTP_SEND.getStatusCode(), ReturnCode.SUCCESSFULLY_OTP_SEND.getMessage()));
        return baseResponse.getCustomizeResponse("otp_send");
    }

    public Object validateOtpAfterLogin(RequestOtpDTO requestOtpDTO){
        userValidator.validateUserEmailWithOtp(requestOtpDTO);

        final UserDetails userDetails = loadUserByUsername(requestOtpDTO.getEmail());
        final String jwt = jwtUtils.generateToken(userDetails);

        ResponseLoginWithOtpDTO responseLoginWithOtpDTO = createObjectResponseLoginWithOtp("jwt", requestOtpDTO.getEmail());
        BaseResponse<ResponseLoginWithOtpDTO> baseResponse = new BaseResponse<>(responseLoginWithOtpDTO, new Meta(ReturnCode.SUCCESSFULLY_LOGIN.getStatusCode(), ReturnCode.SUCCESSFULLY_LOGIN.getMessage()));
        return baseResponse.getCustomizeResponse("login");
    }

    private MstOtpUserAuth createObjectOtpUserAuth(MstUser mstUser){
        String otpBeforeHash = OtpUtils.generateOtp();
        log.info("otp{} = ", otpBeforeHash);
        MstOtpUserAuth mstOtpUserAuth = MstOtpUserAuth.builder()
                .otpAuthId(UUID.randomUUID())
                .mstUser(mstUser)
                .secretOtp(passwordEncoder.encode(otpBeforeHash))
                .build();
        mstOtpUserAuth.setCreatedBy(mstUser.getName());
        mstOtpUserAuth.setUpdatedBy(mstUser.getName());
        return mstOtpUserAuth;
    }

    private MstUser createObjectUser(RequestCreateUserDTO dto, MstRole mstRole){
        MstUser mstUser = MstUser.builder()
                .userId(UUID.randomUUID())
                .name(dto.getName())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .numberPhone(dto.getNumberPhone())
                .mstRole(mstRole)
                .build();
        mstUser.setCreatedBy(dto.getName());
        mstUser.setUpdatedBy(dto.getName());
        return mstUser;
    }

    private ResponseCreateUserDTO createObjectResponseUserCreate(MstUser mstUser){
        return ResponseCreateUserDTO.builder()
                .name(mstUser.getName())
                .email(mstUser.getEmail())
                .build();
    }

    private ResponseLoginUserDTO createObjectResponseUserLogin(String email){
        return ResponseLoginUserDTO.builder()
                .email(email)
                .message(resourceLabel.getBodyLabel("user.login.send.otp.success"))
                .build();
    }

    private ResponseLoginWithOtpDTO createObjectResponseLoginWithOtp(String jwt, String email){
        return ResponseLoginWithOtpDTO.builder()
                .email(email)
                .token(jwt)
                .build();
    }

    public MstUser getUserLogin(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        return mstUserRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException(resourceLabel.getBodyLabel("invalid.crediantial")));
    }
}
