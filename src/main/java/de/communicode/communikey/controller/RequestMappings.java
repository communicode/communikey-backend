/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2017
 */
package de.communicode.communikey.controller;

import de.communicode.communikey.domain.Authority;
import de.communicode.communikey.domain.Key;
import de.communicode.communikey.domain.KeyCategory;
import de.communicode.communikey.domain.User;
import de.communicode.communikey.domain.UserGroup;

/**
 * Provides request mapping constants.
 *
 * @author sgreb@communicode.de
 * @since 0.2.0
 */
public final class RequestMappings {

    /**
     * The root endpoint.
     */
    public static final String API = "/api";

    /**
     * The endpoint for {@link Key} entities.
     */
    public static final String KEYS = API + "/keys";

    /**
     * The endpoint for {@link KeyCategory} entities.
     */
    public static final String KEY_CATEGORIES = API + "/categories";

    /**
     * The request parameter for a {@link KeyCategory} ID.
     */
    public static final String KEY_CATEGORIES_ID = "/{keyCategoryId}";

    /**
     * The request mapping for {@link KeyCategory} children.
     */
    public static final String KEY_CATEGORY_CHILDREN = KEY_CATEGORIES_ID + "/children";

    /**
     * The request mapping for the {@link KeyCategory} {@link Key}s.
     */
    public static final String KEY_CATEGORY_KEYS = KEY_CATEGORIES_ID + "/keys";

    /**
     * The request mapping for the responsible {@link KeyCategory} user.
     */
    public static final String KEY_CATEGORY_RESPONSIBLE = KEY_CATEGORIES_ID + "/responsible";

    /**
     * The request parameter for a {@link Key} entity ID.
     */
    public static final String KEYS_ID = "/{keyId}";

    /**
     * The endpoint for {@link User} entities.
     */
    public static final String USERS = API + "/users";

    /**
     * The endpoint for {@link UserGroup}s.
     */
    public static final String USER_GROUPS = API + "/groups";

    /**
     * The endpoint for {@link UserGroup}s.
     */
    public static final String USER_GROUPS_USERS = "/users";

    /**
     * The request parameter for a {@link UserGroup} name.
     */
    public static final String USER_GROUPS_NAME = "/{userGroupName}";

    /**
     * The request parameter for a {@link User} login.
     */
    public static final String USERS_LOGIN = "/{login}";

    /**
     * The endpoint to update the {@link Authority}s of a {@link User}.
     */
    public static final String USER_AUTHORITIES = USERS_LOGIN + "/authorities";

    /**
     * The endpoint to get {@link Key}s of a {@link User}.
     */
    public static final String USER_KEYS = USERS_LOGIN + "/keys";

    /**
     * The endpoint to activate a {@link User}.
     */
    public static final String USERS_ACTIVATE = "/activate";

    /**
     * The endpoint to deactivate a {@link User}.
     */
    public static final String USERS_DEACTIVATE = "/deactivate";

    /**
     * The endpoint to register a new {@link User}.
     */
    public static final String USERS_REGISTER = "/register";

    /**
     * The endpoint to reset a {@link User} password.
     */
    public static final String USERS_PASSWORD_RESET = "/reset_password";

    private RequestMappings() {}
}
