/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2016
 */
package de.communicode.communikey.exception;

import de.communicode.communikey.domain.UserGroup;

/**
 * Thrown to indicate that a method has been passed an not existing {@link UserGroup} entity.
 *
 * @author sgreb@communicode.de
 * @since 0.2.0
 */
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
