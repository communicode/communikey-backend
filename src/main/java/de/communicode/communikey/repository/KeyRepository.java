/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2017
 */
package de.communicode.communikey.repository;

import de.communicode.communikey.domain.Key;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

/**
 * A repository for {@link Key} entities.
 *
 * @author sgreb@communicode.de
 * @since 0.1.0
 */
@Repository
public interface KeyRepository extends JpaRepository<Key, Long> {

/**
     * Finds all keys created by a user with the specified login.
     *
     * @param login the login of the user to find all created keys of
     * @return a collection of keys
     * @since 0.2.0
     */
    Set<Key> findAllByCreatedBy(String login);

    /**
     * Finds the key with the specified name.
     *
     * @param name the name of the key to find
     * @return the key if found
     * @since 0.2.0
     */
    Key findOneByName(String name);

    Set<Key> findAllByCategoryIsNull();
}