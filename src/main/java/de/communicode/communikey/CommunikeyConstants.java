/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2016
*/
package de.communicode.communikey;

/**
 * Provides constants for the communikey application.
 *
 * @author sgreb@communicode.de
 * @since 0.1.0
 */
public class CommunikeyConstants {

    /**
     * The name of the login session ID cookie.
     *
     * @since 0.2.0
     */
    public static final String COOKIE_LOGIN_SESSION_ID = "JSESSIONID";

    /**
     * The endpoint for the {@code 403} HTTP status code.
     *
     * @since 0.2.0
     */
    public static final String ENDPOINT_HTTP_STATUS_CODE_403 = "/accessDenied";

    /**
     * The endpoint for {@link de.communicode.communikey.domain.Password} entities.
     */
    public static final String ENDPOINT_PASSWORDS = "/passwords";

    /**
     * The name of the model attribute for {@link de.communicode.communikey.domain.Password} IDs.
     */
    public static final String MODEL_ID = "id";

    /**
     * The endpoint mapping for {@link de.communicode.communikey.domain.Password} edit requests.
     */
    public static final String REQUEST_PASSWORD_EDIT = ENDPOINT_PASSWORDS + "/" + "{" + MODEL_ID + "}" + "/edit";

    /**
     * The endpoint mapping for {@link de.communicode.communikey.domain.Password} delete requests.
     */
    public static final String REQUEST_PASSWORD_DELETE = ENDPOINT_PASSWORDS + "/" + "{" + MODEL_ID + "}" + "/delete";

    /**
     * The endpoint mapping for new {@link de.communicode.communikey.domain.Password} requests.
     */
    public static final String REQUEST_PASSWORD_NEW = ENDPOINT_PASSWORDS + "/new";

    /**
     * The name of the table for {@link de.communicode.communikey.domain.User} entities.
     *
     * @since 0.2.0
     */
    public static final String TABLE_USERS = "users";

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
     * The path to the template file to edit {@link de.communicode.communikey.domain.Password} entities.
     */
    public static final String TEMPLATE_PASSWORD_EDIT = "passwords-edit";

    /**
     * The path to the template file to create a new {@link de.communicode.communikey.domain.Password} entity.
     */
    public static final String TEMPLATE_PASSWORD_NEW  = "passwords-new";

    /**
     * Prefixes the given {@code endpoint} with the {@code redirect:/} string.
     *
     * @param endpoint the endpoint to prepend the redirection to
     * @return the string with the prepended redirection string
     */
    public static String asRedirect(String endpoint) {
        return "redirect:/" + endpoint;
    }
}
