/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2017
 */
package de.communicode.communikey.service;

import static de.communicode.communikey.security.SecurityUtils.getCurrentUserLogin;
import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;

import de.communicode.communikey.domain.Key;
import de.communicode.communikey.domain.KeyCategory;
import de.communicode.communikey.domain.User;
import de.communicode.communikey.domain.UserGroup;
import de.communicode.communikey.exception.KeyCategoryConflictException;
import de.communicode.communikey.exception.KeyCategoryNotFoundException;
import de.communicode.communikey.exception.KeyNotFoundException;
import de.communicode.communikey.exception.UserGroupNotFoundException;
import de.communicode.communikey.exception.UserNotFoundException;
import de.communicode.communikey.repository.KeyCategoryRepository;
import de.communicode.communikey.repository.KeyRepository;
import de.communicode.communikey.repository.UserGroupRepository;
import de.communicode.communikey.repository.UserRepository;
import de.communicode.communikey.service.payload.KeyCategoryPayload;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hashids.Hashids;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * The REST API service to process {@link KeyCategory} entities via a {@link KeyCategoryRepository}.
 *
 * @author sgreb@communicode.de
 * @author dvonderbey@communicode.de
 * @since 0.2.0
 */
@Service
public class KeyCategoryService {

    private static final Logger log = LogManager.getLogger();
    private final KeyCategoryRepository keyCategoryRepository;
    private final KeyRepository keyRepository;
    private final UserService userService;
    private final UserRepository userRepository;
    private final KeyService keyService;
    private final UserGroupService userGroupService;
    private final UserGroupRepository userGroupRepository;
    private final Hashids hashids;

    @Autowired
    public KeyCategoryService(KeyCategoryRepository keyCategoryRepository, UserService userService, KeyService keyService, KeyRepository keyRepository,
                              UserRepository userRepository, UserGroupService userGroupService, UserGroupRepository userGroupRepository, Hashids hashids) {
        this.keyCategoryRepository = requireNonNull(keyCategoryRepository, "keyCategoryRepository must not be null!");
        this.userService = requireNonNull(userService, "userService must not be null!");
        this.keyService = requireNonNull(keyService, "keyService must not be null!");
        this.keyRepository = requireNonNull(keyRepository, "keyRepository must not be null!");
        this.userRepository = requireNonNull(userRepository, "userRepository must not be null!");
        this.userGroupService = requireNonNull(userGroupService, "userGroupService must not be null!");
        this.userGroupRepository = requireNonNull(userGroupRepository, "userGroupRepository must not be null!");
        this.hashids = requireNonNull(hashids, "hashids must not be null!");
    }

    /**
     * Adds a key category as a child of a parent key category.
     *
     * @param parentKeyCategoryId the ID of the parent key category to add the child key category to
     * @param childKeyCategoryId the ID of the parent key category to be added to the parent key category
     * @return the updated parent key category
     * @throws KeyCategoryNotFoundException if a key category with specified ID has not been found
     */
    public KeyCategory addChild(Long parentKeyCategoryId, Long childKeyCategoryId) throws KeyCategoryNotFoundException {
        KeyCategory child = this.validate(childKeyCategoryId);
        KeyCategory parent = validate(parentKeyCategoryId);

        if (Objects.equals(parentKeyCategoryId, childKeyCategoryId)) {
            throw new KeyCategoryConflictException(
                "parent key category ID '" + parentKeyCategoryId + "' equals child key category ID '" + childKeyCategoryId + "'");
        }
        if (child.getTreeLevel() < parent.getTreeLevel()) {
            checkPathCollision(parent, childKeyCategoryId);
        }
        validateUniqueKeyCategoryName(child.getName(), parentKeyCategoryId);

        if (parent.addChild(child)) {
            child.setParent(parent);
            child.setTreeLevel(parent.getTreeLevel() + 1);
            updateChildrenTreeLevel(child);
            log.debug("Added key category with ID '{}' as child to key category with ID '{}'", child.getId(), parent.getId());
            return keyCategoryRepository.save(parent);
        }
        return parent;
    }

