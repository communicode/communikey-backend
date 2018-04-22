/*
 * This file is part of communikey.
 * Copyright (C) 2016-2018  communicode AG <communicode.de>
 *
 * communikey is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
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
 * @author tkabus@communicode.de
 * @since 0.2.0
 */
@ControllerAdvice
public class RestViewConfiguration extends AbstractMappingJacksonResponseBodyAdvice {

    @Override
    protected void beforeBodyWriteInternal(MappingJacksonValue bodyContainer, MediaType contentType, MethodParameter returnType, ServerHttpRequest request,
                                           ServerHttpResponse response) {
        Predicate<String> checkForAdmin = authority -> authority.equals(AuthoritiesConstants.ADMIN);

        Class<?> viewClass = Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .map(Authentication::getAuthorities)
                .filter(authorities -> authorities.stream()
                        .map(GrantedAuthority::getAuthority)
                        .anyMatch(checkForAdmin))
                .<Class<?>>map(isTrue -> AuthoritiesRestView.Admin.class)
                .orElse(AuthoritiesRestView.User.class);
        bodyContainer.setSerializationView(viewClass);
    }
}
