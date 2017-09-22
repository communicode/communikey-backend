/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2017
 */
package de.communicode.communikey.repository;

import de.communicode.communikey.domain.Key;
import de.communicode.communikey.domain.KeyCategory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

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
     * Finds all key entities of the repository.
     *
     * @return a collection of found key entities
     */
    @Override
    Set<Key> findAll();

    /**
     * Finds all key entities that are in the specified key category.
     *
     * @param keyCategory the key category the keys should be in
     * @return a collection of found key entities
     */
    Set<Key> findAllByCategory(KeyCategory keyCategory);
}
