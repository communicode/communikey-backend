/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2017
 */
package de.communicode.communikey.controller;

import static de.communicode.communikey.CommunikeyApplication.COMMUNIKEY_REST_API_VERSION;
import static de.communicode.communikey.controller.RequestMappings.API;
import static de.communicode.communikey.controller.RequestParameter.API_PRIVILEGED;
import static de.communicode.communikey.controller.RequestParameter.API_VERSION;

import com.google.common.collect.ImmutableMap;
import de.communicode.communikey.domain.User;
import de.communicode.communikey.security.AuthoritiesConstants;
import de.communicode.communikey.security.SecurityUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * The REST API controller to provide information about the communikey API.
 * <p>Mapped to the "{@value RequestMappings#API}" endpoint.
 *
 * @author sgreb@communicode.de
 * @since 0.3.0
 */
@RestController
@RequestMapping(API)
public class ApiController {

    /**
     * Gets the version of the communikey REST API.
     *
     * <p>This endpoint is mapped to "{@value RequestMappings#API}".
     *
     * <p>Required parameter:
     * <ul>
     *     <li>{@value RequestParameter#API_VERSION}</li>
     * </ul>
     *
     * @param version the request parameter to get the version
     * @return the version of the communikey REST API as response entity
     */
    @GetMapping(params = API_VERSION)
    ResponseEntity<Map<String, String>> getRestApiVersion(@RequestParam(value = API_VERSION) String version) {
        return new ResponseEntity<>(ImmutableMap.of(API_VERSION, COMMUNIKEY_REST_API_VERSION), HttpStatus.OK);
    }

    /**
     * Checks if the current {@link User} is privileged.
     *
     * <p>This endpoint is mapped to "{@value RequestMappings#API}".
     *
     * <p>Required parameter:
     * <ul>
     *     <li>{@value RequestParameter#API_PRIVILEGED}</li>
     * </ul>
     *
     * @param privileged the request parameter to get the privilege status
     * @return {@code true} if the current user is privileged, {@code false} otherwise
     */
    @GetMapping(params = API_PRIVILEGED)
    ResponseEntity isUserPrivileged(@RequestParam(value = API_PRIVILEGED) String privileged) {
        return new ResponseEntity<>(ImmutableMap.of(API_PRIVILEGED, SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN)), HttpStatus.OK);
    }
}