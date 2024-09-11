package com.belanjaki.id.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashSet;
import java.util.Set;

@Configuration
public class PermittedPathsConfig {

    @Bean
    public Set<String> permittedPaths() {
        Set<String> permittedPaths = new HashSet<>();

        // admin
        permittedPaths.add("/api/v1/admin/create");
        permittedPaths.add("/api/v1/admin/login");
        permittedPaths.add("/api/v1/admin/validate-otp");

        // user
        permittedPaths.add("/api/v1/user/create");
        permittedPaths.add("/api/v1/user/login");
        permittedPaths.add("/api/v1/user/validate-otp");
        permittedPaths.add("/api/v1/user/resend-otp");

        // merchant
        permittedPaths.add("/api/v1/merchant/create");
        permittedPaths.add("/api/v1/merchant/login");
        permittedPaths.add("/api/v1/merchant/validate-otp");

        permittedPaths.add("/swagger-ui/index.html");
        permittedPaths.add("/swagger-resources/**");
        permittedPaths.add("/v3/api-docs/**");
        permittedPaths.add("/swagger-ui/**");
        return permittedPaths;
    }

}
