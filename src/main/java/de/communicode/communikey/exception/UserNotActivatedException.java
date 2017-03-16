/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2017
 */
package de.communicode.communikey.exception;

import de.communicode.communikey.domain.User;

/**
 * Thrown to indicate that a not activated {@link User} trying to authenticate.
 *
 * @author sgreb@communicode.de
 * @since 0.2.0
 */
public class UserNotActivatedException extends RuntimeException {

    /**
     * Constructs an {@code UserNotActivatedException} for the specified {@link User} login.
     *
     * @param login the login of the user that has not been found
     */
    public UserNotActivatedException(String login) {
        super("user " + login + " is not activated");
    }
}
