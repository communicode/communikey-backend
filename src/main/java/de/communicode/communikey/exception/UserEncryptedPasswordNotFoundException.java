/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2017
 */
package de.communicode.communikey.exception;

import de.communicode.communikey.domain.Key;
import de.communicode.communikey.domain.User;
import de.communicode.communikey.domain.UserEncryptedPassword;

/**
 * Thrown to indicate that a method has been passed a non existing {@link UserEncryptedPassword}.
 *
 * @author dvonderbey@communicode.de
 * @since 0.15.0
 */
public class UserEncryptedPasswordNotFoundException extends RuntimeException {

    /**
     * Constructs a {@code UserEncryptedPasswordNotFoundException}.
     */
    public UserEncryptedPasswordNotFoundException() {
        super("userEncryptedPassword not found");
    }

    /**
     * Constructs a {@code UserEncryptedPasswordNotFoundException} with the specified {@link Key} Hashid and {@link User} name.
     *
     * @param keyHashid the Hashid of the key that has not been found
     * @since 0.12.0
     */
    public UserEncryptedPasswordNotFoundException(Long keyHashid, String username) {
        super("userEncryptedPassword with ID '" + keyHashid + "' for user '" + username + "' not found");
    }
}
