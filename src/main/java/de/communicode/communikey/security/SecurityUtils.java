/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2017
 */
package de.communicode.communikey.security;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Provides utilities for Spring Security.
 *
 * @author sgreb@communicode.de
 * @since 0.2.0
 */
public final class SecurityUtils {

    private static final int RANDOM_MIN_LENGTH = 20;

    private SecurityUtils() {}

    /**
     * Get the login of the current user.
     *
     * @return the login of the current user
     */
    public static String getCurrentUserLogin() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        String login = null;
        if (authentication != null) {
            if (authentication.getPrincipal() instanceof UserDetails) {
                UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal();
                login = springSecurityUser.getUsername();
            } else if (authentication.getPrincipal() instanceof String) {
                login = (String) authentication.getPrincipal();
            }
        }
        return login;
    }

    /**
     * Check if a user is authenticated.
     *
     * @return {@code true} if the user is authenticated, {@code false} otherwise
     */
    public static boolean isAuthenticated() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        return authentication != null && authentication.getAuthorities().stream()
            .noneMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(AuthoritiesConstants.ANONYMOUS));
    }

    /**
     * Check if the current user has a specific authority (security role).
     *
     * @param authority the authority to check
     * @return {@code true} if the current user has the authority, {@code false} otherwise
     */
    public static boolean isCurrentUserInRole(String authority) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        return authentication != null && authentication.getAuthorities().stream()
            .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(authority));
    }

    /**
     * Generates a random password.
     *
     * @return the generated password
     */
    public static String generateRandomPassword() {
        return RandomStringUtils.randomAlphanumeric(RANDOM_MIN_LENGTH);
    }

    /**
     * Generates an random activation key.
     *
     * @return the generated activation key
     */
    public static String generateRandomActivationKey() {
        return RandomStringUtils.randomNumeric(RANDOM_MIN_LENGTH);
    }

    /**
     * Generates a random reset key.
     *
     * @return the generated reset key
     */
    public static String generateRandomResetKey() {
        return RandomStringUtils.randomNumeric(RANDOM_MIN_LENGTH);
    }
}