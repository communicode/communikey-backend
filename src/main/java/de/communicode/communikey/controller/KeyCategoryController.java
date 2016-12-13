/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2016
 */
package de.communicode.communikey.controller;

import static de.communicode.communikey.controller.RequestMappings.KEY_CATEGORIES;
import static de.communicode.communikey.controller.RequestMappings.KEY_CATEGORY_ID;
import static java.util.Objects.requireNonNull;

import de.communicode.communikey.domain.KeyCategory;
import de.communicode.communikey.exception.KeyCategoryNotFoundException;
import de.communicode.communikey.service.KeyCategoryRestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * The REST API controller to process {@link KeyCategory} entities.
 * <p>
 *     Mapped to the "{@value RequestMappings#KEY_CATEGORIES}" endpoint.
 *
 * @author sgreb@communicode.de
 * @since 0.2.0
 */
@RestController
@RequestMapping(KEY_CATEGORIES)
public class KeyCategoryController {

    private final KeyCategoryRestService keyCategoryService;

    @Autowired
    public KeyCategoryController(KeyCategoryRestService keyCategoryService) {
        this.keyCategoryService = requireNonNull(keyCategoryService, "keyCategoryService must not be null!");
    }

    /**
     * Gets all key category entities.
     * <p>
     *     This endpoint is mapped to "{@value RequestMappings#KEY_CATEGORIES}".
     *
     * @param creatorUserId the ID of the user to get all created key category entities of
     * @param responsibleUserId the ID of the responsible user to get all key category entities of
     * @param name the name of the key category entities to get
     * @return a collection of key categories
     */
    @GetMapping
    Set<KeyCategory> getAll(@RequestParam(name = "creator", required = false) Long creatorUserId,
                               @RequestParam(name = "responsible", required = false) Long responsibleUserId,
                               @RequestParam(name = "name", required = false) String name) {
        return keyCategoryService.getAll().stream()
            .filter(keyCategory -> creatorUserId == null || creatorUserId.equals(keyCategory.getCreator().getId()))
            .filter(keyCategory -> responsibleUserId == null || responsibleUserId.equals(keyCategory.getResponsible().getId()))
            .filter(keyCategory -> name == null || name.equalsIgnoreCase(keyCategory.getName()))
            .collect(Collectors.toSet());
    }

    /**
     * Gets the {@link KeyCategory} entity with the specified ID.
     * <p>
     *     This endpoint is mapped to "{@value RequestMappings#KEY_CATEGORIES}{@value RequestMappings#KEY_CATEGORY_ID}".
     *
     * @param keyCategoryId the ID of the key category entity to get
     * @return the key category entity
     * @throws KeyCategoryNotFoundException if the key category entity with the specified ID has not been found
     */
    @GetMapping(value = KEY_CATEGORY_ID)
    KeyCategory get(@PathVariable long keyCategoryId) throws KeyCategoryNotFoundException {
        return keyCategoryService.getById(keyCategoryId);
    }
}