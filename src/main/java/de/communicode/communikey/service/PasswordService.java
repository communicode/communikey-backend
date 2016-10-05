/*
  * Copyright (C) 2016 communicode AG
 */
package de.communicode.communikey.service;

import de.communicode.communikey.domain.Password;

import java.sql.Timestamp;

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
     * @return a {@link Iterable<Password>} of all {@link Password} entities
     */
    Iterable<Password> getAllPasswords();

    /**
     * Gets the {@link Password} with the given {@code id}.
     *
     * @param id The ID of the {@link Password}
     * @return the {@link Password} with the given ID
     */
    Password getPasswordById(long id);

    /**
     * Gets the first {@link Password} found with the given creation {@link Timestamp}.
     *
     * @param timestamp The {@link Timestamp} of a {@link Password} to find
     * @return a {@link Password} with the given creation timestamp
     */
    Password getPasswordByCreationDate(Timestamp timestamp);

    /**
     * Deletes the given {@link Password}.
     *
     * @param password The {@link Password} to delete
     */
    void deletePassword(Password password);

    /**
     * Modifies the value of the given {@link Password}.
     *
     * @param password The {@link Password} to modify the value of
     * @param newValue The new value for the given {@link Password}
     */
    void modifyPasswordValue(Password password, String newValue);

    /**
     * Saves the given {@link Password} in the {@link de.communicode.communikey.repository.PasswordRepository}.
     *
     * @param password The {@link Password} to save
     * @return the saved {@link Password}
     */
    Password savePassword(Password password);
}