/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2017
 */
package de.communicode.communikey.service;

import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;

import de.communicode.communikey.domain.UserGroup;
import de.communicode.communikey.exception.UserGroupConflictException;
import de.communicode.communikey.exception.UserGroupNotFoundException;
import de.communicode.communikey.exception.UserNotFoundException;
import de.communicode.communikey.repository.UserGroupRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * The REST API service to process {@link UserGroup}s via a {@link UserGroupRepository}.
 *
 * @author sgreb@communicode.de
 * @since 0.2.0
 */
@Service
public class UserGroupService {

    private final Logger log = LoggerFactory.getLogger(UserGroupService.class);
    private final UserGroupRepository userGroupRepository;
    private final UserService userService;

    @Autowired
    public UserGroupService(UserGroupRepository userGroupRepository, UserService userService) {
        this.userGroupRepository = requireNonNull(userGroupRepository, "userGroupRepository must not be null!");
        this.userService = requireNonNull(userService, "userService must not be null!");
    }

    /**
     * Add a user to the user group with the specified name.
     *
     * @param userGroupName the name of the user group to add the user to
     * @param login the login of the user to be added
     * @return the updated user group
     * @throws UserNotFoundException if the user with the specified login has not been found
     * @throws UserGroupNotFoundException if the user group with the specified name has not been found
     */
    public UserGroup addUser(String userGroupName, String login) {
        return ofNullable(userGroupRepository.findOneByName(userGroupName))
            .map(userGroup -> {
                userGroup.getUsers().add(userService.validate(login));
                userGroupRepository.save(userGroup);
                log.debug("Added user {} to user group {}", login, userGroup.getName());
                return userGroup;
            }).orElseThrow(() -> new UserGroupNotFoundException(userGroupName));
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
        log.debug("Created new user group: {}", userGroup);
        return userGroup;
    }

    /**
     * Deletes the user group with the specified name.
     *
     * @param name the name of the user group to delete
     * @throws UserGroupNotFoundException if the user group with the specified name has not been found
     */
    public boolean delete(String name) {
        userGroupRepository.delete(ofNullable(validate(name)).orElseThrow(() -> new UserGroupNotFoundException(name)));
        log.debug("Deleted user group {}", name);
        return true;
    }

    /**
     * Gets all user groups.
     *
     * @return a collection of all user groups
     */
    public Set<UserGroup> getAll() {
        return StreamSupport.stream(userGroupRepository.findAll().spliterator(), false)
            .collect(Collectors.toSet());
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
     * Removes a user from the user group with the specified name.
     *
     * @param userGroupName the name of the user group to remove the user from
     * @param login the login of the user to be removed
     * @return the updated user group
     * @throws UserNotFoundException if the user with the specified login has not been found
     * @throws UserGroupNotFoundException if the user group with the specified name has not been found
     */
    public UserGroup removeUser(String userGroupName, String login) {
        return ofNullable(userGroupRepository.findOneByName(userGroupName))
            .map(userGroup -> {
                userGroup.getUsers().remove(userService.validate(login));
                userGroupRepository.save(userGroup);
                log.debug("Removed user {} from user group {}", login, userGroup.getName());
                return userGroup;
            }).orElseThrow(() -> new UserGroupNotFoundException(userGroupName));
    }

    /**
     * Updates the user group with the specified payload.
     *
     * @param name the name of the user group to update
     * @param payload the payload to update the user group with
     * @return the updated user group
     * @throws UserGroupNotFoundException if the user group with the specified name has not been found
     */
    public UserGroup update(String name, UserGroup payload) throws UserGroupNotFoundException {
        return ofNullable(validate(name))
            .map(userGroup -> {
                if (!userGroup.getName().equals(payload.getName())) {
                    userGroup.setName(validateUniqueName(payload.getName()));
                    userGroupRepository.save(userGroup);
                    log.debug("Updated user group: {}", userGroup);
                }
                return userGroup;
            }).orElseThrow(() -> new UserGroupNotFoundException(name));
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
     * Validates that the specified name is unique.
     *
     * @param name the name to validate
     * @return the validated name if unique
     * @throws UserGroupConflictException if the specified name is not unique
     */
    private String validateUniqueName(String name) throws UserGroupConflictException {
        if (ofNullable(userGroupRepository.findOneByName(name)).isPresent()) {
            throw new UserGroupConflictException("user group '" + name +"' already exists");
        }
        return name;
    }
}
