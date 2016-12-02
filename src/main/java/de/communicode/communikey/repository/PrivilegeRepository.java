/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2016
 */

package de.communicode.communikey.repository;

import de.communicode.communikey.domain.Privilege;
import org.springframework.data.repository.CrudRepository;

/**
 * A repository for {@link Privilege} entities.
 *
 * @author sgreb@communicode.de
 * @since 0.2.0
 */
public interface PrivilegeRepository extends CrudRepository<Privilege, Long> {

    /**
     * Finds the privilege entity with the specified name.
     *
     * @param name the name of the privilege entity to find
     * @return the privilege entity if found
     */
    Privilege findOneByName(String name);

    /**
     * Deletes the specified privilege entity.
     *
     * @param privilege the privilege entity to delete
     */
    @Override
    void delete(Privilege privilege);

}
