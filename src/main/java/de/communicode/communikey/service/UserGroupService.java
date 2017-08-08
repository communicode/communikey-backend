/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2017
 */
package de.communicode.communikey.service;

import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;

import com.google.common.collect.Sets;
import de.communicode.communikey.domain.UserGroup;
import de.communicode.communikey.exception.UserGroupConflictException;
import de.communicode.communikey.exception.UserGroupNotFoundException;
import de.communicode.communikey.exception.UserNotFoundException;
import de.communicode.communikey.repository.KeyCategoryRepository;
import de.communicode.communikey.repository.UserGroupRepository;
import de.communicode.communikey.repository.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * The REST API service to process {@link UserGroup}s via a {@link UserGroupRepository}.
 *
 * @author sgreb@communicode.de
 * @since 0.2.0
 */
@Service
public class UserGroupService {

    private static final Logger log = LogManager.getLogger();
    private final UserGroupRepository userGroupRepository;
    private final UserRepository userRepository;
    private final KeyCategoryRepository keyCategoryRepository;
    private final UserService userService;
    private final KeyService keyService;

    @Autowired
    public UserGroupService(UserGroupRepository userGroupRepository, UserService userService, UserRepository userRepository,
            KeyCategoryRepository keyCategoryRepository, KeyService keyService) {
        this.userGroupRepository = requireNonNull(userGroupRepository, "userGroupRepository must not be null!");
        this.userRepository = requireNonNull(userRepository, "userRepository must not be null!");
        this.keyCategoryRepository = requireNonNull(keyCategoryRepository, "keyCategoryRepository must not be null!");
        this.userService = requireNonNull(userService, "userService must not be null!");
        this.keyService = requireNonNull(keyService, "keyService must not be null!");
    }

    /**
     * Add a user to the user group with the specified ID.
     *
     * @param userGroupId the ID of the user group to add the user to
     * @param login the login of the user to be added
     * @return the updated user group
     * @throws UserNotFoundException if the user with the specified login has not been found
     * @throws UserGroupNotFoundException if the user group with the specified ID has not been found
     */
    public UserGroup addUser(Long userGroupId, String login) {
        return ofNullable(userGroupRepository.findOne(userGroupId))
            .map(userGroup -> {
                if (userGroup.addUser(userService.validate(login))) {
                    userGroupRepository.save(userGroup);
                    log.debug("Added user with login '{}' to user group '{}'", login, userGroup.getName());
                    return userGroup;
                }
                return userGroup;
            }).orElseThrow(() -> new UserGroupNotFoundException(userGroupId));
    }

    /**
     * Creates a new user group.
     *
     * @param payload  the payload for the new user group
     * @return the created user group
     * @throws UserGroupConflictException if a user group with the specified name already exists
     */
    public UserGroup create(UserGroup payload) throws UserGroupConflictException {
        validateUniqueName(payload.getName());

        UserGroup userGroup = new UserGroup();
        userGroup.setName(payload.getName());

        userGroupRepository.save(userGroup);
        log.debug("Created new user group '{}'", userGroup.getName());
        return userGroup;
    }

    /**
     * Deletes the user group with the specified name.
     *
     * @param userGroupId the ID of the user group to delete
     * @throws UserGroupNotFoundException if the user group with the specified ID has not been found
     */
    public void delete(Long userGroupId) {
        UserGroup userGroup = validate(userGroupId);
        userGroup.getUsers().forEach(user -> {
            user.removeGroup(userGroup);
            userRepository.save(user);
            keyService.removeObsoletePasswords(user);
            log.debug("Removed user group with name '{}' from user with login '{}'", userGroup.getName(), user.getLogin());
        });
        userGroup.getCategories().forEach(keyCategory -> {
            keyCategory.removeGroup(userGroup);
            keyCategoryRepository.save(keyCategory);
            log.debug("Removed user group with name '{}' from key category with ID '{}'", userGroup.getName(), keyCategory.getId());
        });
        userGroupRepository.delete(userGroup);
        log.debug("Deleted user group with ID '{}'", userGroupId);
    }

