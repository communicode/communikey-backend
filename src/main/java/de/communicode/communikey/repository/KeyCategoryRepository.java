/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2016
 */
package de.communicode.communikey.repository;

import de.communicode.communikey.domain.KeyCategory;
import de.communicode.communikey.domain.User;
import de.communicode.communikey.exception.KeyCategoryNotFoundException;
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
     * Finds all {@link KeyCategory} entities created by the given {@link User}.
     *
     * @param creator the {@link User} to get all created {@link KeyCategory} entities of
     * @return a collection of all found {@link KeyCategory} entities
     */
    Set<KeyCategory> findAllByCreator(User creator);

    /**
     * Finds all {@link KeyCategory} entities with the given {@code name}.
     *
     * @param name the name of the {@link KeyCategory} entities to find
     * @return a collection of all found {@link KeyCategory} entities
     */
    Set<KeyCategory> findAllByName(String name);

    /**
     * Finds all {@link KeyCategory} entities owned by the given parent {@link KeyCategory}.
     *
     * @param keyCategory the parent {@link KeyCategory} to get all child {@link KeyCategory} entities of
     * @return a collection of all found {@link KeyCategory} entities
     */
    Set<KeyCategory> findAllByParent(KeyCategory keyCategory);

    /**
     * Finds  all {@link KeyCategory} entities the given {@link User} is responsible for.
     *
     * @param responsible the responsible {@link User} to get all {@link KeyCategory} entities of
     * @return a collection of all found {@link KeyCategory} entities
     */
    Set<KeyCategory> findAllByResponsible(User responsible);

    /**
     * Finds the {@link KeyCategory} with by the given {@code id}.
     *
     * @param id the ID of the {@link KeyCategory} to find
     * @return the {@link KeyCategory} entity if found
     * @throws KeyCategoryNotFoundException if no {@link KeyCategory} entity with the given {@code id} has been found
     */
    KeyCategory findOneById(long id) throws KeyCategoryNotFoundException;
}
