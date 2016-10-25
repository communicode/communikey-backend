/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2016
 */
package de.communicode.communikey.service;

import de.communicode.communikey.domain.Key;

import java.sql.Timestamp;
import java.util.Set;

/**
 * A service to interact with the {@link de.communicode.communikey.repository.KeyRepository}.
 *
 * @author sgreb@communicode.de
 * @since 0.1.0
 */
public interface KeyService {
    /**
     * Gets all {@link Key} entities of the {@link de.communicode.communikey.repository.KeyRepository}.
     *
     * @return a collection of all {@link Key} entities
     */
    Set<Key> getAllKeys();

    /**
     * Gets the {@link Key} with the given {@code id}.
     *
     * @param id The ID of the {@link Key}
     * @return the {@link Key} with the given ID
     * @throws NullPointerException if the given {@code id} is invalid
     */
    Key getKeyById(long id) throws NullPointerException;

    /**
     * Gets the first {@link Key} found with the given creation date.
     *
     * @param timestamp The {@link Timestamp} of a {@link Key} to find
     * @return a {@link Key} with the given creation timestamp
     * @throws NullPointerException if no key found with the given {@code timestamp}
     */
    Key getKeyByCreationDate(Timestamp timestamp) throws NullPointerException;

    /**
     * Deletes the given {@link Key}.
     *
     * @param key The {@link Key} to delete
     * @throws NullPointerException if the given {@code key} is null
     */
    void deleteKey(Key key) throws NullPointerException;

    /**
     * Modifies the value of the given {@link Key}.
     *
     * @param key The {@link Key} to modify the value of
     * @param newValue The new value for the given {@link Key}
     * @throws NullPointerException if the given {@code key} is null
     * @throws IllegalArgumentException if the given new value is empty
     */
    void modifyKeyValue(Key key, String newValue) throws NullPointerException, IllegalArgumentException;

    /**
     * Saves the given {@link Key} in the {@link de.communicode.communikey.repository.KeyRepository}.
     *
     * @param key The {@link Key} to save
     * @return the saved {@link Key}
     * @throws NullPointerException if the given {@code key} is null
     */
    Key saveKey(Key key) throws NullPointerException;
}