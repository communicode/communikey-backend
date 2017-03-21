/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2017
 */
package de.communicode.communikey.repository;

import de.communicode.communikey.domain.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * A repository for {@link Authority} entities.
 *
 * @author sgreb@communicode.de
 * @since 0.2.0
 */
public interface AuthorityRepository extends JpaRepository<Authority, String> {
    /**
     * Finds the authority entity with the specified name.
     *
     * @param name the name of the authority to find
     * @return the found authority entity, {@code null} otherwise
     * @since 0.3.0
     */
    Authority findOneByName(String name);
}
