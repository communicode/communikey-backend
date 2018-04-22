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
package de.communicode.communikey.security;

import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;

import de.communicode.communikey.domain.User;
import de.communicode.communikey.exception.UserNotActivatedException;
import de.communicode.communikey.exception.UserNotFoundException;
import de.communicode.communikey.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * The user details service which maps {@link User} entities to Springs {@link org.springframework.security.core.userdetails.User} entity.
 *
 * @author sgreb@communicode.de
 * @since 0.2.0
 */
@Component("userDetailsService")
public class RestUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public RestUserDetailsService(UserRepository userRepository) {
        this.userRepository = requireNonNull(userRepository, "userRepository must not be null!");
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(final String login) {
        Optional<User> userFromDatabase = ofNullable(userRepository.findOneWithAuthoritiesByLogin(login));
        return userFromDatabase.map(user -> {
            if (!user.isActivated()) {
                throw new UserNotActivatedException(login);
            }
            List<GrantedAuthority> grantedAuthorities = user.getAuthorities().stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getName()))
                .collect(Collectors.toList());
            return new org.springframework.security.core.userdetails.User(login, user.getPassword(), grantedAuthorities);
        }).orElseThrow(() -> new UserNotFoundException(login));
    }
}
