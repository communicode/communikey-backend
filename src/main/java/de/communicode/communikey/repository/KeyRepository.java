/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2016
 */
package de.communicode.communikey.repository;

import de.communicode.communikey.domain.Key;
import de.communicode.communikey.domain.KeyCategory;
import de.communicode.communikey.domain.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.Set;

/**
 * A repository for {@link Key} entities.
 *
 * @author sgreb@communicode.de
 * @since 0.1.0
 */
@Repository
public interface KeyRepository extends CrudRepository<Key, Long> {

    /**
     * Finds all key entities with the specified {@link KeyCategory} ID.
     *
     * @param keyCategoryId the ID from which all key entities are to be found
     * @return a collection of found key entities
     * @since 0.2.0
     */
    Set<Key> findAllByCategory(long keyCategoryId);

    /**
     * Finds all key entities with the specified creation timestamp.
     *
     * @param creationTimestamp the creation timestamp from which all key entities are to be found
     * @return a collection of found key entities
     */
    Set<Key> findAllByCreationTimestamp(Timestamp creationTimestamp);

    /**
     * Finds all key entities created by the specified {@link User} ID.
     *
     * @param creatorUserId the ID of the user to find all created key entities of
     * @return a collection of found key entities
     * @since 0.2.0
     */
    Set<Key> findAllByCreatorId(long creatorUserId);

    /**
     * Finds all key entities with the specified value.
     *
     * @param value the value from which all key entities are to be found
     * @return a collection of found key entities
     * @since 0.2.0
     */
    Set<Key> findAllByValue(String value);
}