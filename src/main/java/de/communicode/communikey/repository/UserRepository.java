/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2016
*/
package de.communicode.communikey.repository;

import org.springframework.data.repository.CrudRepository;
import de.communicode.communikey.domain.User;
import org.springframework.stereotype.Repository;

/**
 * The repository for {@link User} entities.
 *
 * @author sgreb@communicode.de
 * @since 0.2.0
 */
@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    /**
     * Finds and returns the {@link User} specified by the given {@code id}.
     *
     * @param id The ID of the {@link User} to find
     * @return the {@link User} with the given {@code id}
     */
    User findOneById(long id);

    /**
     * Finds and returns the first {@link User} found with the given {@code username}.
     *
     * @param username The {@code username} for a {@link User} to find
     * @return the {@link User} with the given {@code username}
     */
    User findOneByUsername(String username);
}
