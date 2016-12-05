/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2016
 */
package de.communicode.communikey.controller;

import de.communicode.communikey.domain.Key;
import de.communicode.communikey.domain.KeyCategory;
import de.communicode.communikey.domain.Role;
import de.communicode.communikey.domain.User;
import de.communicode.communikey.domain.UserGroup;

/**
 * Provides request mapping constants.
 *
 * @author sgreb@communicode.de
 * @since 0.2.0
 */
public final class RequestMappings {

    private RequestMappings() {}

    /**
     * The endpoint for the HTTP status code {@code 403}.
     */
    public static final String HTTP_STATUS_CODE_403 = "/error/403";

    /**
     * The endpoint for the HTTP status code {@code 404}.
     */
    public static final String HTTP_STATUS_CODE_404 = "/error/404";

    /**
     * The endpoint for {@link Key} entities.
     */
    public static final String KEYS = "/keys";

    /**
     * The endpoint for {@link KeyCategory} entities.
     */
    public static final String KEY_CATEGORIES = "/categories";

    /**
     * The request parameter for a {@link KeyCategory} entity ID.
     */
    public static final String KEY_CATEGORY_ID = "/{keyCategoryId}";

    /**
     * The request parameter for a {@link Key} entity ID.
     */
    public static final String KEY_ID = "/{keyId}";

    /**
     * The {@link User} login endpoint.
     */
    public static final String LOGIN = "/login";

    /**
     * The {@link User} logout endpoint.
     */
    public static final String LOGOUT = "/logout";

    /**
     * The root endpoint.
     */
    public static final String ROOT = "/";

    /**
     * The endpoint for {@link User} entities.
     */
    public static final String USERS = "/users";

    /**
     * The endpoint for {@link Role} entities.
     */
    public static final String ROLES = USERS + "/roles";

    /**
     * The endpoint for {@link UserGroup} entities.
     */
    public static final String USER_GROUPS = USERS + "/groups";

    /**
     * The request parameter for a {@link UserGroup} entity ID.
     */
    public static final String USER_GROUP_ID = "/{userGroupId}";

    /**
     * The request parameter for a {@link User} entity ID.
     */
    public static final String USER_ID = "/{userId}";
}
