/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2017
 */
package de.communicode.communikey.controller;

import static de.communicode.communikey.controller.RequestMappings.KEYS;
import static de.communicode.communikey.controller.RequestMappings.KEYS_ID;
import static java.util.Objects.requireNonNull;

import de.communicode.communikey.domain.Key;
import de.communicode.communikey.service.payload.KeyPayload;
import de.communicode.communikey.service.KeyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import java.util.Set;

/**
 * The REST API controller to process {@link Key}s.
 * <p>
 * Mapped to the "{@value RequestMappings#KEYS}" endpoint.
 *
 * @author sgreb@communicode.de
 * @since 0.1.0
 */
@RestController
@RequestMapping(KEYS)
public class KeyController {
    private final KeyService keyService;

    @Autowired
    public KeyController(KeyService keyService) {
        this.keyService = requireNonNull(keyService, "keyService must not be null!");
    }

    /**
     * Creates a new key with the specified payload.
     * <p>
     * This endpoint is mapped to "{@value RequestMappings#KEYS}{@value RequestMappings#KEYS_ID}".
     *
     * @param payload the key request payload
     * @return the key as response entity
     * @since 0.2.0
     */
    @PostMapping
    public ResponseEntity<Key> create(@Valid @RequestBody KeyPayload payload) {
        return new ResponseEntity<>(keyService.create(payload), HttpStatus.CREATED);
    }

    /**
     * Deletes the key with the specified ID.
     * <p>
     * This endpoint is mapped to "{@value RequestMappings#KEYS}{@value RequestMappings#KEYS_ID}".
     *
     * @param keyId the ID of the key to delete
     * @return a empty response entity
     * @since 0.2.0
     */
    @DeleteMapping(value = KEYS_ID)
    public ResponseEntity<Void> delete(@PathVariable Long keyId) {
        keyService.delete(keyId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Deletes all keys.
     * <p>
     * This endpoint is mapped to "{@value RequestMappings#KEYS}".
     *
     * @return a empty response entity
     * @since 0.2.0
     */
    @DeleteMapping
    public ResponseEntity<Key> deleteAll() {
        keyService.deleteAll();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Gets the key with the specified ID.
     * <p>
     * This endpoint is mapped to "{@value RequestMappings#KEYS}{@value RequestMappings#KEYS_ID}".
     *
     * @param keyId the ID of the key entity to get
     * @return the key as response entity
     */
    @GetMapping(value = KEYS_ID)
    public ResponseEntity<Key> get(@PathVariable Long keyId) {
        return new ResponseEntity<>(keyService.get(keyId), HttpStatus.OK);
    }

    /**
     * Gets all keys.
     * <p>
     * This endpoint is mapped to "{@value RequestMappings#KEYS}".
     *
     * @return a collection of keys as response entity
     */
    @GetMapping
    public ResponseEntity<Set<Key>> getAll() {
        return new ResponseEntity<>(keyService.getAll(), HttpStatus.OK);
    }

    /**
     * Updates a key with the specified payload.
     * <p>
     * This endpoint is mapped to "{@value RequestMappings#KEYS}{@value RequestMappings#KEYS_ID}".
     *
     * @param keyId the ID of the key entity to update
     * @param payload the key request payload to update the key entity with
     * @return the updated key as response entity
     * @since 0.2.0
     */
    @PutMapping(value = KEYS_ID)
    public ResponseEntity<Key> update(@PathVariable Long keyId, @Valid @RequestBody KeyPayload payload) {
        return new ResponseEntity<>(keyService.update(keyId, payload), HttpStatus.OK);
    }
}