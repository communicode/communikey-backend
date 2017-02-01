/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2017
 */
package de.communicode.communikey.exception.privilege;

import de.communicode.communikey.domain.Privilege;

/**
 * Thrown to indicate that a method has been passed conflicting {@link Privilege} entity data.
 *
 * @author sgreb@communicode.de
 * @since 0.2.0
 */
public class PrivilegeConflictException extends RuntimeException {

    /**
     * Constructs a {@code PrivilegeConflictException} with the detailed conflict message.
     *
     * @param message the detailed message about the conflict
     */
    public PrivilegeConflictException(String message) {
        super(message);
    }
}
