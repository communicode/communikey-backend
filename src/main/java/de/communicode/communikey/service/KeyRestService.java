/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2016
 */
package de.communicode.communikey.service;

import static java.util.Objects.requireNonNull;

import de.communicode.communikey.domain.Key;
import de.communicode.communikey.exception.KeyNotFoundException;
import de.communicode.communikey.exception.UserNotFoundException;
import de.communicode.communikey.repository.KeyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * The REST API service to process {@link Key} entities via a {@link KeyRepository}.
 *
 * @author sgreb@communicode.de
 * @since 0.1.0
 */
@Service
public class KeyRestService implements KeyService {

    private final KeyRepository keyRepository;
    private final UserRestService userRestService;

    @Autowired
    public KeyRestService(KeyRepository keyRepository, UserRestService userRestService) {
        this.keyRepository = requireNonNull(keyRepository, "keyRepository must not be null!");
        this.userRestService = requireNonNull(userRestService, "userRestService must not be null!");
    }

    @Override
    public void delete(long keyId) throws KeyNotFoundException {
        keyRepository.delete(validate(keyId));
    }

    @Override
    public Set<Key> getAll() {
        return StreamSupport.stream(keyRepository.findAll().spliterator(), false)
            .collect(Collectors.toSet());
    }

    @Override
    public Set<Key> getAllByCreationTimestamp(Timestamp timestamp) {
        return keyRepository.findAllByCreationTimestamp(timestamp);
    }

    @Override
    public Set<Key> getAllByCreator(long creatorUserId) throws UserNotFoundException {
        return keyRepository.findAllByCreatorId(userRestService.validate(creatorUserId).getId());
    }

    @Override
    public Set<Key> getAllByValue(String value) {
        return keyRepository.findAllByValue(value);
    }

    @Override
    public Key getById(long keyId) throws KeyNotFoundException {
        return validate(keyId);
    }

    @Override
    public void modifyValue(long keyId, String newValue) {
        if (newValue.trim().isEmpty()) {
            throw new IllegalArgumentException("new value can not be empty!");
        }
        Key key = validate(keyId);
        key.setValue(newValue);
        keyRepository.save(key);
    }

    @Override
    public Key save(Key key) throws NullPointerException {
        return requireNonNull(keyRepository.save(key), "key must not be null!");
    }

    @Override
    public Key validate(long keyId) throws KeyNotFoundException {
        return Optional.ofNullable(keyRepository.findOne(keyId)).orElseThrow(() -> new KeyNotFoundException(keyId));
    }
}
