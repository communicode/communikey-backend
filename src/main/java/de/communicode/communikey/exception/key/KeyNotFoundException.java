/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2017
 */
package de.communicode.communikey.exception.key;

import de.communicode.communikey.domain.Key;

/**
 * Thrown to indicate that a method has been passed an not existing {@link Key} entity.
 *
 * @author sgreb@communicode.de
 * @since 0.2.0
 */
public class KeyNotFoundException extends RuntimeException {

    /**
     * Constructs a {@code KeyNotFoundException} with the specified {@link Key} ID applied to the message.
     *
     * @param keyId the ID of the key entity that has not been found
     */
    public KeyNotFoundException(Long keyId) {
        super("could not find key with ID " + keyId);
    }
}
