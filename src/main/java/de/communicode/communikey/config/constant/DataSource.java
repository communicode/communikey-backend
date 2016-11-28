/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2016
 */
package de.communicode.communikey.config.constant;

import de.communicode.communikey.domain.Key;
import de.communicode.communikey.domain.KeyCategory;
import de.communicode.communikey.domain.User;
import de.communicode.communikey.domain.UserGroup;

/**
 * Provides data source constants.
 *
 * @author sgreb@communicode.de
 * @since 0.2.0
 */
public final class DataSource {

    private DataSource() {}

    /**
     * The name of the table for {@link KeyCategory} entities.
     */
    public static final String CATEGORIES = "categories";

    /**
     * The name of the column for creator {@link User} entity IDs.
     */
    public static final String CREATOR_USER_ID = "creator_user_id";

    /**
     * The name of the column for {@link KeyCategory} entity IDs.
     */
    public static final String KEY_CATEGORY_ID = "key_category_id";

    /**
     * The name of the column for {@link Key} entity IDs.
     */
    public static final String KEY_ID = "key_id";

    /**
     * The name of the table for {@link Key} entities.
     */
    public static final String KEYS = "\"keys\"";

    /**
     * The name of the column for responsible {@link User} entity IDs.
     */
    public static final String RESPONSIBLE_USER_ID = "responsible_user_id";

    /**
     * The name of the column for {@link User} entity activation statuses.
     */
    public static final String USER_ENABLED = "enabled";

    /**
     * The name of the column for {@link UserGroup} entity IDs.
     */
    public static final String USER_GROUP_ID = "user_group_id";

    /**
     * The name of the column for {@link UserGroup} entity names.
     */
    public static final String USER_GROUP_NAME = "user_group_name";

    /**
     * The name of the table for {@link UserGroup} entities.
     */
    public static final String USER_GROUPS = "user_groups";

    /**
     * The name of the column for {@link User} entity IDs.
     */
    public static final String USER_ID = "user_id";

    /**
     * The name of the table for {@link User} entities.
     */
    public static final String USERS = "users";

    /**
     * The name of the table for the relation of {@link User}- to {@link UserGroup} entities.
     */
    public static final String USERS_TO_GROUPS = "users_to_groups";

}
