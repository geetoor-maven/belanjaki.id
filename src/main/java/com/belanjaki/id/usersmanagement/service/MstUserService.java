package com.belanjaki.id.usersmanagement.service;

import com.belanjaki.id.usersmanagement.repository.MstUserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MstUserService {

    private final MstUserRepository mstUserRepository;
    private final AuthService authService;

    @Transactional
    public Object updateUser(){


        return new Object();
    }
}
