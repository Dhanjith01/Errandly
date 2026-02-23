package com.errandly.Errandly.user.entity;

import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;

public enum Roles implements GrantedAuthority{
    CUSTOMER,
    ADMIN,
    RUNNER;

    @Override
    public @Nullable String getAuthority() {
        
        return "ROLE_"+name();
    }
}
