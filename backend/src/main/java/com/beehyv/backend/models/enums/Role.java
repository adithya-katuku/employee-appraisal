package com.beehyv.backend.models.enums;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    EMPLOYEE,
    ADMIN;

    @Override
    public String getAuthority() {
        return this.name();
    }
}
