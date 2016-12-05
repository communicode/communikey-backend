/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2016
 */
package de.communicode.communikey.service;

import de.communicode.communikey.domain.Role;
import de.communicode.communikey.exception.RoleConflictException;
import de.communicode.communikey.exception.RoleNotFoundException;

import java.util.Set;

/**
 * A service to process {@link Role} entities.
 *
 * @author sgreb@communicode.de
 * @since 0.2.0
 */
public interface RoleService {

    /**
     * Creates a new role.
     *
     * @param name the name of the role entity to create
     * @throws RoleConflictException if a role entity with the specified name already exists
     * @throws IllegalArgumentException if the specified name is empty
     */
    Role create(String name) throws RoleConflictException, IllegalArgumentException;

    /**
     * Deletes the specified role.
     *
     * @param roleId the ID of the role to delete
     * @throws RoleNotFoundException if the role entity with the specified ID has been found
     */
    void delete(long roleId) throws RoleNotFoundException;

    /**
     * Gets all role entities.
     *
     * @return a collection of all role entities
     */
    Set<Role> getAll();

    /**
     * Gets the role with the specified ID.
     *
     * @param roleId the ID of the role to find
     * @return the found role entity
     * @throws RoleNotFoundException if the role entity with the specified ID has not been found
     */
    Role getById(long roleId) throws RoleNotFoundException;

    /**
     * Get the role entity with the specified name.
     *
     * @param name the name of the role entity to find
     * @return the role entity, if found
     * @throws RoleNotFoundException if the role with the specified name has not been found
     * @throws IllegalArgumentException if the specified name is empty
     */
    Role getByName(String name) throws RoleNotFoundException, IllegalArgumentException;

    /**
     * Modifies the name of the specified {@link Role} entity.
     *
     * @param roleId the ID of the role to modify the name of
     * @param newName the new name for the specified role
     * @throws RoleNotFoundException if the role with the specified ID has not been found
     * @throws IllegalArgumentException if the specified new name is empty
     */
    void modifyName(long roleId, String newName) throws RoleNotFoundException, RoleConflictException, IllegalArgumentException;

    /**
     * Saves the specified role.
     *
     * @param role the role entity to save
     * @return the saved role entity
     * @throws NullPointerException if the specified role entity is null
     */
    Role save(Role role) throws NullPointerException;

    /**
     * Validates the specified {@link Role} entity.
     *
     * @param roleId the ID of the role entity to validate
     * @return the role if validated
     * @throws RoleNotFoundException if the role with the specified ID has not been found
     */
    Role validate(long roleId) throws RoleNotFoundException;
}