/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2017
 */
package de.communicode.communikey.config;

import com.fasterxml.jackson.annotation.JsonView;
import de.communicode.communikey.domain.Authority;
import de.communicode.communikey.security.AuthoritiesConstants;
import de.communicode.communikey.service.view.AuthoritiesRestView;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.AbstractMappingJacksonResponseBodyAdvice;

import java.util.Optional;
import java.util.function.Predicate;

/**
 * Configuration for {@link JsonView} annotated entity attributes to allow {@link Authority} based JSON response (de)serialization.
 *
 * @author sgreb@communicode.de
 * @since 0.2.0
 */
@ControllerAdvice
public class RestViewConfiguration extends AbstractMappingJacksonResponseBodyAdvice {

    @Override
    protected void beforeBodyWriteInternal(MappingJacksonValue bodyContainer, MediaType contentType, MethodParameter returnType, ServerHttpRequest request,
                                           ServerHttpResponse response) {
        Predicate<String> checkForAdmin = authority -> authority.equals(AuthoritiesConstants.ADMIN);
        Predicate<String> checkForUser = authority -> authority.equals(AuthoritiesConstants.USER);
        Predicate<String> check = checkForAdmin.or(checkForUser);

        Class<?> viewClass =  Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .map(Authentication::getAuthorities)
                .filter(authorities -> authorities.stream()
                        .map(GrantedAuthority::getAuthority)
                        .anyMatch(check))
                .<Class<?>>map(isTrue -> AuthoritiesRestView.Admin.class)
                .orElse(AuthoritiesRestView.User.class);
        bodyContainer.setSerializationView(viewClass);
    }
}