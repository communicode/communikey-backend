/*
 * This file is part of communikey.
 * Copyright (C) 2016-2018  communicode AG <communicode.de>
 *
 * communikey is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
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
