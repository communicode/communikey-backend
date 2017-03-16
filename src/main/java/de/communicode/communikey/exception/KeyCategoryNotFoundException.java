/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2017
 */
package de.communicode.communikey.exception;

import de.communicode.communikey.domain.KeyCategory;

/**
 * Thrown to indicate that a method has been passed a not existing {@link KeyCategory}.
 *
 * @author sgreb@communicode.de
 * @since 0.2.0
 */
public class KeyCategoryNotFoundException extends RuntimeException {

    /**
     * Constructs an {@code KeyCategoryNotFoundException} with the specified {@link KeyCategory} ID.
     *
     * @param keyCategoryId the ID of the key category that has not been found
     */
    public KeyCategoryNotFoundException(Long keyCategoryId) {
        super("key category with ID '" + keyCategoryId + "' not found");
    }
}