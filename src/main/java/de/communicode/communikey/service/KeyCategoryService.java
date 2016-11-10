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
import de.communicode.communikey.repository.KeyCategoryRepository;

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
     * Deletes the given {@link KeyCategory}.
     *
     * <p>
     *     <strong>This is a recursive action that deletes all {@link Key} entities owned by this {@code category} and all {@link KeyCategory} entities where
     *     the given {@code category} is the parent of!</strong>
     *
     * @param category the {@link KeyCategory} to delete
     * @throws NullPointerException if the given {@code category} entity is null
     */
    void delete(KeyCategory category) throws NullPointerException;

    /**
     * Gets all {@link KeyCategory} entities of the {@link KeyCategoryRepository}.
     *
     * @return a collection of all found {@link KeyCategory} entities
     */
    Set<KeyCategory> getAll();

    /**
     * Gets all {@link KeyCategory} entities created by the given {@link User}.
     *
     * @param creator the {@link User} to get all {@link KeyCategory} entities of
     * @return a collection of all found {@link KeyCategory} entities
     * @throws UserNotFoundException if the given {@code creator} has not been found
     */
    Set<KeyCategory> getAllByCreator(User creator) throws UserNotFoundException;

    /**
     * Gets all {@link KeyCategory} entities with the given {@code name}.
     *
     * @param name the name of the {@link KeyCategory} entities to find
     * @return a collection of all found {@link KeyCategory} entities
     * @throws IllegalArgumentException if the given {@code name} is empty
     */
    Set<KeyCategory> getAllByName(String name) throws IllegalArgumentException;

    /**
     * Gets all {@link KeyCategory} entities owned by the given parent {@link KeyCategory}.
     *
     * @param category the parent {@link KeyCategory} to get all child entities of
     * @return a collection of all found {@link KeyCategory} entities
     * @throws KeyCategoryNotFoundException if the given {@code category} has not been found
     */
    Set<KeyCategory> getAllByParent(KeyCategory category) throws KeyCategoryNotFoundException;

    /**
     * Gets all {@link KeyCategory} entities the given {@link User} is responsible for.
     *
     * @param responsible the responsible {@link User} to get all {@link KeyCategory} entities of
     * @return a collection of all found {@link KeyCategory} entities
     * @throws UserNotFoundException if the given {@code responsible} has not been found
     */
    Set<KeyCategory> getAllByResponsible(User responsible) throws UserNotFoundException;

    /**
     * Gets all {@link Key} entities of the given {@link KeyCategory}.
     *
     * @param category the {@link KeyCategory} to get all {@link Key} entities of
     * @return a collection of all found {@link KeyCategory} entities
     * @throws KeyCategoryNotFoundException if the given {@code category} has not been found
     */
    Set<Key> getAllKeys(KeyCategory category) throws KeyCategoryNotFoundException;

    /**
     * Gets the {@link KeyCategory} with the given {@code id}.
     *
     * @param id the ID of the {@link KeyCategory}
     * @return the {@link KeyCategory} with the given ID
     * @throws KeyCategoryNotFoundException if the {@link KeyCategory} entity with the given {@code id} has not been found
     */
    KeyCategory getById(long id) throws KeyCategoryNotFoundException;

    /**
     * Gets the child {@link KeyCategory} entities of the given {@code category}.
     *
     * @param category the {@link KeyCategory} to get all child entities of
     * @return a collection all found child {@link KeyCategory} entities
     * @throws KeyCategoryNotFoundException if the given {@code category} has not been found
     */
    Set<KeyCategory> getChilds(KeyCategory category) throws KeyCategoryNotFoundException;

    /**
     * Gets the {@link Key} entity with the given {@code id} if owned by the given {@link KeyCategory}.
     *
     * @param id the ID of the {@link Key} entity to get of the the given {@code category}
     * @param category the {@link KeyCategory} which owns the {@link Key} entity
     * @return the {@link Key} entity if owned by the given {@code category}
     * @throws KeyCategoryNotFoundException if the given {@code category} has not been found
     */
    Optional<Key> getKey(long id, KeyCategory category) throws KeyCategoryNotFoundException;

    /**
     * Gets the parent {@link KeyCategory} of the given {@code category}.
     *
     * @param category the {@link KeyCategory} to get the parent of
     * @return the parent {@link Optional<KeyCategory>}
     */
    Optional<KeyCategory> getParent(KeyCategory category);

    /**
     * Checks if the given child {@link KeyCategory} is owned the given parent {@link KeyCategory} entity.
     *
     * @param parentCategory the parent {@link KeyCategory} entity to check for the given child {@code category} for
     * @param category the child {@link KeyCategory} to search
     * @return {@code true} if the given child {@code category} is owned by the given {@code parentCategory}, false otherwise
     * @throws KeyCategoryNotFoundException if the given {@code parentCategory} or {@code category} have not been found
     */
    boolean hasChild(KeyCategory parentCategory, KeyCategory category) throws KeyCategoryNotFoundException;

    /**
     * Checks if the given {@link KeyCategory} directly owns the given {@link Key} entity.
     *
     * @param key the {@link Key} entity to look for
     * @param category the {@link KeyCategory} to search
     * @return {@code true} if the given {@code category} directly owns the given {@code key}, false otherwise
     * @throws KeyNotFoundException if the given {@code key} has not been found
     * @throws KeyCategoryNotFoundException if the given {@code category} has not been found
     */
    boolean hasKey(Key key, KeyCategory category) throws KeyNotFoundException, KeyCategoryNotFoundException;

    /**
     * Modifies the name of the given {@link KeyCategory}.
     *
     * @param category the {@link KeyCategory} to modify the name of
     * @param newName the new name for the given {@link KeyCategory}
     * @throws KeyCategoryNotFoundException if the given {@code category} has not been found
     * @throws IllegalArgumentException if the given new name is empty
     */
    void modifyName(KeyCategory category, String newName) throws KeyCategoryNotFoundException, IllegalArgumentException;

    /**
     * Saves the given {@link KeyCategory} in the {@link KeyCategoryRepository}.
     *
     * @param category the {@link KeyCategory} to save
     * @return the saved {@link KeyCategory}
     * @throws NullPointerException if the given {@code category} is null
     */
    KeyCategory save(KeyCategory category) throws NullPointerException;

    /**
     * Validates that the given {@link KeyCategory} entity exists in the {@link de.communicode.communikey.repository.KeyCategoryRepository}.
     *
     * @param keyCategoryId the ID of the {@link KeyCategory} entity to validate
     * @return the validated {@link KeyCategory}
     * @throws KeyCategoryNotFoundException if the {@link KeyCategory} with the given {@code keyCategoryId} has not been found
     */
    KeyCategory validate(long keyCategoryId) throws KeyCategoryNotFoundException;
}