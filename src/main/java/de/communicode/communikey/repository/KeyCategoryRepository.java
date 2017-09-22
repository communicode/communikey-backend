/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2017
 */
package de.communicode.communikey.repository;

import de.communicode.communikey.domain.KeyCategory;
import de.communicode.communikey.domain.UserGroup;
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
     * Finds all key category entities of the repository.
     *
     * @return a collection of found key category entities
     */
    @Override
    Set<KeyCategory> findAll();

    /**
     * Finds all key category entities where the parent key category is {@code null}.
     *
     * @return a collection of found key category entities
     */
    Set<KeyCategory> findAllByParentIsNull();

    /**
     * Finds all key category entities which are in the specified usergroup.
     *
     * @param userGroup the usergroup the key categories should contain
     * @return a collection of found key category entities
     */
    Set<KeyCategory> findAllByGroupsContains(UserGroup userGroup);
}
