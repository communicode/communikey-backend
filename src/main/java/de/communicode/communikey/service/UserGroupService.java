/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2016
 */
package de.communicode.communikey.service;

import de.communicode.communikey.domain.UserGroup;
import de.communicode.communikey.exception.UserGroupConflictException;
import de.communicode.communikey.exception.UserGroupNotFoundException;

import java.util.Optional;
import java.util.Set;

/**
 * A service to process {@link UserGroup} entities.
 *
 * @author sgreb@communicode.de
 * @since 0.2.0
 */
public interface UserGroupService {

    /**
     * Creates a new user group.
     *
     * @param userGroupName the name of the user group entity to create
     * @return the created user group entity
     * @throws UserGroupConflictException if a user group entity with the specified name already exists
     * @throws IllegalArgumentException if the specified name is empty
     */
    UserGroup create(String userGroupName) throws UserGroupConflictException, IllegalArgumentException;

    /**
     * Deletes the specified {@link UserGroup}.
     *
     * @param userGroupId the ID of the user group entity to delete
     * @throws UserGroupNotFoundException if the user group entity with the specified ID has not been found
     */
    void delete(long userGroupId) throws UserGroupNotFoundException;

    /**
     * Gets all user groups.
     *
     * @return a collection of all user group entities
     */
    Set<UserGroup> getAll();

    /**
     * Gets the user group with the specified ID.
     *
     * @param userGroupId the ID of the user group entity to get
     * @return the user group entity with the specified ID
     * @throws UserGroupNotFoundException if the user group with the specified ID has not been found
     */
    UserGroup getById(long userGroupId) throws UserGroupNotFoundException;

    /**
     * Gets the user group with the specified name.
     *
     * @param name the name to find the user group entity of
     * @return the user group entity with the specified name if found, {@link Optional#EMPTY} otherwise
     * @throws IllegalArgumentException if the specified name is empty
     */
    Optional<UserGroup> getByName(String userGroupName) throws IllegalArgumentException;

    /**
     * Modifies the name of the specified {@link UserGroup}.
     *
     * @param userGroupId the ID of the user group entity to modify the name of
     * @param newName the new name for the specified user group entity
     * @throws UserGroupNotFoundException if the user group entity with the specified ID has not been found
     * @throws UserGroupConflictException if a user group entity with the specified new name already exists
     * @throws IllegalArgumentException if the specified new name is empty
     */
    void modifyName(long userGroupId, String newName) throws UserGroupNotFoundException, UserGroupConflictException, IllegalArgumentException;

    /**
     * Saves the specified user group.
     *
     * @param userGroup the user group entity to save
     * @throws NullPointerException if the specified user group entity is null
     */
    UserGroup save(UserGroup userGroup) throws NullPointerException;

    /**
     * Validates the specified {@link UserGroup} entity.
     *
     * @param userGroupId the ID of the user group entity to validate
     * @return the user group if validated
     * @throws UserGroupNotFoundException if the user group entity with the specified ID has not been found
     */
    UserGroup validate(long userGroupId) throws UserGroupNotFoundException;
}