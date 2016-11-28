/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2016
 */
package de.communicode.communikey.repository;

import de.communicode.communikey.domain.KeyCategory;
import de.communicode.communikey.domain.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

/**
 * A repository for {@link KeyCategory} entities.
 *
 * @author sgreb@communicode.de
 * @since 0.2.0
 */
@Repository
public interface KeyCategoryRepository extends CrudRepository<KeyCategory, Long> {

    /**
     * Finds all key category entities created by the specified {@link User} ID.
     *
     * @param creatorUserId the ID of the user to get all created key categories entities of
     * @return a collection of found key category entities
     */
    Set<KeyCategory> findAllByCreator(long creatorUserId);

    /**
     * Finds all key category entities with the specified name.
     *
     * @param name the name of the key category entities to find
     * @return a collection of found key category entities
     */
    Set<KeyCategory> findAllByName(String name);

    /**
     * Finds all key category entities owned by the specified parent {@link KeyCategory} ID.
     *
     * @param keyCategoryId the parent key category to get all child key category entities of
     * @return a collection of found key category entities
     */
    Set<KeyCategory> findAllByParent(long keyCategoryId);

    /**
     * Finds all key category entities the specified {@link User} is responsible for.
     *
     * @param responsibleUserId the ID of the responsible user to get all key category entities of
     * @return a collection of found key category entities
     */
    Set<KeyCategory> findAllByResponsible(long responsibleUserId);
}
