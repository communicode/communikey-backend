/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2017
 */
package de.communicode.communikey.service;

import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;

import de.communicode.communikey.domain.Key;
import de.communicode.communikey.domain.KeyCategory;
import de.communicode.communikey.domain.User;
import de.communicode.communikey.exception.KeyCategoryConflictException;
import de.communicode.communikey.exception.KeyCategoryNotFoundException;
import de.communicode.communikey.exception.KeyNotFoundException;
import de.communicode.communikey.exception.UserNotFoundException;
import de.communicode.communikey.repository.KeyCategoryRepository;
import de.communicode.communikey.repository.KeyRepository;
import de.communicode.communikey.repository.UserRepository;
import de.communicode.communikey.security.AuthoritiesConstants;
import de.communicode.communikey.security.SecurityUtils;
import de.communicode.communikey.service.payload.KeyCategoryPayload;
import de.communicode.communikey.support.KeyCategoryChildrenMap;
import de.communicode.communikey.support.KeyCategoryParentMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The REST API service to process {@link KeyCategory} entities via a {@link KeyCategoryRepository}.
 *
 * @author sgreb@communicode.de
 * @since 0.2.0
 */
@Service
public class KeyCategoryService {

    private static final Logger log = LogManager.getLogger(KeyCategoryService.class);
    private final KeyCategoryRepository keyCategoryRepository;
    private final KeyRepository keyRepository;
    private final UserService userService;
    private final UserRepository userRepository;
    private final KeyService keyService;
    private final KeyCategoryChildrenMap keyCategoryChildrenMap = KeyCategoryChildrenMap.getInstance();
    private final KeyCategoryParentMap keyCategoryParentMap = KeyCategoryParentMap.getInstance();

    @Bean
    public Map<KeyCategory, Set<KeyCategory>> keyCategoryTable() {
        return new HashMap<>();
    }

    @Autowired
    public KeyCategoryService(KeyCategoryRepository keyCategoryRepository, UserService userService, KeyService keyService, KeyRepository keyRepository,
                              UserRepository userRepository) {
        this.keyCategoryRepository = requireNonNull(keyCategoryRepository, "keyCategoryRepository must not be null!");
        this.userService = requireNonNull(userService, "userService must not be null!");
        this.keyService = requireNonNull(keyService, "keyService must not be null!");
        this.keyRepository = requireNonNull(keyRepository, "keyRepository must not be null!");
        this.userRepository = requireNonNull(userRepository, "userRepository must not be null!");
    }

    /**
     * Adds a key category as a child of a parent key category.
     *
     * @param parentKeyCategoryId the ID of the parent key category to add the child key category to
     * @param childKeyCategoryId the ID of the parent key category to be added to the parent key category
     * @return the updated parent key category
     * @throws KeyCategoryNotFoundException if a key category with specified ID has not been found
     */
    public KeyCategory addChild(Long parentKeyCategoryId, Long childKeyCategoryId) throws KeyCategoryNotFoundException {
        KeyCategory parent = validate(parentKeyCategoryId);
        KeyCategory child = validate(childKeyCategoryId);
        isUniqueKeyCategoryName(child.getName(), parentKeyCategoryId);

        if (Objects.equals(parentKeyCategoryId, childKeyCategoryId)) {
            throw new KeyCategoryConflictException(
                "parent key category ID '" + parentKeyCategoryId + "' equals child key category ID '" + childKeyCategoryId + "'");
        }
        if (keyCategoryChildrenMap.getMap().get(childKeyCategoryId).contains(parentKeyCategoryId)) {
            throw new KeyCategoryConflictException("key category with ID '" + parentKeyCategoryId + "' can not be set as own child reference");
        }

        if (parent.getChildren().add(child)) {
            ofNullable(child.getParent()).ifPresent(directParent -> {
                keyCategoryChildrenMap.getMap().get(directParent.getId()).remove(childKeyCategoryId);
                keyCategoryParentMap.getMap().get(directParent.getId())
                    .forEach(subParentId -> keyCategoryChildrenMap.getMap().get(subParentId).remove(childKeyCategoryId));
                keyCategoryParentMap.getMap().get(childKeyCategoryId)
                    .forEach(subChildId -> keyCategoryChildrenMap.getMap().get(subChildId).removeAll(keyCategoryChildrenMap.getMap().get(childKeyCategoryId)));
                keyCategoryParentMap.getMap().get(childKeyCategoryId).retainAll(keyCategoryParentMap.getMap().get(parentKeyCategoryId));
                keyCategoryChildrenMap.getMap().get(childKeyCategoryId)
                    .forEach(subChildId -> {
                        keyCategoryParentMap.getMap().get(subChildId).retainAll(keyCategoryParentMap.getMap().get(validate(subChildId).getParent().getId()));
                        keyCategoryParentMap.getMap().get(subChildId).add(validate(subChildId).getParent().getId());
                    });
                keyCategoryParentMap.getMap().get(childKeyCategoryId).add(parentKeyCategoryId);
            });

            child.setParent(parent);
            KeyCategory persistedKeyCategory = keyCategoryRepository.save(parent);
            log.debug("Added key category with ID '{}' as child to key category with ID '{}'", child.getId(), parent.getId());

            keyCategoryChildrenMap.getMap().get(parentKeyCategoryId).add(childKeyCategoryId);
            keyCategoryChildrenMap.getMap().get(parentKeyCategoryId).addAll(keyCategoryChildrenMap.getMap().get(childKeyCategoryId));
            keyCategoryParentMap.getMap().get(childKeyCategoryId).add(parentKeyCategoryId);
            keyCategoryParentMap.getMap().get(childKeyCategoryId).addAll(keyCategoryParentMap.getMap().get(parentKeyCategoryId));
            keyCategoryChildrenMap.getMap().get(parentKeyCategoryId)
                .forEach(childId -> keyCategoryParentMap.getMap().get(childId).addAll(keyCategoryParentMap.getMap().get(childKeyCategoryId)));
            keyCategoryParentMap.getMap().get(childKeyCategoryId)
                .forEach(parentId -> keyCategoryChildrenMap.getMap().get(parentId).addAll(keyCategoryChildrenMap.getMap().get(parentKeyCategoryId)));
            return persistedKeyCategory;
        }
        return parent;
    }

