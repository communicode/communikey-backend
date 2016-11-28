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
 * Thrown to indicate that a method has been passed an not existing {@link UserGroup} entity.
 *
 * @author sgreb@communicode.de
 * @since 0.2.0
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserGroupNotFoundException extends RuntimeException {

    /**
     * Constructs an {@code UserGroupNotFoundException} with the specified {@link UserGroup} ID applied to the message.
     *
     * @param userGroupId the ID of the user group ID entity that has not been found
     */
    public UserGroupNotFoundException(long userGroupId) {
        super("could not find user group with ID" + userGroupId);
    }
}
