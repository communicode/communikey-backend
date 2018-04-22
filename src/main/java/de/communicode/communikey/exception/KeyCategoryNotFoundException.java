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

import de.communicode.communikey.domain.KeyCategory;

/**
 * Thrown to indicate that a method has been passed a not existing {@link KeyCategory}.
 *
 * @author sgreb@communicode.de
 * @author dvonderbey@communicode.de
 * @since 0.2.0
 */
public class KeyCategoryNotFoundException extends RuntimeException {

    /**
     * Constructs a {@code KeyNotFoundException}.
     */
    public KeyCategoryNotFoundException() {
        super("key category not found");
    }

    /**
     * Constructs an {@code KeyCategoryNotFoundException} with the specified {@link KeyCategory} Hashid.
     *
     * @param keyCategoryHashid the Hashid of the key category that has not been found
     */
    public KeyCategoryNotFoundException(String keyCategoryHashid) {
        super("key category with ID '" + keyCategoryHashid + "' not found");
    }
}