    /**
     * Adds the key to the key category with the specified ID.
     *
     * @param keyCategoryId the ID of the key category the key will be added to
     * @param keyId the ID of the key to be added
     * @return the updated key category
     * @throws KeyCategoryNotFoundException if the key category with the specified ID has not been found
     * @throws KeyNotFoundException if the key with the specified ID has not been found
     */
    public KeyCategory addKey(Long keyCategoryId, Long keyId) {
        KeyCategory keyCategory = validate(keyCategoryId);
        Key key = keyService.validate(keyId);
        key.setCategory(keyCategory);
        key = keyRepository.save(key);
        keyCategory.getKeys().add(key);
        keyCategory = keyCategoryRepository.save(keyCategory);
        log.debug("Added key with ID '{}' to key category with ID '{}'", keyId, keyCategoryId);
        return keyCategory;
    }

    /**
     * Creates a new key category.
     *
     * @param payload the key category payload
     * @return the created key category
     * @throws KeyCategoryNotFoundException if the parent key category with the specified ID has not been found
     * @throws UserNotFoundException if the user with the specified ID has not been found
     */
    public KeyCategory create(KeyCategoryPayload payload) throws KeyCategoryNotFoundException, UserNotFoundException {
        isUniqueKeyCategoryName(payload.getName(), null);

        KeyCategory keyCategory = new KeyCategory();
        User user = userService.validate(SecurityUtils.getCurrentUserLogin());

        keyCategory.setName(payload.getName());
        keyCategory.setCreator(userService.validate(SecurityUtils.getCurrentUserLogin()));
        keyCategory = keyCategoryRepository.save(keyCategory);
        setResponsibleUser(keyCategory.getId(), user.getLogin());


        user.getResponsibleKeyCategories().add(keyCategory);
        userRepository.save(user);
        keyCategoryChildrenMap.getMap().put(keyCategory.getId(), new HashSet<>());
        keyCategoryParentMap.getMap().put(keyCategory.getId(), new HashSet<>());
        log.debug("Created new key category with ID '{}'", keyCategory.getId());
        return keyCategory;
    }

    /**
     * Deletes the specified key category.
     * <p>
     *     <strong>This is a recursive operation that deletes all children key categories!</strong>
     *
     * @param keyCategoryId the ID of the key category to delete
     * @throws KeyCategoryNotFoundException if no key category entity with the specified ID has been found
     */
    public void delete(Long keyCategoryId) throws KeyCategoryNotFoundException {
        keyCategoryRepository.delete(validate(keyCategoryId));

        keyCategoryChildrenMap.getMap().get(keyCategoryId)
            // for all children of the target...
            .forEach(childId -> keyCategoryParentMap.getMap().get(keyCategoryId)
                // ...get all parents of the target and remove all children of the target (including itself) from all parents of the target
                .forEach(parentId -> {
                    keyCategoryChildrenMap.getMap().get(parentId).removeAll(keyCategoryChildrenMap.getMap().get(keyCategoryId));
                    keyCategoryChildrenMap.getMap().get(parentId).remove(keyCategoryId);
                }));

        keyCategoryChildrenMap.getMap().get(keyCategoryId)
            .forEach(childId -> {
                // remove all children of the target from the parent map
                keyCategoryParentMap.getMap().remove(childId);
                // remove the target itself from the parent map
                keyCategoryParentMap.getMap().remove(keyCategoryId);
                // remove all children of the target from the child map
                keyCategoryChildrenMap.getMap().remove(childId);
            });

        // remove the target itself from the child map
        keyCategoryChildrenMap.getMap().remove(keyCategoryId);
        log.debug("Deleted key category with ID '{}'", keyCategoryId);
    }

