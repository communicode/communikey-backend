/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2016
 */
package de.communicode.communikey.service;

import de.communicode.communikey.domain.Privilege;
import de.communicode.communikey.exception.PrivilegeConflictException;
import de.communicode.communikey.exception.PrivilegeNotFoundException;

import java.util.Set;

/**
 * A service to process {@link Privilege} entities.
 *
 * @author sgreb@communicode.de
 * @since 0.2.0
 */
public interface PrivilegeService {

    /**
     * Creates a new privilege.
     *
     * @param name the name of the privilege entity to create
     * @throws PrivilegeConflictException if a privilege entity with the specified name already exists
     * @throws IllegalArgumentException if the specified name is empty
     */
    Privilege create(String name) throws PrivilegeConflictException, IllegalArgumentException;

    /**
     * Deletes the specified privilege.
     *
     * @param privilegeId the ID of the privilege to delete
     * @throws PrivilegeNotFoundException if the privilege entity with the specified ID has been found
     */
    void delete(long privilegeId) throws PrivilegeNotFoundException;

    /**
     * Gets all privilege entities.
     *
     * @return a collection of all privilege entities
     */
    Set<Privilege> getAll();

    /**
     * Gets the privilege with the specified ID.
     *
     * @param privilegeId the ID of the privilege to find
     * @return the found privilege entity
     * @throws PrivilegeNotFoundException if the privilege entity with the specified ID has not been found
     */
    Privilege getById(long privilegeId) throws PrivilegeNotFoundException;

    /**
     * Get the privilege entity with the specified name.
     *
     * @param name the name of the privilege entity to find
     * @return the privilege entity, if found
     * @throws PrivilegeNotFoundException if the privilege with the specified name has not been found
     * @throws IllegalArgumentException if the specified name is empty
     */
    Privilege getByName(String name) throws PrivilegeNotFoundException, IllegalArgumentException;

    /**
     * Modifies the name of the specified {@link Privilege} entity.
     *
     * @param privilegeId the ID of the privilege to modify the name of
     * @param newName the new name for the specified privilege
     * @throws PrivilegeNotFoundException if the privilege with the specified ID has not been found
     * @throws IllegalArgumentException if the specified new name is empty
     */
    void modifyName(long privilegeId, String newName) throws PrivilegeNotFoundException, PrivilegeConflictException, IllegalArgumentException;

    /**
     * Saves the specified privilege.
     *
     * @param privilege the privilege entity to save
     * @return the saved privilege entity
     * @throws NullPointerException if the specified privilege entity is null
     */
    Privilege save(Privilege privilege) throws NullPointerException;

    /**
     * Validates the specified {@link Privilege} entity.
     *
     * @param privilegeId the ID of the privilege entity to validate
     * @return the privilege if validated
     * @throws PrivilegeNotFoundException if the privilege with the specified ID has not been found
     */
    Privilege validate(long privilegeId) throws PrivilegeNotFoundException;
}