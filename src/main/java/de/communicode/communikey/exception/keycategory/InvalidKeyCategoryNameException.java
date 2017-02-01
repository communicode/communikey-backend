/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2017
 */
package de.communicode.communikey.exception.keycategory;

import de.communicode.communikey.domain.KeyCategory;

/**
 * Thrown to indicate that a method has been passed a invalid {@link KeyCategory} name.
 *
 * @author sgreb@communicode.de
 * @since 0.2.0
 */
public class InvalidKeyCategoryNameException extends RuntimeException {
    /**
     * Constructs an {@code InvalidKeyCategoryNameException} with the specified invalid name applied to the message.
     *
     * @param message the cause this exception has been thrown
     */
    public InvalidKeyCategoryNameException(String message) {
        super(message);
    }
}