/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2017
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
