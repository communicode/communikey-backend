/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2017
 */
package de.communicode.communikey.exception.role;

import de.communicode.communikey.domain.Role;

/**
 * Thrown to indicate that a method has been passed conflicting {@link Role} entity data.
 *
 * @author sgreb@communicode.de
 * @since 0.2.0
 */
public class RoleConflictException extends RuntimeException {

    /**
     * Constructs a {@code RoleConflictException} with the detailed conflict message.
     *
     * @param message the detailed message about the conflict
     */
    public RoleConflictException(String message) {
        super(message);
    }
}
