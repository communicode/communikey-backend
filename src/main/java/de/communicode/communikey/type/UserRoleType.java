/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2016
 */
package de.communicode.communikey.type;

import de.communicode.communikey.domain.User;

/**
 * Provides {@link User} role types.
 *
 * @author sgreb@communicode.de
 * @since 0.2.0
 */
public enum UserRoleType {
    /**
     * The user role with administrative permissions.
     */
    ROLE_ADMIN,

    /**
     * The default user role.
     */
    ROLE_USER;
}
