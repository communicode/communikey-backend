/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2016
 */
package de.communicode.communikey.controller;

import static de.communicode.communikey.CommunikeyConstants.ENDPOINT_HTTP_STATUS_CODE_403;
import static de.communicode.communikey.CommunikeyConstants.ENDPOINT_LOGIN;
import static de.communicode.communikey.CommunikeyConstants.ENDPOINT_LOGOUT;
import static de.communicode.communikey.CommunikeyConstants.REQUEST_PARAM_LOGIN_LOGOUT;
import static de.communicode.communikey.CommunikeyConstants.TEMPLATE_HTTP_STATUS_CODE_403;
import static de.communicode.communikey.CommunikeyConstants.asRedirect;
import static de.communicode.communikey.CommunikeyConstants.withParameters;

import de.communicode.communikey.CommunikeyConstants;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * The controller for all HTTP session related endpoints.
 *
 * @author sgreb@communicode.de
 * @since 0.2.0
 */
@Controller
public class SessionController {

    /**
     * Endpoint to the HTTP status code 403 error page for unprivileged {@link de.communicode.communikey.domain.User}.
     *
     * @return the string to the endpoint
     */
    @GetMapping(value = ENDPOINT_HTTP_STATUS_CODE_403)
    public String accessDenied() {
        return TEMPLATE_HTTP_STATUS_CODE_403;
    }

    /**
     * Endpoint to the {@link de.communicode.communikey.domain.User} login.
     *
     * @return the string to the endpoint
     */
    @GetMapping(value = ENDPOINT_LOGIN)
    public String login() {
        return ENDPOINT_LOGIN;
    }

    /**
     * Endpoint to logout a {@link de.communicode.communikey.domain.User}.
     *
     * <p>
     *     Redirects to the {@value CommunikeyConstants#ENDPOINT_LOGIN} endpoint.
     *
     * @param request the HTTP request information
     * @param response the HTTP response information
     * @return the string to the endpoint redirection
     */
    @GetMapping(value = ENDPOINT_LOGOUT)
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return asRedirect(withParameters(ENDPOINT_LOGIN, REQUEST_PARAM_LOGIN_LOGOUT));
    }
}
