package com.belanjaki.id.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashSet;
import java.util.Set;

@Configuration
public class PermittedPathExceptAdmin {

    @Bean
    public Set<String> permittedPathsExceptAdmin() {
        Set<String> permittedPaths = new HashSet<>();
        permittedPaths.add("/api/v1/merchant/list");
        permittedPaths.add("/api/v1/merchant/update-status");
        return permittedPaths;
    }
}
