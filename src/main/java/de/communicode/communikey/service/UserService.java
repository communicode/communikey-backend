/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2016
 */
package de.communicode.communikey.service;

import de.communicode.communikey.domain.User;
import de.communicode.communikey.exception.UserConflictException;
import de.communicode.communikey.exception.UserNotFoundException;

import java.util.Optional;
import java.util.Set;

/**
 * A service to process {@link User} entities.
 *
 * @author sgreb@communicode.de
 * @since 0.2.0
 */
public interface UserService {

    /**
     * Creates a new user.
     *
     * @param email the email of the user entity to create
     * @param password the password of the user entity to create
     * @return the created user entity
     * @throws UserConflictException if a user entity with the specified username already exists
     * @throws IllegalArgumentException if the specified username or password is empty
     */
    User create(String email, String password) throws UserConflictException, IllegalArgumentException;

    /**
     * Deletes the specified user.
     *
     * @param userId the ID of the user entity to delete
     * @throws UserNotFoundException if the user with the specified ID has not been found
     */
    void delete(long userId) throws UserNotFoundException;

    /**
     * Gets all user entities.
     *
     * @return a collection of all user entities
     */
    Set<User> getAll();

    /**
     * Gets the user with the specified email.
     *
     * @param email the email to find the user entity of
     * @return the user entity with the specified email if found
     * @throws UserNotFoundException if the user entity with the specified email has not been found
     * @throws IllegalArgumentException if the specified email is empty
     */
    User getByEmail(String email) throws UserNotFoundException, IllegalArgumentException;

    /**
     * Gets the user with the specified ID.
     *
     * @param userId the ID of the user entity to get
     * @return the user entity with the specified ID
     * @throws UserNotFoundException if the user with the specified ID has not been found
     */
    User getById(long userId) throws UserNotFoundException;

    /**
     * Modifies the email of the specified user.
     *
     * @param userId the ID of the user entity to modify the email of
     * @param newEmail the new email for the specified user entity
     * @throws UserNotFoundException if the user with the specified ID has not been found
     * @throws UserConflictException if a user entity with the specified new email already exists
     * @throws IllegalArgumentException if the specified new email is empty
     */
    void modifyEmail(long userId, String newEmail) throws UserNotFoundException, UserConflictException, IllegalArgumentException;

    /**
     * Modifies the role of the specified user.
     *
     * @param userId the ID of the user to modify the role of
     * @param newRole the new role type
     * @throws UserNotFoundException if the specified {@code user} has not been found
     */
    //void modifyRole(long userId, UserRoleType newRole) throws UserNotFoundException;

    /**
     * Saves the specified user.
     *
     * @param user the user entity to save
     * @return the saved user entity
     * @throws NullPointerException if the specified user entity is null
     */
    User save(User user) throws NullPointerException;

    /**
     * Sets the activation status of the specified user.
     *
     * @param userId the ID of the user entity to set the activation status of
     * @param isEnabled the new activation status
     * @throws UserNotFoundException if the user entity with the specified ID has not been found
     */
    void setEnabled(long userId, boolean isEnabled) throws UserNotFoundException;

    /**
     * Validates the specified {@link User} entity.
     *
     * @param userId the ID of the user entity to validate
     * @return the user entity if validated
     * @throws UserNotFoundException if the user entity with the specified ID has not been found
     */
    User validate(long userId) throws UserNotFoundException;
}
