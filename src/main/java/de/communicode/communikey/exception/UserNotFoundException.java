/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2016
 */
package de.communicode.communikey.exception;

import de.communicode.communikey.domain.User;

/**
 * Thrown to indicate that a method has been passed an not existing {@link User} entity.
 *
 * @author sgreb@communicode.de
 * @since 0.2.0
 */
public class UserNotFoundException extends RuntimeException {

    /**
     * Constructs an {@code UserNotFoundException} with the specified {@link User} ID applied to the message.
     *
     * @param userId the ID of the user entity that has not been found
     */
    public UserNotFoundException(long userId) {
        super("could not find user with ID " + userId);
    }

    /**
     * Constructs an {@code UserNotFoundException} with the specified {@link User} name applied to the message.
     *
     * @param username the name of the user entity that has not been found
     */
    public UserNotFoundException(String username) {
        super("could not find user with name " + username);
    }
}
