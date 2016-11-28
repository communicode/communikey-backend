/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2016
 */
package de.communicode.communikey.exception;

import de.communicode.communikey.domain.UserGroup;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Thrown to indicate that a method has been passed conflicting {@link UserGroup} entity data.
 *
 * @author sgreb@communicode.de
 * @since 0.2.0
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class UserGroupConflictException extends RuntimeException {

    /**
     * Constructs an {@code UserGroupConflictException} with the detailed conflict message.
     *
     * @param conflictMessage the detailed message about the conflict
     */
    public UserGroupConflictException(String conflictMessage) {
        super(conflictMessage);
    }
}
