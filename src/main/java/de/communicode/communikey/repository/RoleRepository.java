/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2016
 */

package de.communicode.communikey.repository;

import de.communicode.communikey.domain.Role;
import org.springframework.data.repository.CrudRepository;

/**
 * A repository for {@link Role} entities.
 *
 * @author sgreb@communicode.de
 * @since 0.2.0
 */
public interface RoleRepository extends CrudRepository<Role, Long> {

    /**
     * Finds the role entity with the specified name.
     *
     * @param name the name of the role entity to find
     * @return the role entity if found
     */
    Role findOneByName(String name);

    /**
     * Deletes the specified role entity.
     *
     * @param role the role entity to delete
     */
    @Override
    void delete(Role role);
}
