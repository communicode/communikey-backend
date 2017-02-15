/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2017
 */
package de.communicode.communikey.exception;

import de.communicode.communikey.domain.UserGroup;

/**
 * Thrown to indicate that a method has been passed conflicting {@link UserGroup} data.
 *
 * @author sgreb@communicode.de
 * @since 0.2.0
 */
public class UserGroupConflictException extends RuntimeException {

    /**
     * Constructs a {@code UserGroupConflictException} with the detailed conflict message.
     *
     * @param message the detailed message about the conflict
     */
    public UserGroupConflictException(String message) {
        super(message);
    }
}
