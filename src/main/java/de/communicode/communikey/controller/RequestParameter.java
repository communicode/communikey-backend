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
     * The request parameter for the {@value RequestMappings#API} endpoint to get the privilege status of the current user.
     */
    public static final String API_PRIVILEGED = "privileged";

    /**
     * The request parameter for the {@value RequestMappings#API} endpoint to get the version of the REST API.
     */
    public static final String API_VERSION = "version";

    private RequestParameter() {}
}
