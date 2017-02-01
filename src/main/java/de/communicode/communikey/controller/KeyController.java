/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2017
 */
package de.communicode.communikey.controller;

import static de.communicode.communikey.controller.RequestMappings.KEYS;
import static de.communicode.communikey.controller.RequestMappings.KEY_ID;
import static java.util.Objects.requireNonNull;

import de.communicode.communikey.domain.Key;
import de.communicode.communikey.domain.request.KeyRequest;
import de.communicode.communikey.service.KeyRestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import java.security.Principal;
import java.util.Set;

/**
 * The REST API controller to process {@link Key} entities.
 * <p>
 *     Mapped to the "{@value RequestMappings#KEYS}" endpoint.
 *
 * @author sgreb@communicode.de
 * @since 0.1.0
 */
@Validated
@RestController
@RequestMapping(KEYS)
public class KeyController {
    private final KeyRestService keyService;

    @Autowired
    public KeyController(KeyRestService keyService) {
        this.keyService = requireNonNull(keyService, "keyService must not be null!");
    }

    /**
     * Creates a new key with the specified payload.
     * <p>
     *     This endpoint is mapped to "{@value RequestMappings#KEYS}{@value RequestMappings#KEY_ID}".
     *
     * @param principal the principal that represents the user to create a new key for
     * @param payload the key request payload
     * @return the key as response entity
     * @since 0.2.0
     */
    @PostMapping
    public ResponseEntity<Key> create(Principal principal, @Valid @RequestBody KeyRequest payload) {
        return new ResponseEntity<>(keyService.create(payload, principal), HttpStatus.CREATED);
    }

    /**
     * Deletes the key with the specified ID.
     * <p>
     *     This endpoint is mapped to "{@value RequestMappings#KEYS}{@value RequestMappings#KEY_ID}".
     *
     * @param keyId the ID of the key to delete
     * @return the key as response entity
     * @since 0.2.0
     */
    @DeleteMapping(value = KEY_ID)
    public ResponseEntity<Key> delete(@PathVariable("keyId") Long keyId) {
        keyService.delete(keyId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Gets the key with the specified ID.
     * <p>
     *     This endpoint is mapped to "{@value RequestMappings#KEYS}{@value RequestMappings#KEY_ID}".
     *
     * @param keyId the ID of the key entity to get
     * @return the key as response entity
     */
    @GetMapping(value = KEY_ID)
    public ResponseEntity<Key> get(@PathVariable("keyId") Long keyId) {
        return new ResponseEntity<>(keyService.getById(keyId), HttpStatus.OK);
    }

    /**
     * Gets all keys.
     * <p>
     *     This endpoint is mapped to "{@value RequestMappings#KEYS}".
     *
     * @param principal the principal that represents the user entity to get all key entities of
     * @return a collection of keys as response entity
     */
    @GetMapping
    public ResponseEntity<Set<Key>> getAll(Principal principal) {
        return new ResponseEntity<>(keyService.getAll(principal), HttpStatus.OK);
    }

    /**
     * Updates a key with the specified payload.
     * <p>
     *     This endpoint is mapped to "{@value RequestMappings#KEYS}{@value RequestMappings#KEY_ID}".
     *
     * @param keyId the ID of the key entity to update
     * @param payload the key request payload to update the key entity with
     * @return the updated key as response entity
     * @since 0.2.0
     */
    @PutMapping(value = KEY_ID)
    public ResponseEntity<Key> update(@PathVariable("keyId") Long keyId, @Valid @RequestBody KeyRequest payload) {
        return new ResponseEntity<>(keyService.update(keyId, payload), HttpStatus.OK);
    }
}