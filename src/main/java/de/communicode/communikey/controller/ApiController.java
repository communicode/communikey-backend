/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2017
 */
package de.communicode.communikey.controller;

import static de.communicode.communikey.CommunikeyApplication.COMMUNIKEY_REST_API_VERSION;
import static de.communicode.communikey.config.SecurityConfig.APP_ID;
import static de.communicode.communikey.controller.RequestMappings.API;
import static de.communicode.communikey.controller.RequestParameter.API_AUTHORIZE;
import static de.communicode.communikey.controller.RequestParameter.API_VERSION;
import static de.communicode.communikey.controller.RequestParameter.API_ME;
import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNull;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
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
    private final UserDetailsService restUserDetailsService;

    private final AuthorizationServerTokenServices defaultAuthorizationServerTokenServices;


    @Autowired
    public ApiController(UserService userService,
                         AuthorizationServerTokenServices defaultAuthorizationServerTokenServices,
                         UserDetailsService restUserDetailsService) {
        this.userService = requireNonNull(userService, "userService must not be null!");
        this.restUserDetailsService = requireNonNull(restUserDetailsService, "restUserDetailsService must not be null!");
        this.defaultAuthorizationServerTokenServices = requireNonNull(
            defaultAuthorizationServerTokenServices, "defaultAuthorizationServerTokenServices must not be null!");
    }

    /**
     * Authorizes a user if the specified {@link User} credentials are valid and returns a OAuth2 access token.
     *
     * <p>This endpoint is mapped to "{@value RequestMappings#API}".
     *
     * <p>Required parameter:
     * <ul>
     *     <li>{@value RequestParameter#API_AUTHORIZE}</li>
     * </ul>
     *
     * @param payload the payload containing the user credentials
     * @return a JSON payload of {@link OAuth2AccessToken} attributes if the user credentials are valid, a {@link HttpStatus#UNAUTHORIZED} otherwise
     * @since 0.8.0
     */
    @PostMapping(params = API_AUTHORIZE)
    ResponseEntity authorizeOAuth2(@Valid @RequestBody UserCredentialPayload payload) {
        if (!userService.validateCredentials(payload.getLogin(), payload.getPassword())) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

        User user = userService.validate(payload.getLogin());
        UserDetails userDetails = restUserDetailsService.loadUserByUsername(payload.getLogin());

        Map<String, String> authorizationParameters = ImmutableMap.of("scope", "read,write", "client_id", APP_ID, "grant", "password");
        Set<GrantedAuthority> authorities = user.getAuthorities().stream()
                .map(Authority::getName)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
        Set<String> responseType = ImmutableSet.of("token");
        Set<String> scopes = ImmutableSet.of("read", "write");

        OAuth2Request authorizationRequest = new OAuth2Request(authorizationParameters, APP_ID, authorities, true, scopes, null, "", responseType, null);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
        OAuth2Authentication authenticationRequest = new OAuth2Authentication(authorizationRequest, authenticationToken);
        authenticationRequest.setAuthenticated(true);
        OAuth2AccessToken accessToken = defaultAuthorizationServerTokenServices.createAccessToken(authenticationRequest);

        return new ResponseEntity<>(ImmutableMap.builder()
            .put(OAuth2AccessToken.ACCESS_TOKEN, accessToken.getValue())
            .put(OAuth2AccessToken.TOKEN_TYPE, accessToken.getTokenType())
            .put(OAuth2AccessToken.EXPIRES_IN, accessToken.getExpiresIn())
            .build(),
            HttpStatus.OK);
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
            .put("publicKey", isNull(user.getPublicKey()) ? "" : user.getPublicKey())
            .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
