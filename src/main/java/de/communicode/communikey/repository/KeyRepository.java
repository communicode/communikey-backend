/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2016
 */
package de.communicode.communikey.repository;

import de.communicode.communikey.domain.Key;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;

/**
 * The repository for {@link Key} entities.
 *
 * @author sgreb@communicode.de
 * @since 0.1.0
 */
@Repository
public interface KeyRepository extends CrudRepository<Key, Long> {
    /**
     * Finds and returns the {@link Key} specified by the given {@code id}.
     *
     * @param id The ID of the {@link Key} to find
     * @return the {@link Key} with the given {@code id}
     */
    Key findOneById(long id);

    /**
     * Finds and returns the first {@link Key} found with the given creation {@link Timestamp}.
     *
     * @param creationTimestamp The creation {@link Timestamp} for a {@link Key} to find
     * @return the {@link Key} with the given creation {@link Timestamp}
     */
    Key findOneByCreationTimestamp(Timestamp creationTimestamp);
}
