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

import java.util.Optional;

/**
 * A repository for {@link UserGroup} entities.
 *
 * @author sgreb@communicode.de
 * @since 0.2.0
 */
@Repository
public interface UserGroupRepository extends CrudRepository<UserGroup, Long> {

    /**
     * Finds the user group entity with the specified name.
     *
     * @param userGroupName the name of the user group entity to find
     * @return the user group entity if found, {@link Optional#EMPTY} otherwise
     */
    Optional<UserGroup> findOneByName(String userGroupName);
}
