/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2017
 */
package de.communicode.communikey.controller;

import static de.communicode.communikey.controller.RequestMappings.KEY_CATEGORIES;
import static de.communicode.communikey.controller.RequestMappings.KEY_CATEGORIES_ID;
import static de.communicode.communikey.controller.RequestMappings.KEY_CATEGORY_CHILDREN;
import static de.communicode.communikey.controller.RequestMappings.KEY_CATEGORY_GROUPS;
import static de.communicode.communikey.controller.RequestMappings.KEY_CATEGORY_KEYS;
import static de.communicode.communikey.controller.RequestMappings.KEY_CATEGORY_RESPONSIBLE;
import static java.util.Objects.requireNonNull;

import de.communicode.communikey.domain.KeyCategory;
import de.communicode.communikey.exception.UserNotFoundException;
import de.communicode.communikey.security.AuthoritiesConstants;
import de.communicode.communikey.service.payload.KeyCategoryPayload;
import de.communicode.communikey.exception.KeyCategoryNotFoundException;
import de.communicode.communikey.service.KeyCategoryService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import java.util.Set;

/**
 * The REST API controller to process {@link KeyCategory} entities.
 *
 * <p>Mapped to the "{@value RequestMappings#KEY_CATEGORIES}" endpoint.
 *
 * @author sgreb@communicode.de
 * @since 0.2.0
 */
@RestController
@RequestMapping(KEY_CATEGORIES)
public class KeyCategoryController {

    private final KeyCategoryService keyCategoryService;

    @Autowired
    public KeyCategoryController(KeyCategoryService keyCategoryService) {
        this.keyCategoryService = requireNonNull(keyCategoryService, "keyCategoryService must not be null!");
    }

    /**
     * Adds a key category as child to a parent key category with the specified ID.
     *
     * <p>This endpoint is mapped to "{@value RequestMappings#KEY_CATEGORIES}{@value RequestMappings#KEY_CATEGORY_CHILDREN}".
     *
     * @param keyCategoryId the ID of the parent key category to add the child to
     * @param childKeyCategoryId the ID of the child key category to be added
     * @return the updated parent key category as response entity
     */
    @GetMapping(value = KEY_CATEGORY_CHILDREN)
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<KeyCategory> addChild(@PathVariable Long keyCategoryId, @RequestParam Long childKeyCategoryId) {
        return new ResponseEntity<>(keyCategoryService.addChild(keyCategoryId, childKeyCategoryId), HttpStatus.OK);
    }

    /**
     * Adds a user group to a key category with the specified ID.
     *
     * <p>This endpoint is mapped to "{@value RequestMappings#KEY_CATEGORIES}{@value RequestMappings#KEY_CATEGORY_GROUPS}".
     *
     * @param keyCategoryId the ID of the parent key category to add the child to
     * @param userGroupName the name of the user group to be added
     * @return the updated parent key category as response entity
     */
    @GetMapping(value = KEY_CATEGORY_GROUPS)
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<KeyCategory> addUserGroup(@PathVariable Long keyCategoryId, @RequestParam String userGroupName) {
        return new ResponseEntity<>(keyCategoryService.addUserGroup(keyCategoryId, userGroupName), HttpStatus.OK);
    }

    /**
     * Adds the key to the key category with the specified ID.
     *
     * <p>This endpoint is mapped to "{@value RequestMappings#KEY_CATEGORIES}{@value RequestMappings#KEY_CATEGORY_KEYS}".
     *
     * @param keyCategoryId the ID of the key category to add the key to
     * @param keyId the ID of the key to be added
     * @return the updated key category entity
     */
    @GetMapping(value = KEY_CATEGORY_KEYS)
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<KeyCategory> addKey(@PathVariable Long keyCategoryId, @RequestParam Long keyId) {
        return new ResponseEntity<>(keyCategoryService.addKey(keyCategoryId, keyId), HttpStatus.OK);
    }

    /**
     * Creates a new key category without a parent key category.
     *
     * <p>This endpoint is mapped to "{@value RequestMappings#KEY_CATEGORIES}".
     *
     * @param payload the payload for the new key category
     * @return the key category as response entity
     */
    @PostMapping
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<KeyCategory> create(@Valid @RequestBody KeyCategoryPayload payload) {
        return new ResponseEntity<>(keyCategoryService.create(payload), HttpStatus.CREATED);
    }

