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

import de.communicode.communikey.domain.Tag;

/**
 * Thrown to indicate that a method has been passed a not existing {@link Tag}.
 *
 * @author dvonderbey@communicode.de
 * @since 0.18.0
 */
public class TagNotFoundException extends RuntimeException {

    /**
     * Constructs a {@code TagNotFoundException}.
     */
    public TagNotFoundException() {
        super("tag not found");
    }

    /**
     * Constructs a {@code TagNotFoundException} with the specified {@link Tag} Hashid.
     *
     * @param tagHashid the Hashid of the tag that has not been found
     * @since 0.12.0
     */
    public TagNotFoundException(String tagHashid) {
        super("tag with ID '" + tagHashid + "' not found");
    }
}
