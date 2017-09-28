/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2017
 */
package de.communicode.communikey.exception;

/**
 * Thrown to indicate that the user has no access to a key. Is checked for every
 * userEncryptedPassword that is posted.
 *
 * @author dvonderbey@communicode.de
 * @since 0.2.0
 */
public class KeyNotAccessibleByUserException extends RuntimeException {

    /**
     * Constructs a {@code KeyNotAccessibleByUserException}.
     */
    public KeyNotAccessibleByUserException() {
        super("Key not accessible by user!");
    }
}
