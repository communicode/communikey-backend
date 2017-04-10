/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2017
 */
package de.communicode.communikey.service;

import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;

import com.google.common.collect.Sets;
import de.communicode.communikey.domain.Key;
import de.communicode.communikey.domain.User;
import de.communicode.communikey.security.SecurityUtils;
import de.communicode.communikey.service.payload.KeyPayload;
import de.communicode.communikey.exception.KeyNotFoundException;
import de.communicode.communikey.repository.KeyRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * The REST API service to process {@link Key} entities via a {@link KeyRepository}.
 *
 * @author sgreb@communicode.de
 * @since 0.1.0
 */
@Service
public class KeyService {

    private static final Logger log = LogManager.getLogger();
    private final KeyRepository keyRepository;
    private final UserService userService;

    @Autowired
    public KeyService(KeyRepository keyRepository, UserService userRestService) {
        this.keyRepository = requireNonNull(keyRepository, "keyRepository must not be null!");
        this.userService = requireNonNull(userRestService, "userService must not be null!");
    }

    /**
     * Creates a new key.
     *
     * @param payload the key payload
     */
    public Key create(KeyPayload payload) {
        Key key = new Key();
        String userLogin = SecurityUtils.getCurrentUserLogin();
        User user = userService.validate(userLogin);
        key.setCreator(user);
        key.setName(payload.getName());
        key.setPassword(payload.getPassword());
        key = keyRepository.save(key);
        userService.addKey(userLogin, key);
        log.debug("Created new key with ID '{}'", key.getId());
        return key;
    }

    /**
     * Deletes the key with the specified ID.
     *
     * @param keyId the ID of the key to delete
     * @throws KeyNotFoundException if the key with the specified ID has not been found
     */
    public void delete(Long keyId) throws KeyNotFoundException {
        keyRepository.delete(validate(keyId));
        log.debug("Deleted key with ID '{}'", keyId);
    }

    /**
     * Deletes all keys.
     */
    public void deleteAll() {
        keyRepository.deleteAll();
        log.debug("Deleted all keys");
    }

    /**
     * Gets the key with the specified ID.
     *
     * @param keyId the ID of the key to get
     * @return the key
     * @throws KeyNotFoundException if the key with the specified ID has not been found
     */
    public Key get(Long keyId) throws KeyNotFoundException {
        return validate(keyId);
    }

    /**
     * Gets all keys the current user is authorized to.
     *
     * @return a collection of keys
     */
    public Set<Key> getAll() {
        return Sets.newConcurrentHashSet(keyRepository.findAll());
    }

    /**
     * Updates a key with the specified payload.
     *
     * @param keyId the ID of the key to update
     * @param payload the payload to update the key with
     * @return the updated key
     * @since 0.2.0
     */
    public Key update(Long keyId, KeyPayload payload) {
        Key key = validate(keyId);
        key.setName(payload.getName());
        key.setPassword(payload.getPassword());
        key = keyRepository.save(key);
        log.debug("Updated key with ID '{}'", key.getId());
        return key;
    }

    /**
     * Validates a key.
     *
     * @param keyId the ID of the key to validate
     * @return the key if validated
     * @throws KeyNotFoundException if the key with the specified ID has not been found
     */
    public Key validate(Long keyId) throws KeyNotFoundException {
        return ofNullable(keyRepository.findOne(keyId)).orElseThrow(() -> new KeyNotFoundException(keyId));
    }
}