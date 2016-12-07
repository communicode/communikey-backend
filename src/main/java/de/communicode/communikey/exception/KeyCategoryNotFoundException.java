/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2016
 */
package de.communicode.communikey.exception;

import de.communicode.communikey.domain.KeyCategory;

/**
 * Thrown to indicate that a method has been passed an not existing {@link KeyCategory} entity.
 *
 * @author sgreb@communicode.de
 * @since 0.2.0
 */
public class KeyCategoryNotFoundException extends RuntimeException {

    /**
     * Constructs an {@code KeyCategoryNotFoundException} with the specified {@link KeyCategory} ID applied to the message.
     *
     * @param keyCategoryId the ID of the key category entity that has not been found
     */
    public KeyCategoryNotFoundException(long keyCategoryId) {
        super("could not find key category with ID " + keyCategoryId);
    }
}