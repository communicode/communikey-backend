/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2016
 */
package de.communicode.communikey.service;

import de.communicode.communikey.domain.Key;
import de.communicode.communikey.domain.KeyCategory;
import de.communicode.communikey.domain.User;
import de.communicode.communikey.exception.KeyCategoryNotFoundException;
import de.communicode.communikey.exception.KeyNotFoundException;
import de.communicode.communikey.exception.UserNotFoundException;

import java.util.Optional;
import java.util.Set;

/**
 * A service to process {@link KeyCategory} entities.
 *
 * @author sgreb@communicode.de
 * @since 0.2.0
 */
public interface KeyCategoryService {

    /**
     * Deletes the specified key.
     * <p>
     *     <strong>This is a recursive action that deletes all key entities owned by this category and all key entities where the specified category is the parent of!</strong>
     *
     * @param keyCategoryId the ID of the key category to delete
     * @throws KeyCategoryNotFoundException if no key category entity with the specified ID has been found
     */
    void delete(long keyCategoryId) throws KeyCategoryNotFoundException;

    /**
     * Gets all key category entities.
     *
     * @return a collection of all key category entities
     */
    Set<KeyCategory> getAll();

    /**
     * Gets all key category entities created by the specified {@link User}.
     *
     * @param creatorUserId the ID of the user to get all key category entities of
     * @return a collection of found key category entities
     * @throws UserNotFoundException if the user with the specified ID has not been found
     */
    Set<KeyCategory> getAllByCreator(long creatorUserId) throws UserNotFoundException;

    /**
     * Gets all key category entities with the specified name.
     *
     * @param name the name of the key category entities to find
     * @return a collection of found key category entities
     * @throws IllegalArgumentException if the specified name is empty
     */
    Set<KeyCategory> getAllByName(String name) throws IllegalArgumentException;

    /**
     * Gets all key category entities owned by the specified parent key category.
     *
     * @param parentKeyCategoryId the ID of the parent key category to get all child entities of
     * @return a collection of child key category entities
     * @throws KeyCategoryNotFoundException if the key category with the specified ID has not been found
     */
    Set<KeyCategory> getAllByParent(long parentKeyCategoryId) throws KeyCategoryNotFoundException;

    /**
     * Gets all key category entities the specified {@link User} is responsible for.
     *
     * @param responsibleUserId the ID of the responsible user to get all key category entities of
     * @return a collection of found key category entities
     * @throws UserNotFoundException if the user wit the specified ID has not been found
     */
    Set<KeyCategory> getAllByResponsible(long responsibleUserId) throws UserNotFoundException;

    /**
     * Gets all {@link Key} entities of the specified key category.
     *
     * @param keyCategoryId the ID of the key category to get all key entities of
     * @return a collection of key entities
     * @throws KeyCategoryNotFoundException if the key category with the specified ID has not been found
     */
    Set<Key> getAllKeys(long keyCategoryId) throws KeyCategoryNotFoundException;

    /**
     * Gets the key category with the specified ID.
     *
     * @param keyCategoryId the ID of the key category to find
     * @return the found key category entity
     * @throws KeyCategoryNotFoundException if the key category entity with the specified ID has not been found
     */
    KeyCategory getById(long keyCategoryId) throws KeyCategoryNotFoundException;

    /**
     * Gets all child key category entities of the specified key category.
     *
     * @param keyCategoryId the ID of the key category to get all child entities of
     * @return a collection of child key category entities
     * @throws KeyCategoryNotFoundException if the key category with the specified ID has not been found
     */
    Set<KeyCategory> getChilds(long keyCategoryId) throws KeyCategoryNotFoundException;

    /**
     * Gets the {@link Key} entity with the specified ID if owned by the specified key category.
     *
     * @param keyId the ID of the key entity to get of the the specified key category
     * @param keyCategoryId the ID of the key category which owns the key entity
     * @return the key entity if owned by the specified key category
     * @throws KeyNotFoundException if the key with the specified ID has not been found
     * @throws KeyCategoryNotFoundException if the key category with the specified ID has not been found
     */
    Key getKey(long keyId, long keyCategoryId) throws KeyNotFoundException, KeyCategoryNotFoundException;

    /**
     * Gets the parent key category of the specified key category.
     * <p>
     *     Note that this can be {@code null} which implies that the specified key category is located in the root of the tree.
     *
     * @param keyCategoryId the key category entity to get the parent of
     * @return the parent key category if not located in the key category root, {@code null} otherwise
     */
    Optional<KeyCategory> getParent(long keyCategoryId);

    /**
     * Checks if the specified child key category is owned the specified parent key category entity.
     *
     * @param parentKeyCategoryId the ID of the parent key category entity to check for the specified child key category for
     * @param childKeyCategoryId the ID of the child key category entity to search
     * @return {@code true} if the specified child key category is owned by the specified parent key category, {@code false} otherwise
     * @throws KeyCategoryNotFoundException if the parent- or child key category with the specified IDs have not been found
     */
    boolean hasChild(long parentKeyCategoryId, long childKeyCategoryId) throws KeyCategoryNotFoundException;

    /**
     * Checks if the specified key category directly owns the specified {@link Key} entity.
     *
     * @param keyId the ID of the  key entity to look for
     * @param keyCategoryId the ID of the key category to search
     * @return {@code true} if the specified key category directly owns the specified key entity, {@code false} otherwise
     * @throws KeyNotFoundException if the key with the specified ID has not been found
     * @throws KeyCategoryNotFoundException if the key category with the specified ID has not been found
     */
    boolean hasKey(long keyId, long keyCategoryId) throws KeyNotFoundException, KeyCategoryNotFoundException;

    /**
     * Modifies the name of the specified {@link KeyCategory}.
     *
     * @param keyCategoryId the ID of the key category to modify the name of
     * @param newName the new name for the specified key category
     * @throws KeyCategoryNotFoundException if the key category with the specified ID has not been found
     * @throws IllegalArgumentException if the specified new name is empty
     */
    void modifyName(long keyCategoryId, String newName) throws KeyCategoryNotFoundException, IllegalArgumentException;

    /**
     * Saves the specified key category.
     *
     * @param keyCategory the key category entity to save
     * @return the saved key category entity
     * @throws NullPointerException if the specified key category entity is null
     */
    KeyCategory save(KeyCategory keyCategory) throws NullPointerException;

    /**
     * Validates the specified {@link KeyCategory} entity.
     *
     * @param keyCategoryId the ID of the key category entity to validate
     * @return the key category if validated
     * @throws KeyCategoryNotFoundException if the key category with the specified ID has not been found
     */
    KeyCategory validate(long keyCategoryId) throws KeyCategoryNotFoundException;
}