/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2017
 */
package de.communicode.communikey.service;

import static java.util.Objects.requireNonNull;

import de.communicode.communikey.domain.Key;
import de.communicode.communikey.domain.User;
import de.communicode.communikey.domain.request.KeyRequest;
import de.communicode.communikey.exception.key.KeyNotFoundException;
import de.communicode.communikey.repository.KeyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * The REST API service to process {@link Key} entities via a {@link KeyRepository}.
 *
 * @author sgreb@communicode.de
 * @since 0.1.0
 */
@Service
public class KeyRestService implements KeyService {

    private final KeyRepository keyRepository;
    private final UserRestService userService;
    private final KeyCategoryRestService keyCategoryService;

    @Autowired
    public KeyRestService(KeyRepository keyRepository, UserRestService userRestService, KeyCategoryRestService keyCategoryService) {
        this.keyRepository = requireNonNull(keyRepository, "keyRepository must not be null!");
        this.userService = requireNonNull(userRestService, "userService must not be null!");
        this.keyCategoryService = requireNonNull(keyCategoryService, "keyCategoryService must not be null!");
    }

    @Override
    public Key create(KeyRequest payload, Principal principal) {
        User user = userService.validate(principal);
        Key key = new Key(payload.getName(), payload.getValue(), userService.getByEmail(principal.getName()));
        key.setCreator(user);
        return keyRepository.save(key);
    }

    @Override
    public void delete(Long keyId) throws KeyNotFoundException {
        keyRepository.delete(validate(keyId));
    }

    @Override
    public Set<Key> getAll(Principal principal) {
        Set<Key> keyPool = new HashSet<>();
        keyPool.addAll(keyRepository.findAllByCreator(userService.validate(principal)));
        keyCategoryService.getAll(principal).stream()
            .flatMap(keyCategory -> keyCategory.getKeys().stream())
            .forEach(keyPool::add);
        return keyPool;
    }

    @Override
    public Key getById(Long keyId) throws KeyNotFoundException {
        return validate(keyId);
    }

    @Override
    public Key update(Long keyId, KeyRequest payload) {
        Key key = keyRepository.findOne(keyId);
        key.setName(payload.getName());
        key.setValue(payload.getValue());
        return keyRepository.save(key);
    }

    @Override
    public Key save(Key key) throws NullPointerException {
        return requireNonNull(keyRepository.save(key), "key must not be null!");
    }

    @Override
    public Key validate(Long keyId) throws KeyNotFoundException {
        return Optional.ofNullable(keyRepository.findOne(keyId)).orElseThrow(() -> new KeyNotFoundException(keyId));
    }
}