    /**
     * Deletes all key categories.
     */
    public void deleteAll() {
        keyCategoryRepository.deleteAll();
        keyCategoryChildrenMap.getMap().clear();
        keyCategoryParentMap.getMap().clear();
        log.debug("Deleted all key categories");
    }

    /**
     * Gets all key category entities the current user is authorized to receive.
     * <p>
     *     If a key category is already included as direct- or indirect child of another key category, only the parent key category should be added to the
     *     return collection.
     * <p>
     *     Example of a collection with multiple indirect key category children references:
     * <pre>
     *     |-- category1
     *     |   |-- category2
     *     |   |   |-- category3
     *     |   |-- category4
     *     |   |   |-- category5
     *     |-- category2
     *     |   |-- category3
     *     |-- category3
     *     |-- category4
     *     |   |-- category5
     *     |-- category5
     *     |-- category6
     * </pre>
     *
     * This will be reduced to contain no more duplicated key categories:
     * <pre>
     *     |-- category1
     *     |   |-- category2
     *     |   |   |-- category3
     *     |   |-- category4
     *     |   |   |-- category5
     *     |-- category6
     * </pre>
     *
     * @return a collection of all key categories
     */
    public Set<KeyCategory> getAll() {
        //User user = userService.validate(SecurityUtils.getCurrentUserLogin());

        Set<KeyCategory> keyCategoryPool = new HashSet<>();

        if (SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN)) {
            keyCategoryPool.addAll(keyCategoryRepository.findAll());
        } else {
/*        userService.validate(user.getLogin()).getGroups().stream()
            .flatMap(userGroup -> userGroup.getCategories().stream())
            .forEach(keyCategoryPool::add);*/
            keyCategoryPool.addAll(keyCategoryRepository.findAllByCreator(userService.validate(SecurityUtils.getCurrentUserLogin())));
            keyCategoryPool.addAll(userService.validate(SecurityUtils.getCurrentUserLogin()).getResponsibleKeyCategories());
            //keyCategoryPool.addAll(getAllByResponsible(user.getLogin()));
        }

        return keyCategoryPool.stream()
            .filter(childCategory -> keyCategoryPool.stream()
                .noneMatch(parentCategory -> keyCategoryChildrenMap.getMap().get(parentCategory.getId()).contains(childCategory.getId())))
            .collect(Collectors.toSet());
    }

    /**
     * Gets the key category with the specified ID.
     *
     * @param keyCategoryId the ID of the key category to find
     * @return the found key category entity
     * @throws KeyCategoryNotFoundException if the key category entity with the specified ID has not been found
     */
    public KeyCategory get(Long keyCategoryId) throws KeyCategoryNotFoundException {
        return validate(keyCategoryId);
    }

    /**
     * Sets the responsible user for the key category with the specified ID.
     *
     * @param keyCategoryId the ID of the key category to set the responsible user of
     * @param userLogin the login of the user to be set as the responsible of the key category
     * @return the updated key category entity
     * @throws KeyCategoryNotFoundException if the key category with the specified ID has not been found
     * @throws UserNotFoundException if the user with the specified login has not been found
     */
    public KeyCategory setResponsibleUser(Long keyCategoryId, String userLogin) throws KeyCategoryNotFoundException, UserNotFoundException {
        User user = userService.validate(userLogin);
        KeyCategory keyCategory = validate(keyCategoryId);

        keyCategory.setResponsible(user);
        keyCategory = keyCategoryRepository.save(keyCategory);

        user.getResponsibleKeyCategories().add(keyCategory);
        userRepository.save(user);
        return keyCategory;
    }

    /**
     * Validates the specified key category.
     *
     * @param keyCategoryId the ID of the key category to validate
     * @return the key category if validated
     * @throws KeyCategoryNotFoundException if the key category with the specified ID has not been found
     */
    public KeyCategory validate(Long keyCategoryId) throws KeyCategoryNotFoundException {
        return ofNullable(keyCategoryRepository.findOne(keyCategoryId)).orElseThrow(() -> new KeyCategoryNotFoundException(keyCategoryId));
    }

    /**
     * Validates that the specified key category name is unique.
     *
     * @param name the name of the key category to validate
     * @param parentKeyCategoryId the ID of the parent key category to validate
     * @throws KeyCategoryConflictException if the specified key category name is not unique
     */
    private boolean isUniqueKeyCategoryName(String name, Long parentKeyCategoryId) throws KeyCategoryConflictException {
        if (Objects.nonNull(parentKeyCategoryId)) {
            if (keyCategoryRepository.findOne(parentKeyCategoryId).getChildren().stream()
                .anyMatch(keyCategory -> keyCategory.getName().equals(name))) {
                throw new KeyCategoryConflictException("key category '" + name + "' already exists");
            } else {
                return true;
            }
        }
        if (keyCategoryRepository.findAllByParentIsNull().stream()
            .anyMatch(keyCategory -> keyCategory.getName().equals(name))) {
            throw new KeyCategoryConflictException("key category '" + name + "' already exists");
        }
        return true;
    }
}
