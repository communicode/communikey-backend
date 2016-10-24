/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2016
 */
package de.communicode.communikey.util;

import static de.communicode.communikey.config.CommunikeyConstants.ENDPOINT_HTTP_STATUS_CODE_404;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.joining;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * Provides static helper methods to handle constants for the communikey application.
 *
 * @author sgreb@communicode.de
 * @since 0.2.0
 */
public final class CommunikeyConstantsUtil {

    private CommunikeyConstantsUtil() {}

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
