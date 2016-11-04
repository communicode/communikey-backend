/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2016
 */
package de.communicode.communikey.service;

import de.communicode.communikey.domain.UserGroup;

import java.util.Set;

/**
 * A service to interact with {@link UserGroup} entities via the {@link de.communicode.communikey.repository.UserGroupRepository}.
 *
 * @author sgreb@communicode.de
 * @since 0.2.0
 */
public interface UserGroupService {
    /**
     * Creates a new {@link UserGroup}.
     *
     * @param name The name of the {@link UserGroup} to create
     * @throws NullPointerException if the given {@code name} is null
     * @throws IllegalArgumentException if the given name already exists or is empty
     */
    void create(String name) throws NullPointerException, IllegalArgumentException;

    /**
     * Deletes the given {@link UserGroup}.
     *
     * @param userGroup The {@link UserGroup} to delete
     * @throws NullPointerException if the given {@code userGroup} is null
     */
    void delete(UserGroup userGroup) throws NullPointerException;

    /**
     * Gets all {@link UserGroup} entities of the {@link de.communicode.communikey.repository.UserGroupRepository}.
     *
     * @return a collection of all {@link UserGroup} entities
     */
    Set<UserGroup> getAllUserGroups();

    /**
     * Gets the {@link UserGroup} with the given {@code id}.
     *
     * @param id The ID of the {@link UserGroup}
     * @return the {@link UserGroup} with the given ID
     * @throws NullPointerException if the given {@code id} is null
     * @throws IllegalArgumentException if the given {@code id} is less than or equal to {@code 0}
     */
    UserGroup getById(long id) throws NullPointerException, IllegalArgumentException;

    /**
     * Gets the {@link UserGroup} with the given {@code name}.
     *
     * @param name The name of the {@link UserGroup}
     * @return the {@link UserGroup} with the given name
     * @throws NullPointerException if the given {@code name} is null
     * @throws IllegalArgumentException if the given name is empty
     */
    UserGroup getByName(String name) throws NullPointerException, IllegalArgumentException;

    /**
     * Modifies the name of the given {@link UserGroup}.
     *
     * @param userGroup The {@link UserGroup} to modify the name of
     * @param newName The new name for the given {@link UserGroup}
     * @throws NullPointerException if the given {@code userGroup} is null
     * @throws IllegalArgumentException if the given new name already exists or is empty
     */
    void modifyName(UserGroup userGroup, String newName) throws NullPointerException, IllegalArgumentException;

    /**
     * Saves the given {@code userGroup} into the {@link de.communicode.communikey.repository.UserGroupRepository}.
     *
     * @param userGroup The {@link UserGroup} to save
     * @throws NullPointerException if the given {@code userGroup} is null
     */
    void save(UserGroup userGroup) throws NullPointerException;
}
