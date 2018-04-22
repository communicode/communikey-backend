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
package de.communicode.communikey.service.payload;

import de.communicode.communikey.domain.Tag;
import org.hibernate.validator.constraints.NotBlank;

/**
 * A payload object for a {@link Tag}.
 *
 * @author dvonderbey@communicode.de
 * @since 0.18.0
 */
public class TagPayload {

    @NotBlank
    private String name;

    @NotBlank
    private String color;

    public TagPayload() {}

    public String getName() {
        return name.trim();
    }

    public String getColor() {
        return color;
    }
}
