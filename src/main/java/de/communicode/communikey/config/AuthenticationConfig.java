/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2016
 */
package de.communicode.communikey.config;

import static de.communicode.communikey.type.RoleType.ROLE_USER;
import static java.util.Objects.requireNonNull;

import de.communicode.communikey.domain.User;
import de.communicode.communikey.exception.UserNotFoundException;
import de.communicode.communikey.service.UserRestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Configures the web based security for user authentications and requests to the REST API.
 * <p>
 *     Provides the {@link #userDetailsService} and {@link #passwordEncoder()} bean.
 *
 * @author sgreb@communicode.de
 * @since 0.2.0
 */
@Configuration
public class AuthenticationConfig extends GlobalAuthenticationConfigurerAdapter {

    private final UserRestService userRestService;

    @Autowired
    public AuthenticationConfig(UserRestService userRestService) {
        this.userRestService = requireNonNull(userRestService, "userRestService must not be null!");
    }

    @Override
    public void init(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService());
    }

    /**
     * The user details service bean which maps {@link User} entities to Springs {@link org.springframework.security.core.userdetails.User} entity.
     *
     * @return the user details service bean
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return (username) -> userRestService.getByUsername(username)
            .map(user -> new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), true, true, true, true,
                AuthorityUtils.createAuthorityList(ROLE_USER.name())))
            .orElseThrow(() -> new UserNotFoundException(username));
    }

    /**
     * The password encoder bean for password encryption.
     *
     * @return the {@link BCryptPasswordEncoder} bean
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}