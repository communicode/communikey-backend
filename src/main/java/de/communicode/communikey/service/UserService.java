/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2016
*/
package de.communicode.communikey.service;

import de.communicode.communikey.domain.User;
import de.communicode.communikey.type.UserRoleType;

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
     */
    void deleteUser(User user);

    /**
     * Gets all {@link User} entities of the {@link de.communicode.communikey.repository.UserRepository}.
     *
     * @return a {@link Iterable<User>} of all {@link User} entities
     */
    Iterable<User> getAllUsers();

    /**
     * Gets the {@link User} with the given {@code id}.
     *
     * @param id The ID of the {@link User}
     * @return the {@link User} with the given ID
     */
    User getUserById(long id);

    /**
     * Gets the {@link User} with the given {@code username}.
     *
     * @param username The username of the {@link User}
     * @return the {@link User} with the given username
     */
    User getUserByUsername(String username);

    /**
     * Modifies the username of the given {@link User}.
     *
     * @param user The {@link User} to modify the username of
     * @param newUsername The new username for the given {@link User}
     */
    void modifyUsername(User user, String newUsername);

    /**
     * Modifies the role of the given {@link User}.
     *
     * @param user The {@link User} to modify the role of
     * @param newRole The new role for the given {@link User}
     */
    void modifyUserRole(User user, UserRoleType newRole);

    /**
     * Sets the activation status of the given {@link User}.
     *
     * @param user The {@link User} to set the activation status of
     * @param isEnabled The new activation status for the given {@link User}
     */
    void setUserEnabled(User user, boolean isEnabled);
}
