/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2016
 */
package de.communicode.communikey.config;

/**
 * Provides constants for the communikey application.
 *
 * @author sgreb@communicode.de
 * @since 0.1.0
 */
public final class CommunikeyConstants {

    private CommunikeyConstants() {}

    /**
     * The name of the login session ID cookie.
     *
     * @since 0.2.0
     */
    public static final String COOKIE_LOGIN_SESSION_ID = "JSESSIONID";

    /**
     * The {@link de.communicode.communikey.domain.User} login endpoint.
     *
     * @since 0.2.0
     */
    public static final String ENDPOINT_LOGIN = "/login";

    /**
     * The {@link de.communicode.communikey.domain.User} logout endpoint.
     *
     * @since 0.2.0
     */
    public static final String ENDPOINT_LOGOUT= "/logout";

    /**
     * The endpoint to the root endpoint.
     *
     * @since 0.2.0
     */
    public static final String ENDPOINT_ROOT= "/";

    /**
     * The endpoint for the {@code 403} HTTP status code.
     *
     * @since 0.2.0
     */
    public static final String ENDPOINT_HTTP_STATUS_CODE_403 = "/accessDenied";

    /**
     * The endpoint for the {@code 404} HTTP status code.
     *
     * @since 0.2.0
     */
    public static final String ENDPOINT_HTTP_STATUS_CODE_404 = "/error/404";

    /**
     * The endpoint for {@link de.communicode.communikey.domain.Key} entities.
     */
    public static final String ENDPOINT_KEYS = "/keys";

    /**
     * The name of the model attribute for {@link de.communicode.communikey.domain.Key} IDs.
     */
    public static final String MODEL_ID = "id";

    /**
     * The request parameter for the {@value ENDPOINT_LOGIN} endpoint to logout from a session.
     *
     * @since 0.2.0
     */
    public static final String REQUEST_PARAM_LOGIN_LOGOUT = "logout";

    /**
     * The endpoint mapping for {@link de.communicode.communikey.domain.Key} edit requests.
     */
    public static final String REQUEST_KEY_EDIT = ENDPOINT_KEYS + "/" + "{" + MODEL_ID + "}" + "/edit";

    /**
     * The endpoint mapping for {@link de.communicode.communikey.domain.Key} delete requests.
     */
    public static final String REQUEST_KEY_DELETE = ENDPOINT_KEYS + "/" + "{" + MODEL_ID + "}" + "/delete";

    /**
     * The endpoint mapping for new {@link de.communicode.communikey.domain.Key} requests.
     */
    public static final String REQUEST_KEY_NEW = ENDPOINT_KEYS + "/new";

    /**
     * The name of the table for {@link de.communicode.communikey.domain.Key} entities.
     *
     * @since 0.2.0
     */
    public static final String TABLE_KEYS = "\"keys\"";

    /**
     * The name of the table for the link table of {@link de.communicode.communikey.domain.UserGroup}- and {@link de.communicode.communikey.domain.User}
     * entities.
     *
     * @since 0.2.0
     */
    public static final String TABLE_LINK_USERS_TO_USER_GROUPS = "link_users_to_user_groups";

    /**
     * The name of the table for {@link de.communicode.communikey.domain.UserGroup} entities.
     *
     * @since 0.2.0
     */
    public static final String TABLE_USER_GROUPS = "user_groups";

    /**
     * The name of the table for {@link de.communicode.communikey.domain.User} entities.
     *
     * @since 0.2.0
     */
    public static final String TABLE_USERS = "users";

    /**
     * The name of the column for {@link de.communicode.communikey.domain.Key} entity IDs.
     *
     * @since 0.2.0
     */
    public static final String TABLE_KEYS_COLUMN_KEY_ID = "key_id";

    /**
     * The name of the column for {@link de.communicode.communikey.domain.User} entity IDs.
     *
     * @since 0.2.0
     */
    public static final String TABLE_USERS_COLUMN_USER_ID = "user_id";

    /**
     * The name of the column for the {@link de.communicode.communikey.domain.User} entity activation.
     *
     * @since 0.2.0
     */
    public static final String TABLE_USERS_COLUMN_ENABLED = "enabled";

    /**
     * The name of the column for the {@link de.communicode.communikey.domain.UserGroup} entity ID.
     *
     * @since 0.2.0
     */
    public static final String TABLE_USER_GROUPS_COLUMN_USER_GROUP_ID = "user_group_id";

    /**
     * The name of the column for the {@link de.communicode.communikey.domain.UserGroup} entity name.
     *
     * @since 0.2.0
     */
    public static final String TABLE_USER_GROUPS_COLUMN_NAME = "name";

    /**
     * The path to the template file for the HTTP status code {@code 404}.
     *
     * @since 0.2.0
     */
    public static final String TEMPLATE_HTTP_STATUS_CODE_404 = "error/404";

    /**
     * The path to the template file for the HTTP status code {@code 403}.
     *
     * @since 0.2.0
     */
    public static final String TEMPLATE_HTTP_STATUS_CODE_403 = "error/403";

    /**
     * The path to the template file to edit {@link de.communicode.communikey.domain.Key} entities.
     */
    public static final String TEMPLATE_KEY_EDIT = "keys-edit";

    /**
     * The path to the template file to create a new {@link de.communicode.communikey.domain.Key} entity.
     */
    public static final String TEMPLATE_KEY_NEW  = "keys-new";
}
