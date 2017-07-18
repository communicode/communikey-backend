/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2017
 */
package de.communicode.communikey.controller;

import static de.communicode.communikey.controller.PathVariables.KEY_ID;
import static de.communicode.communikey.controller.RequestMappings.KEYS;
import static de.communicode.communikey.controller.RequestMappings.KEY_HASHID;
import static java.util.Objects.requireNonNull;

import de.communicode.communikey.domain.Key;
import de.communicode.communikey.exception.HashidNotValidException;
import de.communicode.communikey.exception.KeyNotFoundException;
import de.communicode.communikey.security.AuthoritiesConstants;
import de.communicode.communikey.service.payload.KeyPayload;
import de.communicode.communikey.service.KeyService;
import org.hashids.Hashids;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
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
 *
 * <p>Mapped to the "{@value RequestMappings#KEYS}" endpoint.
 *
 * @author sgreb@communicode.de
 * @since 0.1.0
 */
@RestController
@RequestMapping(KEYS)
public class KeyController {
    private final KeyService keyService;
    private final Hashids hashids;

    @Autowired
    public KeyController(KeyService keyService, Hashids hashids) {
        this.keyService = requireNonNull(keyService, "keyService must not be null!");
        this.hashids = requireNonNull(hashids, "hashids must not be null!");
    }

    /**
     * Creates a new key with the specified payload.
     *
     * <p>This endpoint is mapped to "{@value RequestMappings#KEYS}{@value RequestMappings#KEY_HASHID}".
     *
     * @param payload the key request payload
     * @return the key as response entity
     * @since 0.2.0
     */
    @PostMapping
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<Key> create(@Valid @RequestBody KeyPayload payload) {
        return new ResponseEntity<>(keyService.create(payload), HttpStatus.CREATED);
    }

    /**
     * Deletes the key with the specified Hashid.
     *
     * <p>This endpoint is mapped to "{@value RequestMappings#KEYS}{@value RequestMappings#KEY_HASHID}".
     *
     * @param keyHashid the Hashid of the key to delete
     * @return a empty response entity
     * @since 0.2.0
     */
    @DeleteMapping(value = KEY_HASHID)
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<Void> delete(@PathVariable(name = KEY_ID) String keyHashid) {
        keyService.delete(decodeSingleValueHashid(keyHashid));
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Deletes all keys.
     *
     * <p>This endpoint is mapped to "{@value RequestMappings#KEYS}".
     *
     * @return a empty response entity
     * @since 0.2.0
     */
    @DeleteMapping
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<Void> deleteAll() {
        keyService.deleteAll();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Gets the key with the specified Hashid.
     *
     * <p>This endpoint is mapped to "{@value RequestMappings#KEYS}{@value RequestMappings#KEY_HASHID}".
     *
     * @param keyHashid the Hashid of the key entity to get
     * @return the key as response entity
     */
    @GetMapping(value = KEY_HASHID)
    @Secured(AuthoritiesConstants.USER)
    public ResponseEntity get(@PathVariable(name = KEY_ID) String keyHashid) {
        return keyService.get(decodeSingleValueHashid(keyHashid))
                .map(key -> new ResponseEntity<>(key, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.FORBIDDEN));
    }

    /**
     * Gets all keys.
     *
     * <p>This endpoint is mapped to "{@value RequestMappings#KEYS}".
     *
     * @return a collection of keys as response entity
     */
    @GetMapping
    @Secured(AuthoritiesConstants.USER)
    public ResponseEntity<Set<Key>> getAll() {
        return new ResponseEntity<>(keyService.getAll(), HttpStatus.OK);
    }

    /**
     * Updates a key with the specified payload.
     *
     * <p>This endpoint is mapped to "{@value RequestMappings#KEYS}{@value RequestMappings#KEY_HASHID}".
     *
     * @param keyHashid the Hashid of the key entity to update
     * @param payload the key request payload to update the key entity with
     * @return the updated key as response entity
     * @since 0.2.0
     */
    @PutMapping(value = KEY_HASHID)
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<Key> update(@PathVariable(name = KEY_ID) String keyHashid, @Valid @RequestBody KeyPayload payload) {
        return new ResponseEntity<>(keyService.update(hashids.decode(keyHashid)[0], payload), HttpStatus.OK);
    }

    /**
     * Decodes the specified Hashid.
     *
     * @param keyHashid the Hashid of the key to decode
     * @return the decoded Hashid if valid
     * @throws KeyNotFoundException if the Hashid is invalid and the key has not been found
     * @since 0.12.0
     */
    private Long decodeSingleValueHashid(String keyHashid) throws HashidNotValidException {
        long[] decodedHashid = hashids.decode(keyHashid);
        if (decodedHashid.length == 0) {
            throw new HashidNotValidException();
        }
        return decodedHashid[0];
    }
}
