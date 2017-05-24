/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2017
 */
package de.communicode.communikey.controller;

/**
 * Provides request parameter constants.
 *
 * @author sgreb@communicode.de
 * @since 0.3.0
 */
public final class RequestParameter {

    /**
     * The request parameter for the {@value RequestMappings#API} endpoint to get the version of the REST API.
     */
    public static final String API_VERSION = "version";

    /**
     * The request parameter for the {@value RequestMappings#API} endpoint to validate a the credentials of a user.
     *
     * @since 0.4.0
     */
    public static final String API_VALIDATE_USER_CREDENTIALS = "validate_user";

    /**
     * The request parameter for the {@value RequestMappings#API} endpoint to get information about the current user.
     */
    public static final String API_ME = "me";

    private RequestParameter() {}
}
