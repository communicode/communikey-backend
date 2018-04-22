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

/**
 * Thrown to indicate that a method has been passed a not existing {@link Key}.
 *
 * @author sgreb@communicode.de
 * @since 0.2.0
 */
public class KeyNotFoundException extends RuntimeException {

    /**
     * Constructs a {@code KeyNotFoundException}.
     */
    public KeyNotFoundException() {
        super("key not found");
    }

    /**
     * Constructs a {@code KeyNotFoundException} with the specified {@link Key} Hashid.
     *
     * @param keyHashid the Hashid of the key that has not been found
     * @since 0.12.0
     */
    public KeyNotFoundException(String keyHashid) {
        super("key with ID '" + keyHashid + "' not found");
    }
}
