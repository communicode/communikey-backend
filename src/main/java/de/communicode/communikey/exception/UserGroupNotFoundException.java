/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2017
 */
package de.communicode.communikey.exception;

import de.communicode.communikey.domain.UserGroup;

/**
 * Thrown to indicate that a method has been passed an not existing {@link UserGroup}.
 *
 * @author sgreb@communicode.de
 * @since 0.2.0
 */
public class UserGroupNotFoundException extends RuntimeException {

    /**
     * Constructs a {@code UserGroupNotFoundException} for the specified {@link UserGroup}.
     *
     * @param name the name of the user group that has not been found
     */
    public UserGroupNotFoundException(String name) {
        super("user group '" + name + "' not found");
    }
}
