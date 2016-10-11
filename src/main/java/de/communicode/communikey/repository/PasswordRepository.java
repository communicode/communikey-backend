/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2016
*/
package de.communicode.communikey.repository;

import de.communicode.communikey.domain.Password;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;

/**
 * The repository for {@link Password} entities.
 *
 * @author sgreb@communicode.de
 * @since 0.1.0
 */
@Repository
public interface PasswordRepository extends CrudRepository<Password, Long> {
    /**
     * Finds and returns the {@link Password} specified by the given {@code id}.
     *
     * @param id The ID of the {@link Password} to find
     * @return the {@link Password} with the given {@code id}
     */
    Password findOneById(long id);

    /**
     * Finds and returns the first {@link Password} found with the given creation {@link Timestamp}.
     *
     * @param creationTimestamp The creation {@link Timestamp} for a {@link Password} to find
     * @return the {@link Password} with the given creation {@link Timestamp}
     */
    Password findOneByCreationTimestamp(Timestamp creationTimestamp);
}
