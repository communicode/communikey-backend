/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2017
 */
package de.communicode.communikey.exception;

/**
 * Thrown to indicate that the specified Hashid is not valid.
 *
 * @author dvonderbey@communicode.de
 * @since 0.2.0
 */
public class HashidNotValidException extends RuntimeException {

    /**
     * Constructs a {@code HashidNotValidException}.
     */
    public HashidNotValidException() {
        super("Hashid not valid");
    }
}
