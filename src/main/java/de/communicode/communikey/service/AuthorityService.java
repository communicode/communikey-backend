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
package de.communicode.communikey.service;

import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;

import de.communicode.communikey.domain.Authority;
import de.communicode.communikey.exception.AuthorityNotFoundException;
import de.communicode.communikey.repository.AuthorityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

/**
 * The REST API service to process {@link Authority}s via a {@link AuthorityRepository}.
 *
 * @author sgreb@communicode.de
 * @since 0.3.0
 */
@Service
public class AuthorityService {

    private final AuthorityRepository authorityRepository;

    @Autowired
    public AuthorityService(AuthorityRepository authorityRepository) {
        this.authorityRepository = requireNonNull(authorityRepository, "authorityRepository must not be null!");
    }

    /**
     * Gets the authority with the specified name.
     *
     * @param authorityName the name of the authority to get
     * @return the authority
     * @throws AuthorityNotFoundException if the authority with the specified name has not been found
     */
    public Authority get(String authorityName) throws AuthorityNotFoundException {
        return validate(authorityName);
    }

    /**
     * Gets all authorities.
     *
     * @return a collection of all authorities
     */
    public Set<Authority> getAll() {
        return new HashSet<>(authorityRepository.findAll());
    }

    /**
     * Validates a authority.
     *
     * @param authorityName the name of the authority to validate
     * @return the key if validated
     * @throws AuthorityNotFoundException if the authority with the specified name has not been found
     */
    public Authority validate(String authorityName) throws AuthorityNotFoundException {
        return ofNullable(authorityRepository.findOneByName(authorityName)).orElseThrow(() -> new AuthorityNotFoundException(authorityName));
    }
}
