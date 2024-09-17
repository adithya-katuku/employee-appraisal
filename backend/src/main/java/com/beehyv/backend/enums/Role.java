package com.beehyv.backend.enums;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    EMPLOYEE,
    ADMIN;

    @Override
    public String getAuthority() {
        return this.name();
    }

}
