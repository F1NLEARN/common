package com.finlearn.common.security;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class UserAuditorAware implements AuditorAware<UUID> {

    @Override
    public Optional<UUID> getCurrentAuditor() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .filter(Authentication::isAuthenticated).map(Authentication::getPrincipal)
                .filter(principal -> principal instanceof HeaderUserDetails)
                .map(principal -> ((HeaderUserDetails) principal).getUserId());
    }
}
