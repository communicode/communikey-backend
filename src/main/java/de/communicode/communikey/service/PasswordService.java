/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2016
 */
package de.communicode.communikey.service;

import de.communicode.communikey.domain.Password;

import java.sql.Timestamp;
import java.util.Set;

/**
 * A service to interact with the {@link de.communicode.communikey.repository.PasswordRepository}.
 *
 * @author sgreb@communicode.de
 * @since 0.1.0
 */
public interface PasswordService {
    /**
     * Gets all {@link Password} entities of the {@link de.communicode.communikey.repository.PasswordRepository}.
     *
     * @return a collection of all {@link Password} entities
     */
    Set<Password> getAllPasswords();

    /**
     * Gets the {@link Password} with the given {@code id}.
     *
     * @param id The ID of the {@link Password}
     * @return the {@link Password} with the given ID
     * @throws NullPointerException if the given {@code id} is invalid
     */
    Password getPasswordById(long id) throws NullPointerException;

    /**
     * Gets the first {@link Password} found with the given creation date.
     *
     * @param timestamp The {@link Timestamp} of a {@link Password} to find
     * @return a {@link Password} with the given creation timestamp
     * @throws NullPointerException if no password found with the given {@code timestamp}
     */
    Password getPasswordByCreationDate(Timestamp timestamp) throws NullPointerException;

    /**
     * Deletes the given {@link Password}.
     *
     * @param password The {@link Password} to delete
     * @throws NullPointerException if the given {@code password} is null
     */
    void deletePassword(Password password) throws NullPointerException;

    /**
     * Modifies the value of the given {@link Password}.
     *
     * @param password The {@link Password} to modify the value of
     * @param newValue The new value for the given {@link Password}
     * @throws NullPointerException if the given {@code password} is null
     */
    void modifyPasswordValue(Password password, String newValue) throws NullPointerException;

    /**
     * Saves the given {@link Password} in the {@link de.communicode.communikey.repository.PasswordRepository}.
     *
     * @param password The {@link Password} to save
     * @return the saved {@link Password}
     * @throws NullPointerException if the given {@code password} is null
     */
    Password savePassword(Password password) throws NullPointerException;
}