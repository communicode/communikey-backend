/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2017
 */
package de.communicode.communikey.exception;

/**
 * Thrown to indicate that a method has been passed a not existing authority.
 *
 * @author sgreb@communicode.de
 * @since 0.3.0
 */
public class AuthorityNotFoundException extends RuntimeException {
    /**
     * Constructs an {@code AuthorityNotFoundException} for the specified authority name.
     *
     * @param authorityName the name of the authority that has not been found
     */
    public AuthorityNotFoundException(String authorityName) {
        super("authority '" + authorityName + "' not found");
    }
}
