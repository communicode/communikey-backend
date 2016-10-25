/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2016
 */
package de.communicode.communikey.repository;

import de.communicode.communikey.domain.UserGroup;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * The repository for {@link UserGroup} entities.
 *
 * @author sgreb@communicode.de
 * @since 0.2.0
 */
@Repository
public interface UserGroupRepository extends CrudRepository<UserGroup, Long> {
    /**
     * Finds and returns the {@link UserGroup} specified by the given {@code id}.
     *
     * @param id The ID of the {@link UserGroup} to find
     * @return the {@link UserGroup} with the given {@code id}
     */
    UserGroup findOneById(long id);

    /**
     * Finds and returns the first {@link UserGroup} found with the given {@code username}.
     *
     * @param name The {@code name} of a {@link UserGroup} to find
     * @return the {@link UserGroup} with the given {@code name}
     */
    UserGroup findOneByName(String name);
}
