package com.belanjaki.id.config;

import com.belanjaki.id.common.constant.BasePath;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashSet;
import java.util.Set;

@Configuration
public class PermittedPathExceptUser {

    @Bean
    public Set<String> permittedPathsExceptUser() {
        Set<String> permittedPaths = new HashSet<>();
        permittedPaths.add(BasePath.USER);
        permittedPaths.add(BasePath.USER + "/update-photo");
        permittedPaths.add(BasePath.USER + "/update-address");
        permittedPaths.add(BasePath.USER + "/user-address");
        return permittedPaths;
    }

}
