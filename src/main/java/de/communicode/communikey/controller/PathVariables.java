/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2017
 */
package de.communicode.communikey.controller;

import de.communicode.communikey.domain.Key;
import de.communicode.communikey.domain.KeyCategory;

/**
 * Provides path variables constants.
 *
 * @author sgreb@communicode.de
 * @author dvonderbey@communicode.de
 * @since 0.2.0
 */
public final class PathVariables {

    private PathVariables() {}

    public static final String USER_ACTIVATION_TOKEN = "activation_token";
    public static final String USER_EMAIL = "email";
    public static final String USER_LOGIN = "login";

    /**
     * The Hashid of a {@link Key}.
     *
     * <<p>Exposed as ID through the API.
     *
     * @since 0.12.0
     */
    public static final String KEY_ID = "keyId";

    /**
     * The Hashid of a {@link KeyCategory}.
     *
     * <<p>Exposed as ID through the API.
     *
     * @since 0.13.0
     */
    public static final String KEYCATEGORY_ID = "keyCategoryId";
}
