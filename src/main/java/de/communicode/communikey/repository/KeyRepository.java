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
import de.communicode.communikey.exception.KeyNotFoundException;
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
     * Finds all {@link Key} entities with the given {@link KeyCategory}.
     *
     * @param category the {@link KeyCategory} from which all {@link Key} are to be found
     * @return a collection of found {@link Key} entities
     * @since 0.2.0
     */
    Set<Key> findAllByCategory(KeyCategory category);

    /**
     * Finds all {@link Key} entities with the given creation {@link Timestamp}.
     *
     * @param creationTimestamp the creation {@link Timestamp} from which all {@link Key} are to be found
     * @return a collection of found {@link Key} entities
     */
    Set<Key> findAllByCreationTimestamp(Timestamp creationTimestamp);

    /**
     * Finds all {@link Key} entities created by the given {@link User}.
     *
     * @param creator the {@link User} to find all created {@link Key} entities of
     * @return a collection of found {@link Key} entities
     * @since 0.2.0
     */
    Set<Key> findAllByCreator(User creator);

    /**
     * Finds all {@link Key} entities with the given {@code value}.
     *
     * @param value the value from which all {@link Key} are to be found
     * @return a collection of found {@link Key} entities
     * @since 0.2.0
     */
    Set<Key> findAllByValue(String value);

    /**
     * Finds the {@link Key} entity with the given {@code id}.
     *
     * @param id the ID of the {@link Key} to find
     * @return the {@link Key} entity if found
     * @throws KeyNotFoundException if no {@link Key} entity with the given {@code id} has been found
     */
    Key findOneById(long id) throws KeyNotFoundException;
}
