/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2017
 */
package de.communicode.communikey.exception.user;

import de.communicode.communikey.domain.User;

/**
 * Thrown to indicate that a method has been passed a not existing {@link User} entity.
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
    public UserNotFoundException(Long userId) {
        super("could not find user with ID " + userId);
    }

    /**
     * Constructs an {@code UserNotFoundException} with the specified {@link User} email applied to the message.
     *
     * @param email the email of the user entity that has not been found
     */
    public UserNotFoundException(String email) {
        super("could not find user with email " + email);
    }
}
