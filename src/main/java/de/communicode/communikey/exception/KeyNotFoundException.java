/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2017
 */
package de.communicode.communikey.exception;

import de.communicode.communikey.domain.Key;

/**
 * Thrown to indicate that a method has been passed a not existing {@link Key}.
 *
 * @author sgreb@communicode.de
 * @since 0.2.0
 */
public class KeyNotFoundException extends RuntimeException {

    /**
     * Constructs a {@code KeyNotFoundException} with the specified {@link Key} ID.
     *
     * @param keyId the ID of the key that has not been found
     */
    public KeyNotFoundException(Long keyId) {
        super("key with ID '" + keyId + "' not found");
    }
}