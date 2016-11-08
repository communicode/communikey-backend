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
import de.communicode.communikey.repository.KeyCategoryRepository;
import de.communicode.communikey.repository.KeyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class KeyCategoryServiceImpl implements KeyCategoryService {

    private final KeyCategoryRepository keyCategoryRepository;
    private final KeyRepository keyRepository;

    @Autowired
    public KeyCategoryServiceImpl(KeyCategoryRepository keyCategoryRepository, KeyRepository keyRepository) {
        this.keyRepository = requireNonNull(keyRepository, "keyRepository must not be null!");
        this.keyCategoryRepository = requireNonNull(keyCategoryRepository, "keyCategoryRepository must not be null!");
    }

    @Override
    public void delete(KeyCategory category) throws NullPointerException {
        requireNonNull(category, "category must not be null!");
        keyCategoryRepository.delete(keyCategoryRepository.findOneById(category.getId()));
    }

    @Override
    public Set<KeyCategory> getAll() {
        return StreamSupport.stream(keyCategoryRepository.findAll().spliterator(), false)
            .collect(Collectors.toSet());
    }

    @Override
    public Set<KeyCategory> getAllByCreator(User creator) throws NullPointerException {
        requireNonNull(creator, "creator must not be null!");
        return creator.getKeyCategories();
    }

    @Override
    public Set<KeyCategory> getAllByName(String name) throws IllegalArgumentException {
        if (name.trim().isEmpty()) {
            throw new IllegalArgumentException("name must not be null!");
        }
        return keyCategoryRepository.findAllByName(name);
    }

    @Override
    public Set<KeyCategory> getAllByParent(KeyCategory category) throws NullPointerException {
        requireNonNull(category, "category must not be null!");
        return keyCategoryRepository.findAllByParent(category);
    }

    @Override
    public Set<KeyCategory> getAllByResponsible(User responsible) throws NullPointerException {
        requireNonNull(responsible, "responsible must not be null!");
        return keyCategoryRepository.findAllByResponsible(responsible);
    }

    @Override
    public KeyCategory getById(long id) throws NullPointerException {
        return requireNonNull(keyCategoryRepository.findOneById(id), "key category must not be null!");
    }

    @Override
    public Set<KeyCategory> getChilds(KeyCategory category) throws NullPointerException {
        requireNonNull(category, "category must not be null!");
        return category.getChilds();
    }

    @Override
    public Optional<Key> getKey(long id, KeyCategory category) throws NullPointerException {
        requireNonNull(category, "category must not be null!");
        return category.getKeys().stream()
            .filter(key -> keyRepository.findOneById(id).equals(key))
            .findFirst();
    }

    @Override
    public Set<Key> getAllKeys(KeyCategory category) throws NullPointerException {
        requireNonNull(category, "category must not be null!");
        return category.getKeys();
    }

    @Override
    public Optional<KeyCategory> getParent(KeyCategory category) {
        requireNonNull(category, "category must not be null!");
        return category.getParent();
    }

    @Override
    public boolean hasChild(KeyCategory parentCategory, KeyCategory category) throws NullPointerException {
        requireNonNull(parentCategory, "parentCategory must not be null!");
        requireNonNull(category, "category must not be null!");
        return parentCategory.getChilds().contains(category);
    }

    @Override
    public boolean hasKey(Key key, KeyCategory category) throws NullPointerException {
        requireNonNull(key, "key must not be null!");
        requireNonNull(category, "category must not be null!");
        return category.getKeys().contains(key);
    }

    @Override
    public void modifyName(KeyCategory category, String newName) throws NullPointerException, IllegalArgumentException {
        requireNonNull(category, "category must not be null!");
        if (newName.trim().isEmpty()) {
            throw new IllegalArgumentException("New name can not be empty!");
        }
        category.setName(newName);
    }

    @Override
    public KeyCategory save(KeyCategory category) throws NullPointerException {
        requireNonNull(category, "category must not be null!");
        return keyCategoryRepository.save(category);
    }
}