    /**
     * Adds a user group to a key category with the specified ID.
     *
     * @param keyCategoryId the ID of the key category to add the user group to
     * @param userGroupId the ID of the user group to be added to the key category
     * @return the updated key category
     * @throws KeyCategoryNotFoundException if the key category with specified ID has not been found
     * @throws UserGroupNotFoundException if the user group with specified name has not been found
     */
    public KeyCategory addUserGroup(Long keyCategoryId, Long userGroupId) throws KeyCategoryNotFoundException, UserGroupNotFoundException {
        KeyCategory keyCategory = validate(keyCategoryId);
        UserGroup userGroup = userGroupService.validate(userGroupId);

        if (keyCategory.addGroup(userGroup)) {
            userGroup.addCategory(keyCategory);
            userGroupRepository.save(userGroup);
            log.debug("Added user group '{}' to key category with ID '{}'", userGroup.getName(), keyCategoryId);
            return keyCategoryRepository.save(keyCategory);
        }
         return keyCategory;
    }

    /**
     * Adds the key to the key category with the specified ID.
     *
     * @param keyCategoryId the ID of the key category the key will be added to
     * @param keyId the ID of the key to be added
     * @return the updated key category
     * @throws KeyCategoryNotFoundException if the key category with the specified ID has not been found
     * @throws KeyNotFoundException if the key with the specified ID has not been found
     */
    public KeyCategory addKey(Long keyCategoryId, Long keyId) {
        KeyCategory keyCategory = validate(keyCategoryId);
        Key key = keyService.validate(keyId);
        key.setCategory(keyCategory);
        key = keyRepository.save(key);
        keyCategory.addKey(key);
        keyCategory = keyCategoryRepository.save(keyCategory);
        log.debug("Added key with ID '{}' to key category with ID '{}'", keyId, keyCategoryId);
        return keyCategory;
    }

    /**
     * Creates a new key category.
     *
     * @param payload the key category payload
     * @return the created key category
     * @throws KeyCategoryNotFoundException if the parent key category with the specified ID has not been found
     * @throws UserNotFoundException if the user with the specified ID has not been found
     */
    public KeyCategory create(KeyCategoryPayload payload) throws KeyCategoryNotFoundException, UserNotFoundException {
        String name = payload.getName();
        validateUniqueKeyCategoryName(name, null);

        KeyCategory keyCategory = new KeyCategory();
        User user = userService.validate(getCurrentUserLogin());

        keyCategory.setName(name);
        keyCategory.setCreator(user);
        keyCategory = keyCategoryRepository.save(keyCategory);
        keyCategory.setHashid(hashids.encode(keyCategory.getId()));
        keyCategory = keyCategoryRepository.save(keyCategory);
        setResponsibleUser(keyCategory.getId(), user.getLogin());

        user.addResponsibleKeyCategory(keyCategory);
        userRepository.save(user);
        log.debug("Created new key category with ID '{}'", keyCategory.getId());
        return keyCategory;
    }

    /**
     * Deletes the key category with the specified ID.
     *
     * <p><strong>This is a recursive operation that deletes all children key categories!</strong>
     *
     * @param keyCategoryId the ID of the key category to delete
     * @throws KeyCategoryNotFoundException if the key category with the specified ID has been found
     */
    public void delete(Long keyCategoryId) throws KeyCategoryNotFoundException {
        KeyCategory keyCategory = validate(keyCategoryId);
        keyCategory = dissolveReferences(keyCategory);
        keyCategoryRepository.delete(keyCategory);
        log.debug("Deleted key category with ID '{}'", keyCategoryId);
    }

    /**
     * Deletes all key categories.
     */
    public void deleteAll() {
        keyCategoryRepository.deleteAll();
        log.debug("Deleted all key categories");
    }

    /**
     * Gets all key categories.
     *
     * @return a collection of key categories
     */
    public Set<KeyCategory> getAll() {
        return new HashSet<>(keyCategoryRepository.findAll());
    }

