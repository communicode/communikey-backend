/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2016
 */
package de.communicode.communikey.controller;

import static de.communicode.communikey.controller.RequestMappings.HTTP_STATUS_CODE_403;
import static de.communicode.communikey.controller.RequestMappings.LOGIN;
import static de.communicode.communikey.controller.RequestMappings.LOGOUT;
import static de.communicode.communikey.util.CommunikeyConstantsUtil.asRedirect;

import de.communicode.communikey.domain.User;
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
     * Endpoint to the HTTP status code 403 error page for unprivileged {@link User}.
     * <p>
     *     Mapped to the "{@value RequestMappings#HTTP_STATUS_CODE_403}" endpoint.
     *
     * @return the string to the endpoint
     */
    @GetMapping(value = HTTP_STATUS_CODE_403)
    public String accessDenied() {
        return HTTP_STATUS_CODE_403;
    }

    /**
     * Endpoint to the {@link User} login.
     * <p>
     *     Mapped to the "{@value RequestMappings#LOGIN}" endpoint.
     *
     * @return the string to the endpoint
     */
    @GetMapping(value = LOGIN)
    public String login() {
        return LOGIN;
    }

    /**
     * Endpoint to the {@link User} logout.
     * <p>
     *     Redirects to the {@value RequestMappings#LOGOUT} endpoint.
     *
     * @param request the HTTP request information
     * @param response the HTTP response information
     * @return the string to the endpoint redirection
     */
    @GetMapping(value = LOGOUT)
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return asRedirect(LOGOUT);
    }
}