    /**
     * Deletes the key category with the specified ID.
     *
     * <p><strong>This is a recursive action that deletes all children key categories!</strong>
     *
     * <p>This endpoint is mapped to {@value RequestMappings#KEYS}{@value RequestMappings#KEY_CATEGORIES_ID}.
     *
     * @param keyCategoryId the ID of the key category to delete
     * @return a empty response entity
     */
    @DeleteMapping(value = KEY_CATEGORIES_ID)
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<Void> delete(@PathVariable Long keyCategoryId) {
        keyCategoryService.delete(keyCategoryId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Deletes all key categories.
     *
     * <p>This endpoint is mapped to "{@value RequestMappings#KEYS}".
     *
     * @return a empty response entity
     */
    @DeleteMapping
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<Void> deleteAll() {
        keyCategoryService.deleteAll();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Gets the {@link KeyCategory} entity with the specified ID.
     *
     * <p>This endpoint is mapped to "{@value RequestMappings#KEY_CATEGORIES}{@value RequestMappings#KEY_CATEGORIES_ID}".
     *
     * @param keyCategoryId the ID of the key category entity to get
     * @return the key category entity
     * @throws KeyCategoryNotFoundException if the key category entity with the specified ID has not been found
     */
    @GetMapping(value = KEY_CATEGORIES_ID)
    @Secured(AuthoritiesConstants.ADMIN)
    KeyCategory get(@PathVariable Long keyCategoryId) throws KeyCategoryNotFoundException {
        return keyCategoryService.get(keyCategoryId);
    }

    /**
     * Gets all key category entities.
     *
     * <p>This endpoint is mapped to "{@value RequestMappings#KEY_CATEGORIES}".
     *
     * @return a collection of key categories
     */
    @GetMapping
    Set<KeyCategory> getAll() {
        return keyCategoryService.getAll();
    }

    /**
     * Removes a user group from the key category with the specified ID.
     *
     * <p>This endpoint is mapped to "{@value RequestMappings#KEY_CATEGORIES}{@value RequestMappings#KEY_CATEGORY_GROUPS}".
     *
     * @param keyCategoryId the ID of the key category to remove the user group from
     * @param userGroupName the name of the user group to be removed from the key category
     * @return the updated key category as response entity
     */
    @DeleteMapping(value = KEY_CATEGORY_GROUPS)
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<KeyCategory> removeUserGroup(@PathVariable Long keyCategoryId, @RequestParam String userGroupName) {
        return new ResponseEntity<>(keyCategoryService.removeUserGroup(keyCategoryId, userGroupName), HttpStatus.OK);
    }

    /**
     * Removes the key from the key category with the specified ID.
     *
     * <p>This endpoint is mapped to "{@value RequestMappings#KEY_CATEGORIES}{@value RequestMappings#KEY_CATEGORY_KEYS}".
     *
     * @param keyCategoryId the ID of the key category the key will be deleted from
     * @param keyId the ID of the key to be deleted
     * @return the updated key category as response entity
     */
    @DeleteMapping(value = KEY_CATEGORY_KEYS)
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<KeyCategory> removeKey(@PathVariable Long keyCategoryId, @RequestParam Long keyId) {
        return new ResponseEntity<>(keyCategoryService.removeKey(keyCategoryId, keyId), HttpStatus.OK);
    }

    /**
     * Sets the responsible user for the key category with the specified ID.
     *
     * <p>This endpoint is mapped to "{@value RequestMappings#KEY_CATEGORIES}{@value RequestMappings#KEY_CATEGORY_RESPONSIBLE}".
     *
     * @param keyCategoryId the ID of the key category to set the responsible user of
     * @param userLogin the login of the user to be set as the responsible of the key category
     * @return the updated key category entity
     * @throws KeyCategoryNotFoundException if the key category with the specified ID has not been found
     * @throws UserNotFoundException if the user with the specified login has not been found
     */
    @PutMapping(value = KEY_CATEGORY_RESPONSIBLE)
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<KeyCategory> setResponsibleUser(@PathVariable Long keyCategoryId, @RequestParam String userLogin) {
        return new ResponseEntity<>(keyCategoryService.setResponsibleUser(keyCategoryId, userLogin), HttpStatus.OK);
    }

    /**
     * Updates a key category with the specified payload.
     *
     * <p>This endpoint is mapped to "{@value RequestMappings#KEY_CATEGORIES}{@value RequestMappings#KEY_CATEGORIES_ID}".
     *
     * @param keyCategoryId the ID of the key category to update
     * @param payload the key request payload to update the key category with
     * @return the updated key category as response entity
     * @since 0.6.0
     */
    @PutMapping(value = KEY_CATEGORIES_ID)
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<KeyCategory> update(@PathVariable Long keyCategoryId, @Valid @RequestBody KeyCategoryPayload payload) {
        return new ResponseEntity<>(keyCategoryService.update(keyCategoryId, payload), HttpStatus.OK);
    }
}