    /**
     * Gets the key category with the specified ID.
     *
     * @param keyCategoryId the ID of the key category to find
     * @return the found key category entity
     * @throws KeyCategoryNotFoundException if the key category entity with the specified ID has not been found
     */
    public KeyCategory get(Long keyCategoryId) throws KeyCategoryNotFoundException {
        return validate(keyCategoryId);
    }

    /**
     * Removes a user group from the key category with the specified ID.
     *
     * @param keyCategoryId the ID of the key category to remove the user group from
     * @param userGroupId the ID of the user group to be removed from the key category
     * @return the updated key category
     * @throws KeyCategoryNotFoundException if the key category with specified ID has not been found
     * @throws UserGroupNotFoundException if the user group with specified name has not been found
     */
    public KeyCategory removeUserGroup(Long keyCategoryId, Long userGroupId) throws KeyCategoryNotFoundException, UserGroupNotFoundException {
        KeyCategory keyCategory = validate(keyCategoryId);
        UserGroup userGroup = userGroupService.validate(userGroupId);

        if (keyCategory.removeGroup(userGroup)) {
            userGroup.removeCategory(keyCategory);
            userGroupRepository.save(userGroup);
            userGroup.getUsers().forEach(keyService::removeObsoletePasswords);
            log.debug("Removed user group with name '{}' from key category with ID '{}'", userGroup.getName(), keyCategoryId);
            return keyCategoryRepository.save(keyCategory);
        }
        return keyCategory;
    }

    /**
     * Removes the key from the key category with the specified ID.
     *
     * @param keyCategoryId the ID of the key category the key will be deleted from
     * @param keyId the ID of the key to be deleted
     * @return the updated key category
     * @throws KeyCategoryNotFoundException if the key category with the specified ID has not been found
     * @throws KeyNotFoundException if the key with the specified ID has not been found
     */
    public KeyCategory removeKey(Long keyCategoryId, Long keyId) {
        KeyCategory keyCategory = validate(keyCategoryId);
        Key key = keyService.validate(keyId);

        key.setCategory(null);
        key = keyRepository.save(key);
        keyCategory.removeKey(key);
        keyCategory = keyCategoryRepository.save(keyCategory);
        log.debug("Removed key with ID '{}' from key category with ID '{}'", keyId, keyCategoryId);
        return keyCategory;
    }

    /**
     * Sets the responsible user for the key category with the specified ID.
     *
     * @param keyCategoryId the ID of the key category to set the responsible user of
     * @param userLogin the login of the user to be set as the responsible of the key category
     * @return the updated key category entity
     * @throws KeyCategoryNotFoundException if the key category with the specified ID has not been found
     * @throws UserNotFoundException if the user with the specified login has not been found
     */
    public KeyCategory setResponsibleUser(Long keyCategoryId, String userLogin) throws KeyCategoryNotFoundException, UserNotFoundException {
        User user = userService.validate(userLogin);
        KeyCategory keyCategory = validate(keyCategoryId);

        keyCategory.setResponsible(user);
        keyCategory = keyCategoryRepository.save(keyCategory);

        user.addResponsibleKeyCategory(keyCategory);
        userRepository.save(user);
        return keyCategory;
    }

    /**
     * Updates a key category with the specified payload.
     *
     * @param keyCategoryId the ID of the key category to update
     * @param payload the payload to update the key category with
     * @return the updated key category
     * @since 0.6.0
     */
    public KeyCategory update(Long keyCategoryId, KeyCategoryPayload payload) {
        KeyCategory keyCategory = validate(keyCategoryId);
        if (!keyCategory.getName().equals(payload.getName())) {
            validateUniqueKeyCategoryName(payload.getName(), ofNullable(keyCategory.getParent()).map(KeyCategory::getId).orElse(null));
            keyCategory.setName(payload.getName());
            keyCategory = keyCategoryRepository.save(keyCategory);
            log.debug("Updated key category with ID '{}'", keyCategory.getId());
        }
        return keyCategory;
    }

