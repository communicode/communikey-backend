/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2016
 */
package de.communicode.communikey.service;

import static java.util.Objects.requireNonNull;

import de.communicode.communikey.domain.Password;
import de.communicode.communikey.repository.PasswordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class PasswordServiceImpl implements PasswordService {

    @Autowired
    private PasswordRepository passwordRepository;

    @Override
    public Set<Password> getAllPasswords() {
        return StreamSupport.stream(passwordRepository.findAll().spliterator(), false)
            .collect(Collectors.toSet());
    }

    @Override
    public Password getPasswordById(long id) {
        return requireNonNull(passwordRepository.findOneById(id), "Invalid password id!");
    }

    @Override
    public Password getPasswordByCreationDate(Timestamp timestamp) {
        return requireNonNull(passwordRepository.findOneByCreationTimestamp(timestamp), "No password found with the given creation date!");
    }

    @Override
    public void deletePassword(Password password) {
        requireNonNull(password, "password must not be null!");
        passwordRepository.delete(passwordRepository.findOneById(password.getId()));
    }

    @Override
    public void modifyPasswordValue(Password password, String newValue) {
        requireNonNull(password, "password must not be null!");

        if (newValue.isEmpty()) {
            throw new IllegalArgumentException("New value can not be empty!");
        }

        password.setValue(newValue);
        passwordRepository.save(password);
    }

    @Override
    public Password savePassword(Password password) {
        return requireNonNull(passwordRepository.save(password), "password must not be null!");
    }
}
