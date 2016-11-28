/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2016
 */
package de.communicode.communikey.exception;

import de.communicode.communikey.domain.Key;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Thrown to indicate that a method has been passed an not existing {@link Key} entity.
 *
 * @author sgreb@communicode.de
 * @since 0.2.0
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class KeyNotFoundException extends RuntimeException {

    /**
     * Constructs an {@code KeyNotFoundException} with the specified {@link Key} ID applied to the message.
     *
     * @param keyId the ID of the key entity that has not been found
     */
    public KeyNotFoundException(long keyId) {
        super("could not find key with ID " + keyId);
    }
}
