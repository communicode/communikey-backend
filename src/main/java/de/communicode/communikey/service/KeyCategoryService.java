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
import de.communicode.communikey.repository.KeyCategoryRepository;

import java.util.Optional;
import java.util.Set;

/**
 * A service to interact with the {@link KeyCategoryRepository}.
 *
 * @author sgreb@communicode.de
 * @since 0.2.0
 */
public interface KeyCategoryService {
    /**
     * Deletes the given {@link KeyCategory}.
     *
     * <p>
     *     <strong>This is a recursive action that deletes all {@link KeyCategory} entities where the given {@code category} is the parent of!</strong>
     *
     * @param category The {@link KeyCategory} to delete
     * @throws NullPointerException if the given {@code category} is null
     */
    void delete(KeyCategory category) throws NullPointerException;

    /**
     * Gets all {@link KeyCategory} entities of the {@link KeyCategoryRepository}.
     *
     * @return a collection of all {@link KeyCategory} entities
     */
    Set<KeyCategory> getAll();

    /**
     * Gets all {@link KeyCategory} entities created by the given {@link User}.
     *
     * @param creator the {@link User} to get all {@link KeyCategory} entities of
     * @return a collection of all {@link KeyCategory} entities
     * @throws NullPointerException if the given {@code creator} is null
     */
    Set<KeyCategory> getAllByCreator(User creator) throws NullPointerException;

    /**
     * Gets all {@link Key} entities of the given {@link KeyCategory}.
     *
     * @param category the {@link KeyCategory} to get all {@link Key} entities of
     * @return a collection of all {@link KeyCategory} entities
     * @throws NullPointerException if the given {@code category} is null
     */
    Set<Key> getAllKeys(KeyCategory category) throws NullPointerException;

    /**
     * Gets all {@link KeyCategory} entities with the given {@code name}.
     *
     * @param name the name of the {@link KeyCategory} entities to find
     * @return a collection of all {@link KeyCategory} entities
     * @throws IllegalArgumentException if the given {@code name} is empty
     */
    Set<KeyCategory> getAllByName(String name) throws IllegalArgumentException;

    /**
     * Gets all {@link KeyCategory} entities owned by the given parent {@link KeyCategory}.
     *
     * @param category The parent {@link KeyCategory} to get all child entities of
     * @return a collection of all {@link KeyCategory} entities
     * @throws NullPointerException if the given {@code category} is null
     */
    Set<KeyCategory> getAllByParent(KeyCategory category) throws NullPointerException;

    /**
     * Gets all {@link KeyCategory} entities the given {@link User} is responsible for.
     *
     * @param responsible the responsible {@link User} to get all {@link KeyCategory} entities of
     * @return a collection of all {@link KeyCategory} entities
     * @throws NullPointerException if the given {@code responsible} is null
     */
    Set<KeyCategory> getAllByResponsible(User responsible) throws NullPointerException;

    /**
     * Gets the {@link KeyCategory} with the given {@code id}.
     *
     * @param id The ID of the {@link KeyCategory}
     * @return the {@link KeyCategory} with the given ID
     * @throws NullPointerException if the given {@code id} is null
     */
    KeyCategory getById(long id) throws NullPointerException;

    /**
     * Gets the child {@link KeyCategory} entities of the given {@code category}.
     *
     * @return a collection of child {@link KeyCategory} entities
     * @throws NullPointerException if the given {@code category} is null
     */
    Set<KeyCategory> getChilds(KeyCategory category) throws NullPointerException;

    /**
     * Gets the {@link Key} entity with the given {@code id} if owned by the given {@link KeyCategory}.
     *
     * @param id the ID of the {@link Key} entity to get of the the given {@code category}
     * @param category the {@link KeyCategory} which owns the {@link Key} entity
     * @return the {@link Key} entity if owned by the given {@code category}
     * @throws NullPointerException if the given {@code category} is null
     */
    Optional<Key> getKey(long id, KeyCategory category) throws NullPointerException;

    /**
     * Gets the parent {@link KeyCategory} of the given {@code category}.
     *
     * @param category The {@link KeyCategory} to get the parent of
     * @return the parent {@link Optional<KeyCategory>}
     */
    Optional<KeyCategory> getParent(KeyCategory category);

    /**
     * Checks if the given child {@link KeyCategory} is owned the given parent {@link KeyCategory} entity.
     *
     * @param parentCategory the parent {@link KeyCategory} entity to check for the given child {@code category} for.
     * @param category the child {@link KeyCategory} to search
     * @return {@code true} if the given child {@code category} is owned by the given {@code parentCategory}, false otherwise
     * @throws NullPointerException if the given {@code parentCategory} or {@code category} is null
     */
    boolean hasChild(KeyCategory parentCategory, KeyCategory category) throws NullPointerException;

    /**
     * Checks if the given {@link KeyCategory} directly owns the given {@link Key} entity.
     *
     * @param key the {@link Key} entity to look for
     * @param category the {@link KeyCategory} to search
     * @return {@code true} if the given {@code category} directly owns the given {@code key}, false otherwise
     * @throws NullPointerException if the given {@code key} or {@code category} is null
     */
    boolean hasKey(Key key, KeyCategory category) throws NullPointerException;

    /**
     * Modifies the name of the given {@link KeyCategory}.
     *
     * @param category The {@link KeyCategory} to modify the name of
     * @param newName The new name for the given {@link KeyCategory}
     * @throws NullPointerException if the given {@code category} is null
     * @throws IllegalArgumentException if the given new name is empty
     */
    void modifyName(KeyCategory category, String newName) throws NullPointerException, IllegalArgumentException;

    /**
     * Saves the given {@link KeyCategory} in the {@link KeyCategoryRepository}.
     *
     * @param category The {@link KeyCategory} to save
     * @return the saved {@link KeyCategory}
     * @throws NullPointerException if the given {@code category} is null
     */
    KeyCategory save(KeyCategory category) throws NullPointerException;
}