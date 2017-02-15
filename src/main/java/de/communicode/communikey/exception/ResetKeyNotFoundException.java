/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2017
 */
package de.communicode.communikey.exception;

/**
 * Thrown to indicate that a method has been passed a not existing reset key.
 *
 * @author sgreb@communicode.de
 * @since 0.2.0
 */
public class ResetKeyNotFoundException extends RuntimeException {

    /**
     * Constructs an {@code ResetKeyNotFoundException} for the specified reset key.
     *
     * @param resetKey the reset key that has not been found
     */
    public ResetKeyNotFoundException(String resetKey) {
        super("reset key '" + resetKey + "' not found");
    }
}