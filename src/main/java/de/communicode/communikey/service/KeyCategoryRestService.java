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
import de.communicode.communikey.exception.KeyCategoryNotFoundException;
import de.communicode.communikey.exception.KeyNotFoundException;
import de.communicode.communikey.exception.UserNotFoundException;
import de.communicode.communikey.repository.KeyCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * The REST API service to process {@link KeyCategory} entities via a {@link KeyCategoryRepository}.
 *
 * @author sgreb@communicode.de
 * @since 0.2.0
 */
@Service
public class KeyCategoryRestService implements KeyCategoryService {

    private final KeyCategoryRepository keyCategoryRepository;
    private final KeyRestService keyRestService;
    private final UserRestService userRestService;

    @Autowired
    public KeyCategoryRestService(KeyCategoryRepository keyCategoryRepository, KeyRestService keyRestService, UserRestService userRestService) {
        this.keyCategoryRepository = requireNonNull(keyCategoryRepository, "keyCategoryRepository must not be null!");
        this.keyRestService = requireNonNull(keyRestService, "keyRestService must not be null!");
        this.userRestService = requireNonNull(userRestService, "userRestService must not be null!");
    }

    @Override
    public void delete(long keyCategoryId) throws KeyCategoryNotFoundException {
        keyCategoryRepository.delete(validate(keyCategoryId));
    }

    @Override
    public Set<KeyCategory> getAll() {
        return StreamSupport.stream(keyCategoryRepository.findAll().spliterator(), false)
            .collect(Collectors.toSet());
    }

    @Override
    public Set<KeyCategory> getAllByCreator(long creatorUserId) throws UserNotFoundException {
        return keyCategoryRepository.findAllByCreator(userRestService.validate(creatorUserId).getId());
    }

    @Override
    public Set<KeyCategory> getAllByName(String name) throws IllegalArgumentException {
        if (name.trim().isEmpty()) {
            throw new IllegalArgumentException("name must not be empty!");
        }
        return keyCategoryRepository.findAllByName(name);
    }

    @Override
    public Set<KeyCategory> getAllByParent(long parentKeyCategoryId) throws KeyCategoryNotFoundException {
        return keyCategoryRepository.findAllByParent(validate(parentKeyCategoryId).getId());
    }

    @Override
    public Set<KeyCategory> getAllByResponsible(long responsibleUserId) throws UserNotFoundException {
        return keyCategoryRepository.findAllByResponsible(userRestService.validate(responsibleUserId).getId());
    }

    @Override
    public Set<Key> getAllKeys(long keyCategoryId) throws KeyCategoryNotFoundException {
        return validate(keyCategoryId).getKeys();
    }

    @Override
    public KeyCategory getById(long keyCategoryId) throws KeyCategoryNotFoundException {
        return validate(keyCategoryId);
    }

    @Override
    public Set<KeyCategory> getChilds(long keyCategoryId) throws KeyCategoryNotFoundException {
        return validate(keyCategoryId).getChilds();
    }

    @Override
    public Key getKey(long keyId, long keyCategoryId) throws KeyNotFoundException, KeyCategoryNotFoundException {
        Set<Key> keyCategoryKeys = validate(keyCategoryId).getKeys();
        return keyCategoryKeys.stream()
            .filter(keyCategoryKeys::contains)
            .findFirst().orElseThrow(() -> new KeyNotFoundException(keyId));
    }

    @Override
    public Optional<KeyCategory> getParent(long keyCategoryId) {
        return validate(keyCategoryId).getParent();
    }

    @Override
    public boolean hasChild(long parentKeyCategoryId, long childKeyCategoryId) throws KeyCategoryNotFoundException {
        return validate(parentKeyCategoryId).getChilds().contains(validate(childKeyCategoryId));
    }

    @Override
    public boolean hasKey(long keyId, long keyCategoryId) throws KeyNotFoundException, KeyCategoryNotFoundException {
        return validate(keyCategoryId).getKeys().contains(keyRestService.validate(keyId));
    }

    @Override
    public void modifyName(long keyCategoryId, String newName) throws KeyCategoryNotFoundException, IllegalArgumentException {
        if (newName.trim().isEmpty()) {
            throw new IllegalArgumentException("new name must not be empty!");
        }
        KeyCategory keyCategory = validate(keyCategoryId);
        keyCategory.setName(newName);
        keyCategoryRepository.save(keyCategory);
    }

    @Override
    public KeyCategory save(KeyCategory category) throws NullPointerException {
        requireNonNull(category, "category must not be null!");
        return keyCategoryRepository.save(category);
    }

    @Override
    public KeyCategory validate(long keyCategoryId) throws KeyCategoryNotFoundException {
        return Optional.ofNullable(keyCategoryRepository.findOne(keyCategoryId)).orElseThrow(() -> new KeyCategoryNotFoundException(keyCategoryId));
    }
}
