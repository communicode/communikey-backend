/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2017
 */
package de.communicode.communikey.controller;

import static de.communicode.communikey.controller.PathVariables.JOB_TOKEN;
import static de.communicode.communikey.controller.PathVariables.KEYCATEGORY_ID;
import static de.communicode.communikey.controller.PathVariables.KEY_ID;

import de.communicode.communikey.domain.Authority;
import de.communicode.communikey.domain.Key;
import de.communicode.communikey.domain.KeyCategory;
import de.communicode.communikey.domain.User;
import de.communicode.communikey.domain.UserGroup;
import de.communicode.communikey.domain.EncryptionJob;

/**
 * Provides request mapping constants.
 *
 * @author sgreb@communicode.de
 * @author dvonderbey@communicode.de
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
     * The request parameter for a {@link KeyCategory} entity Hashid.
     */
    public static final String KEY_CATEGORIES_HASHID = "/{" + KEYCATEGORY_ID + "}";

    /**
     * The request mapping for {@link KeyCategory} children.
     */
    public static final String KEY_CATEGORY_CHILDREN = KEY_CATEGORIES_HASHID + "/children";

    /**
     * The request mapping for {@link KeyCategory} user groups.
     *
     * @since 0.3.0
     */
    public static final String KEY_CATEGORY_GROUPS = KEY_CATEGORIES_HASHID + "/groups";

    /**
     * The request mapping for the {@link KeyCategory} {@link Key}s.
     */
    public static final String KEY_CATEGORY_KEYS = KEY_CATEGORIES_HASHID + "/keys";

    /**
     * The request mapping for the responsible {@link KeyCategory} user.
     */
    public static final String KEY_CATEGORY_RESPONSIBLE = KEY_CATEGORIES_HASHID + "/responsible";

    /**
     * The request parameter for a {@link Key} entity Hashid.
     */
    public static final String KEY_HASHID = "/{" + KEY_ID + "}";

    /**
     * The request parameter for the subscribers of a {@link Key} entity Hashid.
     */
    public static final String KEY_SUBSCRIBERS = KEY_HASHID + "/subscribers";

    /**
     * The request parameter for a {@link Key} entity userEncryptedPassword.
     */
    public static final String KEY_ENCRYPTED_PASSWORD = KEY_HASHID + "/password";

    /**
     * The endpoint for {@link User} entities.
     */
    public static final String USERS = API + "/users";

    /**
     * The endpoint for {@link UserGroup}s.
     */
    public static final String USER_GROUPS = API + "/groups";

    /**
     * The request parameter for a {@link UserGroup} ID.
     */
    public static final String USER_GROUPS_ID = "/{userGroupId}";

    /**
     * The endpoint for {@link UserGroup}s.
     */
    public static final String USER_GROUPS_USERS = USER_GROUPS_ID + "/users";

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

    /**
     * The endpoint to reset a {@link User} publicKey.
     */
    public static final String USERS_PUBLICKEY_RESET = "/reset_publickey";

    /**
     * The endpoint for {@link Authority} entities.
     *
     * @since 0.3.0
     */
    public static final String AUTHORITIES = API + "/authorities";

    /**
     * The request parameter for a {@link Authority} name.
     *
     * @since 0.3.0
     */
    public static final String AUTHORITIES_NAME = "/{authorityName}";

    /**
     * The messaging queue endpoint.
     *
     * @since 0.15.0
     */
    public static final String QUEUE = "/queue";

    /**
     * The user messaging reply endpoint.
     *
     * @since 0.15.0
     */
    public static final String QUEUE_REPLY = QUEUE + "/reply";

    /**
     * The user messaging reply endpoint.
     *
     * @since 0.15.0
     */
    public static final String QUEUE_JOBS = QUEUE + "/encryption/jobs";

    /**
     * The user messaging update endpoint for key entities.
     *
     * @since 0.15.0
     */
    public static final String QUEUE_UPDATES = QUEUE + "/updates";

    /**
     * The user messaging update endpoint for key entities.
     *
     * @since 0.15.0
     */
    public static final String QUEUE_UPDATES_KEYS = QUEUE_UPDATES + "/keys";

    /**
     * The user messaging update endpoint for deleted key entities.
     *
     * @since 0.15.0
     */
    public static final String QUEUE_UPDATES_KEYS_DELETE = QUEUE_UPDATES_KEYS + "/delete";

    /**
     * The user messaging update endpoint for key entities.
     *
     * @since 0.15.0
     */
    public static final String QUEUE_UPDATES_CATEGORIES = QUEUE_UPDATES + "/categories";

    /**
     * The user messaging update endpoint for deleted key entities.
     *
     * @since 0.15.0
     */
    public static final String QUEUE_UPDATES_CATEGORIES_DELETE = QUEUE_UPDATES_CATEGORIES + "/delete";

    /**
     * The user messaging update endpoint for key entities.
     *
     * @since 0.15.0
     */
    public static final String QUEUE_UPDATES_GROUPS = QUEUE_UPDATES + "/groups";

    /**
     * The user messaging update endpoint for deleted key entities.
     *
     * @since 0.15.0
     */
    public static final String QUEUE_UPDATES_GROUPS_DELETE = QUEUE_UPDATES_GROUPS + "/delete";

    /**
     * The user messaging update endpoint for key entities.
     *
     * @since 0.15.0
     */
    public static final String QUEUE_UPDATES_USERS = QUEUE_UPDATES + "/users";

    /**
     * The user messaging update endpoint for deleted key entities.
     *
     * @since 0.15.0
     */
    public static final String QUEUE_UPDATES_USERS_DELETE = QUEUE_UPDATES_USERS + "/delete";

    /**
     * The user messaging reply endpoint.
     *
     * @since 0.15.0
     */
    public static final String QUEUE_JOB_ABORT = QUEUE + "/encryption/jobs/aborts";

    /**
     * The endpoint for {@link EncryptionJob} entities.
     */
    public static final String JOBS = "/jobs";

    /**
     * The request parameter for an {@link EncryptionJob} token.
     */
    public static final String FULFILL = "/{" + JOB_TOKEN + "}";

    /**
     * The endpoint to fulfill an {@link EncryptionJob}.
     */
    public static final String JOBS_FULFILL = JOBS + FULFILL;

    private RequestMappings() {}
}
