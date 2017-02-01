/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2017
 */
package de.communicode.communikey.service;

import de.communicode.communikey.domain.Key;
import de.communicode.communikey.domain.request.KeyRequest;
import de.communicode.communikey.exception.key.KeyNotFoundException;

import java.security.Principal;
import java.util.Set;

/**
 * A service to process {@link Key} entities.
 *
 * @author sgreb@communicode.de
 * @since 0.1.0
 */
public interface KeyService {

    /**
     * Creates a new key.
     *
     * @param payload the key request payload
     * @param principal the principal that represents the user to create a new key for
     */
    Key create(KeyRequest payload, Principal principal);

    /**
     * Deletes the key with the specified ID.
     *
     * @param keyId the ID of the key entity to delete
     * @throws KeyNotFoundException if the specified key has not been found
     */
    void delete(Long keyId) throws KeyNotFoundException;

    /**
     * Gets all key entities the principal is authorized to.
     *
     * @param principal the principal that represents the user to get all key entities of
     * @return a collection of all key entities
     */
    Set<Key> getAll(Principal principal);

    /**
     * Gets the key with the specified ID.
     *
     * @param keyId the ID of the key entity to get
     * @return the key entity with the specified ID
     * @throws KeyNotFoundException if no key with the specified ID has been found
     */
    Key getById(Long keyId) throws KeyNotFoundException;

    /**
     * Updates a key with the specified payload.
     *
     * @param keyId the ID of the key entity to update
     * @param payload the key request payload to update the key with
     * @return the updated key entity
     * @since 0.2.0
     */
    Key update(Long keyId, KeyRequest payload);

    /**
     * Saves a key.
     *
     * @param key the key entity to save
     * @return the saved key entity
     * @throws NullPointerException if the specified key entity is null
     */
    Key save(Key key) throws NullPointerException;

    /**
     * Validates a key.
     *
     * @param keyId the ID of the key entity to validate
     * @return the key entity if validated
     * @throws KeyNotFoundException if no key entity with the specified ID has been found
     */
    Key validate(Long keyId) throws KeyNotFoundException;
}