/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2017
 */
package de.communicode.communikey.security;

/**
 * Provides constants for Spring Security authorities.
 *
 * @author sgreb@communicode.de
 * @author lleifermann@communicode.de
 * @since 0.2.0
 */
public final class AuthoritiesConstants {

    public static final String ADMIN = "ROLE_ADMIN";
    public static final String USER = "ROLE_USER";
    public static final String ANONYMOUS = "ROLE_ANONYMOUS";
    public static final String ROOT = "root";

    private AuthoritiesConstants() {}
}