    /**
     * Deletes all user groups.
     *
     * @since 0.3.0
     */
    public void deleteAll() {
        userGroupRepository.findAll()
                .forEach(userGroup -> delete(userGroup.getId()));
        log.debug("Deleted all user groups");
    }

    /**
     * Gets the user group with the specified ID.
     *
     * @param userGroupId the ID of the user group to get
     * @return the user group
     * @throws UserGroupNotFoundException if the user group with the specified ID has not been found
     * @since 0.9.0
     */
    public UserGroup get(Long userGroupId) throws UserGroupNotFoundException {
        return validate(userGroupId);
    }

    /**
     * Gets all user groups.
     *
     * @return a collection of all user groups
     */
    public Set<UserGroup> getAll() {
        return Sets.newConcurrentHashSet(userGroupRepository.findAll());
    }

    /**
     * Gets the user group with the specified name.
     *
     * @param name the name of the user group to get
     * @return the user group
     * @throws UserGroupNotFoundException if the user group with the specified name has not been found
     */
    public UserGroup getByName(String name) throws UserGroupNotFoundException {
        return validate(name);
    }

    /**
     * Removes a user from the user group with the specified ID.
     *
     * @param userGroupId the ID of the user group to remove the user from
     * @param login the login of the user to be removed
     * @return the updated user group
     * @throws UserNotFoundException if the user with the specified login has not been found
     * @throws UserGroupNotFoundException if the user group with the specified ID has not been found
     */
    public UserGroup removeUser(Long userGroupId, String login) {
        return ofNullable(userGroupRepository.findOne(userGroupId))
            .map(userGroup -> {
                if (userGroup.removeUser(userService.validate(login))) {
                    userGroupRepository.save(userGroup);
                    log.debug("Removed user with login '{}' from user group '{}'", login, userGroup.getName());
                }
                return userGroup;
            }).orElseThrow(() -> new UserGroupNotFoundException(userGroupId));
    }

    /**
     * Updates the user group with the specified payload.
     *
     * @param userGroupId the ID of the user group to update
     * @param payload the payload to update the user group with
     * @return the updated user group
     * @throws UserGroupNotFoundException if the user group with the specified name has not been found
     */
    public UserGroup update(Long userGroupId, UserGroup payload) throws UserGroupNotFoundException {
        return ofNullable(validate(userGroupId))
            .map(userGroup -> {
                if (!userGroup.getName().equals(payload.getName())) {
                    validateUniqueName(payload.getName());
                    userGroup.setName(payload.getName());
                    userGroupRepository.save(userGroup);
                    log.debug("Updated user group '{}'", userGroup.getName());
                }
                return userGroup;
            }).orElseThrow(() -> new UserGroupNotFoundException(userGroupId));
    }

    /**
     * Validates the specified user group.
     *
     * @param name the name of the user group to validate
     * @return the user group if validated
     * @throws UserGroupNotFoundException if the user group with the specified name has not been found
     */
    public UserGroup validate(String name) throws UserGroupNotFoundException {
        return ofNullable(userGroupRepository.findOneByName(name)).orElseThrow(() -> new UserGroupNotFoundException(name));
    }

    /**
     * Validates the specified user group.
     *
     * @param userGroupId the ID of the user group to validate
     * @return the user group if validated
     * @throws UserGroupNotFoundException if the user group with the specified name has not been found
     * @since 0.9.0
     */
    public UserGroup validate(Long userGroupId) throws UserGroupNotFoundException {
        return ofNullable(userGroupRepository.findOne(userGroupId)).orElseThrow(() -> new UserGroupNotFoundException(userGroupId));
    }

    /**
     * Validates that the specified name is unique.
     *
     * @param name the name to validate
     * @throws UserGroupConflictException if the specified name is not unique
     */
    private void validateUniqueName(String name) throws UserGroupConflictException {
        if (userGroupRepository.findOneByName(name) != null) {
            throw new UserGroupConflictException("user group '" + name +"' already exists");
        }
    }
}
