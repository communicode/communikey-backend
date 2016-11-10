/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2016
 */
package de.communicode.communikey.service;

import de.communicode.communikey.domain.Key;
import de.communicode.communikey.domain.KeyCategory;
import de.communicode.communikey.domain.User;
import de.communicode.communikey.exception.KeyNotFoundException;

import java.sql.Timestamp;
import java.util.Set;

/**
 * A service to process {@link Key} entities.
 *
 * @author sgreb@communicode.de
 * @since 0.1.0
 */
public interface KeyService {

    /**
     * Deletes the given {@link Key}.
     *
     * @param key the {@link Key} entity to delete
     * @throws NullPointerException if the given {@code key} entity is null
     */
    void delete(Key key) throws NullPointerException;

    /**
     * Gets all {@link Key} entities of the {@link de.communicode.communikey.repository.KeyRepository}.
     *
     * @return a collection of all {@link Key} entities
     */
    Set<Key> getAll();

    /**
     * Gets all {@link Key} entities with the given {@link KeyCategory}.
     *
     * @param keyCategory the {@link KeyCategory} from which all {@link Key} entities are to be found
     * @return a collection of found {@link Key} entities
     * @since 0.2.0
     */
    Set<Key> getAllByKeyCategory(KeyCategory keyCategory);

    /**
     * Gets all {@link Key} entities with the given creation {@code timestamp}.
     *
     * @param timestamp the creation {@link Timestamp} from which all {@link Key} are to be found
     * @return a collection of found {@link Key} entities
     * @since 0.2.0
     */
    Set<Key> getAllByCreationTimestamp(Timestamp timestamp);

    /**
     * Gets all {@link Key} entities created by the given {@link User}.
     *
     * @param creator the {@link User} entity to find all created {@link Key} entities of
     * @return a collection of found {@link Key} entities
     * @since 0.2.0
     */
    Set<Key> getAllByCreator(User creator);

    /**
     * Gets all {@link Key} entities with the given {@code value}.
     *
     * @param value the value from which all {@link Key} entity are to be found
     * @return a collection of found {@link Key} entities
     * @since 0.2.0
     */
    Set<Key> getAllByValue(String value);

    /**
     * Gets the {@link Key} with the given {@code id}.
     *
     * @param keyId the ID of the {@link Key} entity to get
     * @return the {@link Key} entity with the given ID
     * @throws de.communicode.communikey.exception.KeyNotFoundException if no {@link Key} with the given {@code keyId} has been found
     */
    Key getById(long keyId) throws KeyNotFoundException;

    /**
     * Modifies the value of the given {@link Key}.
     *
     * @param key the {@link Key} entity to modify the value of
     * @param newValue the new value for the given {@link Key} entity
     * @throws NullPointerException if the given {@code key} entity is null
     * @throws IllegalArgumentException if the given new value is empty
     */
    void modifyValue(Key key, String newValue) throws NullPointerException, IllegalArgumentException;

    /**
     * Saves the given {@link Key} in the {@link de.communicode.communikey.repository.KeyRepository}.
     *
     * @param key the {@link Key} entity to save
     * @return the saved {@link Key}
     * @throws NullPointerException if the given {@code key} entity is null
     */
    Key save(Key key) throws NullPointerException;

    /**
     * Validates that the given {@link Key} entity exists in the {@link de.communicode.communikey.repository.KeyRepository}.
     *
     * @param keyId the ID of the {@link Key} entity to validate
     * @return the validated {@link Key}
     * @throws KeyNotFoundException if the {@link Key} with the given {@code keyId} has not been found
     */
    Key validate(long keyId) throws KeyNotFoundException;
}