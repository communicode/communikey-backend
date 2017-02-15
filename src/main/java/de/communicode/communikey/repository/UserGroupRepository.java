/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2017
 */
package de.communicode.communikey.repository;

import de.communicode.communikey.domain.UserGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * A repository for {@link UserGroup}s.
 *
 * @author sgreb@communicode.de
 * @since 0.2.0
 */
@Repository
public interface UserGroupRepository extends JpaRepository<UserGroup, Long> {

    /**
     * Finds the user group with the specified name.
     *
     * @param name the name of the user group to find
     * @return the user group entity
     */
    UserGroup findOneByName(String name);
}
