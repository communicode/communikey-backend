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
