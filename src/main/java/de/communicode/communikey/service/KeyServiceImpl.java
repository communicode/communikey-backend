/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2016
 */
package de.communicode.communikey.service;

import static java.util.Objects.requireNonNull;

import de.communicode.communikey.domain.Key;
import de.communicode.communikey.repository.KeyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class KeyServiceImpl implements KeyService {

    private final KeyRepository keyRepository;

    @Autowired
    public KeyServiceImpl(KeyRepository keyRepository) {
        this.keyRepository = requireNonNull(keyRepository, "keyRepository must not be null!");
    }

    @Override
    public Set<Key> getAllKeys() {
        return StreamSupport.stream(keyRepository.findAll().spliterator(), false)
            .collect(Collectors.toSet());
    }

    @Override
    public Key getKeyById(long id) {
        return requireNonNull(keyRepository.findOneById(id), "key must not be null!");
    }

    @Override
    public Key getKeyByCreationDate(Timestamp timestamp) {
        return requireNonNull(keyRepository.findOneByCreationTimestamp(timestamp), "key must not be null!");
    }

    @Override
    public void deleteKey(Key key) {
        requireNonNull(key, "key must not be null!");
        keyRepository.delete(keyRepository.findOneById(key.getId()));
    }

    @Override
    public void modifyKeyValue(Key key, String newValue) {
        requireNonNull(key, "key must not be null!");

        if (newValue.isEmpty()) {
            throw new IllegalArgumentException("New value can not be empty!");
        }

        key.setValue(newValue);
        keyRepository.save(key);
    }

    @Override
    public Key saveKey(Key key) {
        return requireNonNull(keyRepository.save(key), "key must not be null!");
    }
}
