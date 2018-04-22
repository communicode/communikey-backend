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

import de.communicode.communikey.domain.UserGroup;

/**
 * Thrown to indicate that a method has been passed an not existing {@link UserGroup}.
 *
 * @author sgreb@communicode.de
 * @since 0.2.0
 */
public class UserGroupNotFoundException extends RuntimeException {

    /**
     * Constructs a {@code UserGroupNotFoundException} for the specified {@link UserGroup}.
     *
     * @param name the name of the user group that has not been found
     */
    public UserGroupNotFoundException(String name) {
        super("user group '" + name + "' not found");
    }

    /**
     * Constructs a {@code UserGroupNotFoundException} for the specified {@link UserGroup}.
     *
     * @param userGroupId the ID of the user group that has not been found
     * @since 0.9.0
     */
    public UserGroupNotFoundException(Long userGroupId) {
        super("user group with ID '" + userGroupId + "' not found");
    }
}
