/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2017
 */
package de.communicode.communikey.repository;

import de.communicode.communikey.domain.KeyCategory;
import de.communicode.communikey.domain.User;
import de.communicode.communikey.domain.UserGroup;
import de.communicode.communikey.exception.KeyCategoryConflictException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
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
     * Finds all key category entities created by the specified user.
     *
     * @param user the user to get all created key categories of
     * @return a collection of found key category entities
     */
    Set<KeyCategory> findAllByCreator(User user); // TODO: Check if this method is necessary

    /**
     * Finds all key category entities the specified user is responsible for.
     *
     * @param user the responsible user to get all key category entities of
     * @return a collection of found key category entities
     */
    //Set<KeyCategory> findAllByResponsible(User user); // TODO: Check if this method is necessary; should be better resolvable through User reference

    /**
     * Finds all key category entities of the repository.
     *
     * @return a collection of found key category entities
     */
    Set<KeyCategory> findAll();


    KeyCategory findOneByIdOrIdIsNull(Long id);

    Set<KeyCategory> findAllByParentIsNull();
}
