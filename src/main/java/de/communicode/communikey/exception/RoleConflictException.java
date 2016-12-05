/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2016
 */
package de.communicode.communikey.exception;

import de.communicode.communikey.domain.Role;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Thrown to indicate that a method has been passed conflicting {@link Role} entity data.
 *
 * @author sgreb@communicode.de
 * @since 0.2.0
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class RoleConflictException extends RuntimeException {

    /**
     * Constructs an {@code RoleConflictException} with the detailed conflict message.
     *
     * @param conflictMessage the detailed message about the conflict
     */
    public RoleConflictException(String conflictMessage) {
        super(conflictMessage);
    }
}
