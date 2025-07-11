package com.ridemates.app.user.domain;

import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public enum Role {
    PASSENGER,
    DRIVER,
    /**
     * Soy admin
     */
    ADMIN;

    public GrantedAuthority asAuthority() {
        return new SimpleGrantedAuthority("ROLE_" + this.name());
    }
}
