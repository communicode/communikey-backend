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
 * Thrown to indicate that a method has been passed conflicting {@link KeyCategory} data.
 *
 * @author sgreb@communicode.de
 * @since 0.2.0
 */
public class KeyCategoryConflictException extends RuntimeException {

    /**
     * Constructs an {@code KeyCategoryConflictException} with the detailed conflict message.
     *
     * @param message the detailed message about the conflict
     */
    public KeyCategoryConflictException(String message) {
        super(message);
    }
}
