/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2016
*/
package de.communicode.communikey.type;

/**
 * User roles for {@link de.communicode.communikey.domain.User} entities.
 *
 * @author sgreb@communicode.de
 * @since 0.2.0
 */
public enum UserRoleType {
    /**
     * The role for a user with administrative permissions.
     */
    ROLE_ADMIN,

    /**
     * The default role for every user.
     */
    ROLE_USER;
}
