/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2016
*/
package de.communicode.communikey.service;

import de.communicode.communikey.domain.User;
import de.communicode.communikey.type.UserRoleType;

import java.util.Set;

/**
 * A service to interact with {@link User} entities via the {@link de.communicode.communikey.repository.UserRepository}.
 *
 * @author sgreb@communicode.de
 * @since 0.2.0
 */
public interface UserService {
    /**
     * Deletes the given {@link User}.
     *
     * @param user The {@link User} to delete
     * @throws NullPointerException if the given {@code user} is null
     */
    void deleteUser(User user) throws NullPointerException;

    /**
     * Gets all {@link User} entities of the {@link de.communicode.communikey.repository.UserRepository}.
     *
     * @return a collection of all {@link User} entities
     */
    Set<User> getAllUsers();

    /**
     * Gets the {@link User} with the given {@code id}.
     *
     * @param id The ID of the {@link User}
     * @return the {@link User} with the given ID
     * @throws NullPointerException if the given {@code id} is invalid
     */
    User getUserById(long id) throws NullPointerException;

    /**
     * Gets the {@link User} with the given {@code username}.
     *
     * @param username The username of the {@link User}
     * @return the {@link User} with the given username
     * @throws NullPointerException if the given {@code username} is invalid
     */
    User getUserByUsername(String username) throws NullPointerException;

    /**
     * Modifies the username of the given {@link User}.
     *
     * @param user The {@link User} to modify the username of
     * @param newUsername The new username for the given {@link User}
     * @throws NullPointerException if the given {@code user} is null
     * @throws IllegalArgumentException if the given new username already exists or is empty
     */
    void modifyUsername(User user, String newUsername) throws NullPointerException, IllegalArgumentException;

    /**
     * Modifies the role of the given {@link User}.
     *
     * @param user The {@link User} to modify the role of
     * @param newRole The new role for the given {@link User}
     * @throws NullPointerException if the given {@code user} is null
     */
    void modifyUserRole(User user, UserRoleType newRole) throws NullPointerException;

    /**
     * Sets the activation status of the given {@link User}.
     *
     * @param user The {@link User} to set the activation status of
     * @param isEnabled The new activation status for the given {@link User}
     * @throws NullPointerException if the given {@code user} is null
     */
    void setUserEnabled(User user, boolean isEnabled) throws NullPointerException;
}
