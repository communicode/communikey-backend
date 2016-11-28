/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2016
 */
package de.communicode.communikey.service;

import de.communicode.communikey.domain.Key;
import de.communicode.communikey.exception.KeyNotFoundException;
import de.communicode.communikey.exception.UserNotFoundException;

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
     * Deletes the specified key.
     *
     * @param keyId the ID of the key entity to delete
     * @throws KeyNotFoundException if the specified key has not been found
     */
    void delete(long keyId) throws KeyNotFoundException;

    /**
     * Gets all key entities.
     *
     * @return a collection of all key entities
     */
    Set<Key> getAll();

    /**
     * Gets all key entities with the specified creation timestamp.
     *
     * @param timestamp the creation timestamp from which all key are to be found
     * @return a collection of found key entities
     * @since 0.2.0
     */
    Set<Key> getAllByCreationTimestamp(Timestamp timestamp);

    /**
     * Gets all key entities created by the specified user.
     *
     * @param creatorUserId the ID of the user entity to find all created key entities of
     * @return a collection of found key entities
     * @throws UserNotFoundException if the user entity with the specified ID has not been found
     * @since 0.2.0
     */
    Set<Key> getAllByCreator(long creatorUserId) throws UserNotFoundException;

    /**
     * Gets all key entities with the specified value.
     *
     * @param value the value from which all key entity are to be found
     * @return a collection of found key entities
     * @since 0.2.0
     */
    Set<Key> getAllByValue(String value);

    /**
     * Gets the key with the specified ID.
     *
     * @param keyId the ID of the key entity to get
     * @return the key entity with the specified ID
     * @throws KeyNotFoundException if no key with the specified ID has been found
     */
    Key getById(long keyId) throws KeyNotFoundException;

    /**
     * Modifies the value of the specified key.
     *
     * @param keyId the ID of the key entity to modify the value of
     * @param newValue the new value for the specified key entity
     * @throws KeyNotFoundException if no key with the specified ID has been found
     * @throws IllegalArgumentException if the specified new value is empty
     */
    void modifyValue(long keyId, String newValue) throws KeyNotFoundException, IllegalArgumentException;

    /**
     * Saves the specified key entity.
     *
     * @param key the key entity to save
     * @return the saved key entity
     * @throws NullPointerException if the specified key entity is null
     */
    Key save(Key key) throws NullPointerException;

    /**
     * Validates the specified {@link Key} entity.
     *
     * @param keyId the ID of the key entity to validate
     * @return the key entity if validated
     * @throws KeyNotFoundException if no key entity with the specified ID has been found
     */
    Key validate(long keyId) throws KeyNotFoundException;
}