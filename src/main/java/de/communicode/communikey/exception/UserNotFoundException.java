/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2017
 */
package de.communicode.communikey.exception;

import de.communicode.communikey.domain.User;

/**
 * Thrown to indicate that a method has been passed a not existing {@link User}.
 *
 * @author sgreb@communicode.de
 * @since 0.2.0
 */
public class UserNotFoundException extends RuntimeException {

    /**
     * Constructs an {@code UserNotFoundException} for the specified {@link User} login.
     *
     * @param userLogin the login of the user that has not been found
     */
    public UserNotFoundException(String userLogin) {
        super("could not find user " + userLogin);
    }
}
