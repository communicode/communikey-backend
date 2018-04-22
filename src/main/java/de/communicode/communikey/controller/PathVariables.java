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
package de.communicode.communikey.controller;

import de.communicode.communikey.domain.EncryptionJob;
import de.communicode.communikey.domain.Key;
import de.communicode.communikey.domain.KeyCategory;
import de.communicode.communikey.domain.Tag;

/**
 * Provides path variables constants.
 *
 * @author sgreb@communicode.de
 * @author dvonderbey@communicode.de
 * @since 0.2.0
 */
public final class PathVariables {

    public static final String USER_ACTIVATION_TOKEN = "activation_token";
    public static final String USER_EMAIL = "email";
    public static final String USER_LOGIN = "login";

    /**
     * The Hashid of a {@link Key}.
     *
     * <p>Exposed as ID through the API.
     *
     * @since 0.12.0
     */
    public static final String KEY_ID = "keyId";

    /**
     * The Hashid of a {@link KeyCategory}.
     *
     * <p>Exposed as ID through the API.
     *
     * @since 0.13.0
     */
    public static final String KEYCATEGORY_ID = "keyCategoryId";

    /**
     * The Token of a {@link EncryptionJob}.
     *
     * @since 0.15.0
     */
    public static final String JOB_TOKEN = "token";

    /**
     * The Hashid of a {@link Tag}.
     *
     * <p>Exposed as ID through the API.
     *
     * @since 0.18.0
     */
    public static final String TAG_ID = "tagId";

    private PathVariables() {}
}
