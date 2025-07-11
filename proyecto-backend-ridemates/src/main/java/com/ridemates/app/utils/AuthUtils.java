package com.ridemates.app.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public interface AuthUtils {

    static Authentication authentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    static String username() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
