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
     * The endpoint for {@link de.communicode.communikey.domain.Password} entities.
     */
    public static final String ENDPOINT_PASSWORDS = "/passwords";

    /**
     * The name of the model attribute for {@link de.communicode.communikey.domain.Password} IDs.
     */
    public static final String MODEL_ID = "id";

    /**
     * The request parameter for the {@value ENDPOINT_LOGIN} endpoint to logout from a session.
     *
     * @since 0.2.0
     */
    public static final String REQUEST_PARAM_LOGIN_LOGOUT = "logout";

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
        return "redirect:" + endpoint;
    }

    /**
     * Suffixes the given {@code parameters} with the ampersand character ({@code &}) to the given {@code endpoint}.
     *
     * @param endpoint the endpoint to append the {@code parameters} to
     * @param parameters the parameters to be appended to the {@code endpoint}
     * @return the endpoint string with the appended parameters
     * @since 0.2.0
     */
    public static String withParameters(String endpoint, String... parameters) {
        if (parameters.length == 1) {
            return endpoint + "?" + parameters[0];
        } else {
            StringBuilder sb = new StringBuilder(endpoint);
            for (String parameter : parameters) {
                sb.append("?").append(parameter);
            }
            return sb.toString();
        }
    }
}
