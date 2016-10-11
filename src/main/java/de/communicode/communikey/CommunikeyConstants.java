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
    public static final String ENDPOINT_PASSWORDS = "passwords";

    public static final String MODEL_ID = "id";

    public static final String REQUEST_PASSWORD_EDIT = ENDPOINT_PASSWORDS + "/" + "{" + MODEL_ID + "}" + "/edit";
    public static final String REQUEST_PASSWORD_DELETE = ENDPOINT_PASSWORDS + "/" + "{" + MODEL_ID + "}" + "/delete";
    public static final String REQUEST_PASSWORD_NEW = ENDPOINT_PASSWORDS + "/new";

    public static final String TEMPLATE_PASSWORD_EDIT = "passwords-edit";
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
