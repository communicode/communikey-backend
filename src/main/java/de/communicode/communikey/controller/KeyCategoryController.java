/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2017
 */
package de.communicode.communikey.controller;

import static de.communicode.communikey.controller.PathVariables.KEYCATEGORY_ID;
import static de.communicode.communikey.controller.PathVariables.KEY_ID;
import static de.communicode.communikey.controller.PathVariables.TAG_ID;
import static de.communicode.communikey.controller.RequestMappings.KEY_CATEGORIES;
import static de.communicode.communikey.controller.RequestMappings.KEY_CATEGORIES_HASHID;
import static de.communicode.communikey.controller.RequestMappings.KEY_CATEGORY_GROUPS;
import static de.communicode.communikey.controller.RequestMappings.KEY_CATEGORY_KEYS;
import static de.communicode.communikey.controller.RequestMappings.KEY_CATEGORY_RESPONSIBLE;
import static de.communicode.communikey.controller.RequestMappings.KEY_CATEGORY_MOVE;
import static de.communicode.communikey.controller.RequestMappings.KEY_CATEGORY_TAGS;
import static java.util.Objects.requireNonNull;

import de.communicode.communikey.domain.KeyCategory;
import de.communicode.communikey.exception.HashidNotValidException;
import de.communicode.communikey.exception.UserNotFoundException;
import de.communicode.communikey.security.AuthoritiesConstants;
import de.communicode.communikey.service.payload.KeyCategoryPayload;
import de.communicode.communikey.exception.KeyCategoryNotFoundException;
import de.communicode.communikey.service.KeyCategoryService;
import de.communicode.communikey.service.payload.KeyCategoryMovePayload;
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
    private final Hashids hashids;

    @Autowired
    public KeyCategoryController(KeyCategoryService keyCategoryService, Hashids hashids) {
        this.keyCategoryService = requireNonNull(keyCategoryService, "keyCategoryService must not be null!");
        this.hashids = requireNonNull(hashids, "hashids must not be null!");
    }

    /**
     * Adds a user group to a key category with the specified ID.
     *
     * <p>This endpoint is mapped to "{@value RequestMappings#KEY_CATEGORIES}{@value RequestMappings#KEY_CATEGORY_GROUPS}".
     *
     * @param keyCategoryHashid the ID of the parent key category to add the child to
     * @param userGroupId the ID of the user group to be added
     * @return the updated parent key category as response entity
     */
    @GetMapping(value = KEY_CATEGORY_GROUPS)
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<KeyCategory> addUserGroup(@PathVariable(name = KEYCATEGORY_ID) String keyCategoryHashid, @RequestParam Long userGroupId) {
        return new ResponseEntity<>(keyCategoryService.addUserGroup(decodeSingleValueHashid(keyCategoryHashid), userGroupId), HttpStatus.OK);
    }

    /**
     * Adds the key to the key category with the specified ID.
     *
     * <p>This endpoint is mapped to "{@value RequestMappings#KEY_CATEGORIES}{@value RequestMappings#KEY_CATEGORY_KEYS}".
     *
     * @param keyCategoryHashid the ID of the key category to add the key to
     * @param keyHashid the Hashid of the key to be added
     * @return the updated key category entity
     */
    @GetMapping(value = KEY_CATEGORY_KEYS)
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<KeyCategory> addKey(@PathVariable(name = KEYCATEGORY_ID) String keyCategoryHashid, @RequestParam(name = KEY_ID) String keyHashid) {
        return new ResponseEntity<>(keyCategoryService.addKey(decodeSingleValueHashid(keyCategoryHashid),
                                                              decodeSingleValueHashid(keyHashid)),
                                    HttpStatus.OK);
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
     * <p>This endpoint is mapped to {@value RequestMappings#KEYS}{@value RequestMappings#KEY_CATEGORIES_HASHID}.
     *
     * @param keyCategoryHashid the Hashid of the key category to delete
     * @return a empty response entity
     */
    @DeleteMapping(value = KEY_CATEGORIES_HASHID)
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<Void> delete(@PathVariable(name = KEYCATEGORY_ID) String keyCategoryHashid) {
        keyCategoryService.delete(decodeSingleValueHashid(keyCategoryHashid));
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
     * <p>This endpoint is mapped to "{@value RequestMappings#KEY_CATEGORIES}{@value RequestMappings#KEY_CATEGORIES_HASHID}".
     *
     * @param keyCategoryHashid the Hashid of the key category entity to get
     * @return the key category entity
     * @throws KeyCategoryNotFoundException if the key category entity with the specified ID has not been found
     */
    @GetMapping(value = KEY_CATEGORIES_HASHID)
    @Secured(AuthoritiesConstants.USER)
    ResponseEntity<KeyCategory> get(@PathVariable(name = KEYCATEGORY_ID) String keyCategoryHashid) throws KeyCategoryNotFoundException {
        return new ResponseEntity<>(keyCategoryService.get(decodeSingleValueHashid(keyCategoryHashid)), HttpStatus.OK);
    }

    /**
     * Gets all key category entities.
     *
     * <p>This endpoint is mapped to "{@value RequestMappings#KEY_CATEGORIES}".
     *
     * @return a collection of key categories
     */
    @GetMapping
    @Secured(AuthoritiesConstants.USER)
    ResponseEntity<Set<KeyCategory>> getAll() {
        return new ResponseEntity<>(keyCategoryService.getAll(), HttpStatus.OK);
    }

    /**
     * Removes a user group from the key category with the specified ID.
     *
     * <p>This endpoint is mapped to "{@value RequestMappings#KEY_CATEGORIES}{@value RequestMappings#KEY_CATEGORY_GROUPS}".
     *
     * @param keyCategoryHashid the hashid of the key category to remove the user group from
     * @param userGroupId the ID of the user group to be removed from the key category
     * @return the updated key category as response entity
     */
    @DeleteMapping(value = KEY_CATEGORY_GROUPS)
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<KeyCategory> removeUserGroup(@PathVariable(name = KEYCATEGORY_ID) String keyCategoryHashid, @RequestParam Long userGroupId) {
        return new ResponseEntity<>(keyCategoryService.removeUserGroup(decodeSingleValueHashid(keyCategoryHashid), userGroupId), HttpStatus.OK);
    }

    /**
     * Removes the key from the key category with the specified ID.
     *
     * <p>This endpoint is mapped to "{@value RequestMappings#KEY_CATEGORIES}{@value RequestMappings#KEY_CATEGORY_KEYS}".
     *
     * @param keyCategoryHashid the hashid of the key category the key will be deleted from
     * @param keyHashid the hashid of the key to be deleted
     * @return the updated key category as response entity
     */
    @DeleteMapping(value = KEY_CATEGORY_KEYS)
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<KeyCategory> removeKey(@PathVariable(name = KEYCATEGORY_ID) String keyCategoryHashid, @RequestParam(name = KEY_ID) String keyHashid) {
        return new ResponseEntity<>(keyCategoryService.removeKey(decodeSingleValueHashid(keyCategoryHashid),
                                                                 decodeSingleValueHashid(keyHashid)),
                                    HttpStatus.OK);
    }

    /**
     * Sets the responsible user for the key category with the specified ID.
     *
     * <p>This endpoint is mapped to "{@value RequestMappings#KEY_CATEGORIES}{@value RequestMappings#KEY_CATEGORY_RESPONSIBLE}".
     *
     * @param keyCategoryHashid the hashid of the key category to set the responsible user of
     * @param userLogin the login of the user to be set as the responsible of the key category
     * @return the updated key category entity
     * @throws KeyCategoryNotFoundException if the key category with the specified ID has not been found
     * @throws UserNotFoundException if the user with the specified login has not been found
     */
    @PutMapping(value = KEY_CATEGORY_RESPONSIBLE)
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<KeyCategory> setResponsibleUser(@PathVariable(name = KEYCATEGORY_ID) String keyCategoryHashid, @RequestParam String userLogin) {
        return new ResponseEntity<>(keyCategoryService.setResponsibleUser(decodeSingleValueHashid(keyCategoryHashid), userLogin), HttpStatus.OK);
    }

    /**
     * Updates a key category with the specified payload.
     *
     * <p>This endpoint is mapped to "{@value RequestMappings#KEY_CATEGORIES}{@value RequestMappings#KEY_CATEGORIES_HASHID}".
     *
     * @param keyCategoryHashid the hashid of the key category to update
     * @param payload the key request payload to update the key category with
     * @return the updated key category as response entity
     * @since 0.6.0
     */
    @PutMapping(value = KEY_CATEGORIES_HASHID)
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<KeyCategory> update(@PathVariable(name = KEYCATEGORY_ID) String keyCategoryHashid, @Valid @RequestBody KeyCategoryPayload payload) {
        return new ResponseEntity<>(keyCategoryService.update(decodeSingleValueHashid(keyCategoryHashid), payload), HttpStatus.OK);
    }

    /**
     * Moves a category
     *
     * <p>This endpoint is mapped to "{@value RequestMappings#KEY_CATEGORIES}{@value RequestMappings#KEY_CATEGORY_MOVE}".
     *
     * @param keyCategorySourceHashid the hashid of the key category to move
     * @param payload The moveKeyCategoryPayload which contains the wanted parent
     * @return the updated key category as response entity
     * @since 0.17.0
     */
    @PostMapping(value = KEY_CATEGORY_MOVE)
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<KeyCategory> move(@PathVariable(name = KEYCATEGORY_ID) String keyCategorySourceHashid, @Valid @RequestBody KeyCategoryMovePayload payload) {
        return new ResponseEntity<>(keyCategoryService.move(decodeSingleValueHashid(keyCategorySourceHashid), payload), HttpStatus.OK);
    }

    /**
     * Adds the specified tag to the key category with the specified ID.
     *
     * <p>This endpoint is mapped to "{@value RequestMappings#KEY_CATEGORIES}{@value RequestMappings#KEY_CATEGORY_TAGS}".
     *
     * @param keyCategoryHashid the ID of the key category to add the tag to
     * @param tagHashid the Hashid of the tag to be added
     * @return the updated key category entity
     * @since 0.18.0
     */
    @GetMapping(value = KEY_CATEGORY_TAGS)
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<KeyCategory> addTag(@PathVariable(name = KEYCATEGORY_ID) String keyCategoryHashid, @RequestParam(name = TAG_ID) String tagHashid) {
        return new ResponseEntity<>(keyCategoryService.addTag(decodeSingleValueHashid(keyCategoryHashid),
            decodeSingleValueHashid(tagHashid)),
            HttpStatus.OK);
    }

    /**
     * Removes the specified tag from the key category with the specified ID.
     *
     * <p>This endpoint is mapped to "{@value RequestMappings#KEY_CATEGORIES}{@value RequestMappings#KEY_CATEGORY_TAGS}".
     *
     * @param keyCategoryHashid the ID of the key category to add the tag to
     * @param tagHashid the Hashid of the tag to be added
     * @return the updated key category entity
     * @since 0.18.0
     */
    @DeleteMapping(value = KEY_CATEGORY_TAGS)
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<KeyCategory> removeTag(@PathVariable(name = KEYCATEGORY_ID) String keyCategoryHashid, @RequestParam(name = TAG_ID) String tagHashid) {
        return new ResponseEntity<>(keyCategoryService.removeTag(decodeSingleValueHashid(keyCategoryHashid),
            decodeSingleValueHashid(tagHashid)),
            HttpStatus.OK);
    }

    /**
     * Decodes the specified Hashid.
     *
     * @param hashid the hashid to decode
     * @return the decoded hashid if valid
     * @throws HashidNotValidException if the Hashid is invalid
     * @since 0.12.0
     */
    private Long decodeSingleValueHashid(String hashid) throws HashidNotValidException {
        long[] decodedHashid = hashids.decode(hashid);
        if (decodedHashid.length == 0) {
            throw new HashidNotValidException();
        }
        return decodedHashid[0];
    }
}
