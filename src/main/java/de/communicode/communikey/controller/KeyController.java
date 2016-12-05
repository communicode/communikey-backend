/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2016
 */
package de.communicode.communikey.controller;

import static de.communicode.communikey.controller.RequestMappings.KEYS;
import static de.communicode.communikey.controller.RequestMappings.KEY_ID;
import static java.util.Objects.requireNonNull;

import de.communicode.communikey.domain.Key;
import de.communicode.communikey.domain.KeyDto;
import de.communicode.communikey.domain.converter.KeyDtoConverter;
import de.communicode.communikey.exception.KeyNotFoundException;
import de.communicode.communikey.service.KeyRestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The REST API controller to process {@link Key} entities.
 * <p>
 *     Mapped to the "{@value RequestMappings#KEYS}" endpoint.
 *
 * @author sgreb@communicode.de
 * @since 0.1.0
 */
@RestController
@RequestMapping(KEYS)
public class KeyController {

    private final KeyRestService keyService;

    private final KeyDtoConverter keyConverter;

    @Autowired
    public KeyController(KeyRestService keyService, KeyDtoConverter keyConverter) {
        this.keyService = requireNonNull(keyService, "keyService must not be null!");
        this.keyConverter = requireNonNull(keyConverter, "keyConverter must not be null!");
    }

    /**
     * Gets all {@link Key} entities.
     * <p>
     *     This endpoint is mapped to "{@value RequestMappings#KEYS}".
     *
     * @param limit the amount of key data transfer objects to include in the response
     * @param userId the ID of the user to get all key entities of
     * @return a collection of {@link Key} data transfer objects
     * @since 0.2.0
     */
    @GetMapping
    Set<KeyDto> getAll(@RequestParam(required = false) Long limit, @RequestParam(name = "user", required = false) Long userId) {
        return keyService.getAll().stream()
            .filter(key -> userId == null || userId.equals(key.getCreator().getId()))
            .limit(Optional.ofNullable(limit).orElse(Long.MAX_VALUE))
            .map(keyConverter)
            .collect(Collectors.toSet());
    }

    /**
     * Gets the {@link Key} entity with the specified ID.
     * <p>
     *     This endpoint is mapped to "{@value RequestMappings#KEYS}{@value RequestMappings#KEY_ID}".
     *
     * @param keyId the ID of the key entity to get
     * @return the key data transfer object
     * @throws KeyNotFoundException if the key entity with the specified ID has not been found
     */
    @GetMapping(value = KEY_ID)
    KeyDto get(@PathVariable long keyId) throws KeyNotFoundException {
        return convertToDto(keyService.getById(keyId));
    }

    /**
     * Converts a key entity to the associated key data transfer object.
     *
     * @param key the key entity to convert
     * @return the converted key data transfer object
     * @since 0.2.0
     */
    private KeyDto convertToDto(Key key) {
        return keyConverter.convert(key);
    }

    /**
     * Converts a key data transfer object to the associated key entity.
     *
     * @param keyDto the key data transfer object to convert
     * @return the converted key entity
     * @throws KeyNotFoundException if the associated key entity of the key data transfer object has not been found
     * @since 0.2.0
     */
    private Key convertToEntity(KeyDto keyDto) throws KeyNotFoundException {
        return keyService.getById(keyDto.getId());
    }
}