/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2016
 */
package de.communicode.communikey.service;

import static java.util.Objects.requireNonNull;

import de.communicode.communikey.domain.User;
import de.communicode.communikey.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * The user details service which maps {@link User} entities to Springs {@link org.springframework.security.core.userdetails.User} entity.
 *
 * @author sgreb@communicode.de
 * @since 0.2.0
 */
@Service
public class UserDetailsRestService implements UserDetailsService {

    private final UserRestService userService;

    @Autowired
    public UserDetailsRestService(UserRestService userService) {
        this.userService = requireNonNull(userService, "userService must not be null!");
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UserNotFoundException {
        return Optional.of(userService.getByEmail(email)).orElseThrow(() -> new UserNotFoundException(email));
    }
}