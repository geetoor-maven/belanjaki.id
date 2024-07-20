package com.belanjaki.id.usersmanagement.service;

import com.belanjaki.id.usersmanagement.repository.MstUserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class MstUserService {

    private final MstUserRepository mstUserRepository;


}
