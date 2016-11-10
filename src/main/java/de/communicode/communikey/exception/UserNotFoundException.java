/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2016
 */
package de.communicode.communikey.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Thrown to indicate that a method has been passed an not existing {@link de.communicode.communikey.domain.User} entity.
 *
 * @author sgreb@communicode.de
 * @since 0.2.0
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException {

    /**
     * Constructs an {@code UserNotFoundException} with the given {@code userId} applied to the message.
     *
     * @param userId the ID of the {@link de.communicode.communikey.domain.User} entity
     */
    public UserNotFoundException(long userId) {
        super("could not find user '" + userId + "'.");
    }
}
