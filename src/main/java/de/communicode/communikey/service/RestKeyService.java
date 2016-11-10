/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2016
 */
package de.communicode.communikey.service;

import static java.util.Objects.requireNonNull;

import de.communicode.communikey.domain.Key;
import de.communicode.communikey.domain.KeyCategory;
import de.communicode.communikey.domain.User;
import de.communicode.communikey.exception.KeyNotFoundException;
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
public class RestKeyService implements KeyService {

    private final KeyRepository keyRepository;

    @Autowired
    public RestKeyService(KeyRepository keyRepository) {
        this.keyRepository = requireNonNull(keyRepository, "keyRepository must not be null!");
    }

    @Override
    public void delete(Key key) throws NullPointerException {
        requireNonNull(key, "key must not be null!");
        keyRepository.delete(keyRepository.findOneById(key.getId()));
    }

    @Override
    public Set<Key> getAll() {
        return StreamSupport.stream(keyRepository.findAll().spliterator(), false)
            .collect(Collectors.toSet());
    }

    @Override
    public Set<Key> getAllByKeyCategory(KeyCategory keyCategory) {
        return keyRepository.findAllByCategory(keyCategory);
    }

    @Override
    public Set<Key> getAllByCreationTimestamp(Timestamp timestamp) {
        return keyRepository.findAllByCreationTimestamp(timestamp);
    }

    @Override
    public Set<Key> getAllByCreator(User creator) {
        return keyRepository.findAllByCreator(creator);
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
    public void modifyValue(Key key, String newValue) {
        requireNonNull(key, "key must not be null!");

        if (newValue.isEmpty()) {
            throw new IllegalArgumentException("New value can not be empty!");
        }

        key.setValue(newValue);
        keyRepository.save(key);
    }

    @Override
    public Key save(Key key) throws NullPointerException {
        return requireNonNull(keyRepository.save(key), "key must not be null!");
    }

    @Override
    public Key validate(long keyId) throws KeyNotFoundException {
        return Optional.ofNullable(keyRepository.findOneById(keyId)).orElseThrow(() -> new KeyNotFoundException(keyId));
    }
}
