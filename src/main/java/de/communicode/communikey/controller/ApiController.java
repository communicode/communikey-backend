/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2017
 */
package de.communicode.communikey.controller;

import static de.communicode.communikey.CommunikeyApplication.COMMUNIKEY_REST_API_VERSION;
import static de.communicode.communikey.controller.RequestMappings.API;
import static de.communicode.communikey.controller.RequestParameter.API_VALIDATE_USER_CREDENTIALS;
import static de.communicode.communikey.controller.RequestParameter.API_VERSION;
import static de.communicode.communikey.controller.RequestParameter.API_ME;
import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNull;

import com.google.common.collect.ImmutableMap;
import de.communicode.communikey.domain.Authority;
import de.communicode.communikey.domain.User;
import de.communicode.communikey.security.AuthoritiesConstants;
import de.communicode.communikey.security.SecurityUtils;
import de.communicode.communikey.service.UserService;
import de.communicode.communikey.service.payload.UserCredentialPayload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.Map;
import java.util.stream.Collectors;

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

    private final UserService userService;

    @Autowired
    public ApiController(UserService userService) {
        this.userService = requireNonNull(userService, "userService must not be null!");
    }

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
     * Checks if the {@link User} credentials are valid.
     *
     * <p>This endpoint is mapped to "{@value RequestMappings#API}".
     *
     * <p>Required parameter:
     * <ul>
     *     <li>{@value RequestParameter#API_VALIDATE_USER_CREDENTIALS}</li>
     * </ul>
     *
     * @param payload the payload containing the user credentials
     * @return {@code true} if the user credentials are valid, {@code false} otherwise
     */
    @PostMapping(params = API_VALIDATE_USER_CREDENTIALS)
    ResponseEntity isValidUserCredentials(@Valid @RequestBody UserCredentialPayload payload) {
        if (userService.validateCredentials(payload.getLogin(), payload.getPassword())) {
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * Outputs information about the current {@link User}.
     *
     * <p>This endpoint is mapped to "{@value RequestMappings#API}".
     *
     * <p>Required parameter:
     * <ul>
     *     <li>{@value RequestParameter#API_ME}</li>
     * </ul>
     *
     * @param me the request parameter to get the user object information
     * @return information about the current user
     */
    @GetMapping(params = API_ME)
    @Secured(value = {AuthoritiesConstants.ADMIN, AuthoritiesConstants.USER})
    ResponseEntity getUserInformation(@RequestParam(value = API_ME) String me) {
        User user = userService.getWithAuthoritiesByLogin(SecurityUtils.getCurrentUserLogin());
        Map response = ImmutableMap.builder()
            .put("login", user.getLogin())
            .put("email", user.getEmail())
            .put("firstName", user.getFirstName())
            .put("lastName", isNull(user.getLastName()) ? "" : user.getLastName())
            .put("privileged", SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN))
            .put("authorities", user.getAuthorities().stream().map(Authority::getName).collect(Collectors.toList()))
            .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
