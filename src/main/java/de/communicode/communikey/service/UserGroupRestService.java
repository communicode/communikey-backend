/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2016
 */
package de.communicode.communikey.service;

import static java.util.Objects.requireNonNull;

import de.communicode.communikey.domain.UserGroup;
import de.communicode.communikey.exception.UserGroupConflictException;
import de.communicode.communikey.exception.UserGroupNotFoundException;
import de.communicode.communikey.repository.UserGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * The REST API service to process {@link UserGroup} entities via a {@link UserGroupRepository}.
 *
 * @author sgreb@communicode.de
 * @since 0.2.0
 */
@Service
public class UserGroupRestService implements UserGroupService {

    private final UserGroupRepository userGroupRepository;

    @Autowired
    public UserGroupRestService(UserGroupRepository userGroupRepository) {
        this.userGroupRepository = requireNonNull(userGroupRepository, "userGroupRepository must not be null!");
    }

    @Override
    public UserGroup create(String userGroupName) throws UserGroupConflictException, IllegalArgumentException {
        if (userGroupName.trim().isEmpty()) {
            throw new IllegalArgumentException("user group name must not be empty!");
        }
        Optional.of(userGroupRepository.findOneByName(userGroupName)).orElseThrow(() ->  new UserGroupConflictException("user group with the specified name already exists!"));
        return userGroupRepository.save(new UserGroup(userGroupName));
    }

    @Override
    public void delete(long userGroupId) throws UserGroupNotFoundException {
        userGroupRepository.delete(validate(userGroupId));
    }

    @Override
    public Set<UserGroup> getAll() {
        return StreamSupport.stream(userGroupRepository.findAll().spliterator(), false)
            .collect(Collectors.toSet());
    }

    @Override
    public UserGroup getById(long userGroupId) throws UserGroupNotFoundException {
        return validate(userGroupId);
    }

    @Override
    public Optional<UserGroup> getByName(String userGroupName) throws IllegalArgumentException {
        if(userGroupName.trim().isEmpty()) {
            throw new IllegalArgumentException("user group name must not be empty!");
        }
        return userGroupRepository.findOneByName(userGroupName);
    }

    @Override
    public void modifyName(long userGroupId, String newUserGroupName) throws UserGroupNotFoundException, UserGroupConflictException, IllegalArgumentException {
        if (newUserGroupName.trim().isEmpty()) {
            throw new IllegalArgumentException("new name must not be empty!");
        }
        Optional.of(userGroupRepository.findOneByName(newUserGroupName)).orElseThrow(() -> new UserGroupConflictException(
            "User group with name \"" + newUserGroupName + "\"already " + "exists!"));
        UserGroup userGroup = validate(userGroupId);
        userGroup.setName(newUserGroupName);
        userGroupRepository.save(userGroup);
    }

    @Override
    public UserGroup save(UserGroup userGroup) throws NullPointerException {
        return requireNonNull(userGroupRepository.save(userGroup), "userGroup must not be null!");
    }

    @Override
    public UserGroup validate(long userGroupId) throws UserGroupNotFoundException {
        return Optional.ofNullable(userGroupRepository.findOne(userGroupId)).orElseThrow(() -> new UserGroupNotFoundException(userGroupId));
    }
}
