/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2017
 */
package de.communicode.communikey.exception;

import de.communicode.communikey.domain.User;

/**
 * Thrown to indicate that a method has been passed conflicting {@link User} entity data.
 *
 * @author sgreb@communicode.de
 * @since 0.2.0
 */
public class UserConflictException extends RuntimeException {

    /**
     * Constructs an {@code UserConflictException} with the detailed conflict message.
     *
     * @param message the detailed message about the conflict
     */
    public UserConflictException(String message) {
        super(message);
    }
}