    /**
     * Validates the specified key category.
     *
     * @param keyCategoryId the ID of the key category to validate
     * @return the key category if validated
     * @throws KeyCategoryNotFoundException if the key category with the specified ID has not been found
     */
    public KeyCategory validate(Long keyCategoryId) throws KeyCategoryNotFoundException {
        return ofNullable(keyCategoryRepository.findOne(keyCategoryId)).orElseThrow(KeyCategoryNotFoundException::new);
    }

    /**
     * Validates that the specified key category name is unique within the tree level including the root.
     *
     * @param name the name of the key category to validate
     * @param parentKeyCategoryId the ID of the parent key category to validate
     * @throws KeyCategoryConflictException if the specified key category name is not unique
     */
    private void validateUniqueKeyCategoryName(String name, Long parentKeyCategoryId) throws KeyCategoryConflictException {
        if (Objects.nonNull(parentKeyCategoryId)) {
            if (ofNullable(keyCategoryRepository.findOne(parentKeyCategoryId))
                .map(keyCategory -> keyCategory.getChildren().stream()
                    .anyMatch(children -> children.getName().equals(name)))
                .orElse(true)) {
                throw new KeyCategoryConflictException("key category '" + name + "' already exists");
            }
        } else if (keyCategoryRepository.findAllByParentIsNull().stream()
                .anyMatch(keyCategory -> keyCategory.getName().equals(name))) {
            throw new KeyCategoryConflictException("key category '" + name + "' already exists");
        }
    }

    /**
     * Dissolves all references to connected {@link Key}, {@link UserGroup} and children key category entities.
     *
     * <p>Used to prepare the deletion of a key category.
     *
     * @param keyCategory the key category to dissolve all references of
     * @return the updated key category
     * @since 0.3.0
     */
    private KeyCategory dissolveReferences(KeyCategory keyCategory) {
        keyCategory.getKeys()
                .forEach(key -> {
                    key.setCategory(null);
                    keyRepository.save(key);
                    log.debug("Unbind key category with ID '{}' from key with ID '{}'", keyCategory.getId(), key.getId());
                });
        keyCategory.getGroups()
                .forEach(userGroup -> {
                    userGroup.removeCategory(keyCategory);
                    userGroupRepository.save(userGroup);
                    log.debug("Unbind key category with ID '{}' from user group with name '{}'", keyCategory.getId(), userGroup.getName());
                });
        keyCategory.getChildren().forEach(child -> delete(child.getId()));

        keyCategory.setParent(null);
        keyCategory.removeAllChildren();
        return keyCategoryRepository.save(keyCategory);
    }

    /**
     * Checks for a path collision when the level of the parent key category is higher than the level of the child key category.
     *
     * @param parent the parent key category
     * @param childKeyCategoryId the ID of the child key category
     * @since 0.9.0
     */
    private void checkPathCollision(KeyCategory parent, Long childKeyCategoryId) {
        if (parent.getParent() != null) {
            checkPathCollisionRecursively(parent.getParent(), childKeyCategoryId);
        }
    }

    /**
     * Checks recursively for a path collision when the level of the parent key category is higher than the level of the child key category.
     *
     * @param parent the parent key category
     * @param childKeyCategoryId the ID of the child key category
     * @since 0.9.0
     */
    private void checkPathCollisionRecursively(KeyCategory parent, Long childKeyCategoryId) {
        if (Objects.equals(parent.getId(), childKeyCategoryId)) {
            throw new KeyCategoryConflictException("key category with ID '" + parent.getId() + "' can not be set as own child reference");
        }
        if (parent.getParent() != null) {
            checkPathCollisionRecursively(parent.getParent(), childKeyCategoryId);
        }
    }

    /**
     * Recursively updates all direct- and indirect key category children tree level of the specified key category.
     *
     * @param keyCategory the key category to update all direct- and indirect children of
     * @since 0.9.0
     */
    private void updateChildrenTreeLevel(KeyCategory keyCategory) {
        for (KeyCategory child : keyCategory.getChildren()) {
            child.setTreeLevel(keyCategory.getTreeLevel() + 1);
            updateChildrenTreeLevel(child);
        }
    }
}
