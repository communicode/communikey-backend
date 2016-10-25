/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2016
 */
package de.communicode.communikey.service;

import static java.util.Objects.requireNonNull;

import de.communicode.communikey.domain.UserGroup;
import de.communicode.communikey.repository.UserGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Service to interact with the {@link de.communicode.communikey.repository.PasswordRepository}.
 *
 * @author sgreb@communicode.de
 * @since 0.2.0
 */
@Service
public class UserGroupServiceImpl implements UserGroupService {
    private final UserGroupRepository userGroupRepository;

    @Autowired
    public UserGroupServiceImpl(UserGroupRepository userGroupRepository) {
        this.userGroupRepository = requireNonNull(userGroupRepository, "userGroupRepository must not be null!");
    }

    @Override
    public void create(String name) throws NullPointerException, IllegalArgumentException {
        requireNonNull(name, "name must not be null!");

        if (name.isEmpty()) {
            throw new IllegalArgumentException("name must not be empty!");
        }
        if (userGroupRepository.findOneByName(name) != null) {
            throw new IllegalArgumentException("User group with the given name already exists!");
        }

        userGroupRepository.save(new UserGroup(name));
    }

    @Override
    public void delete(UserGroup userGroup) throws NullPointerException {
        requireNonNull(userGroup, "userGroup must not be null!");
        userGroupRepository.delete(userGroupRepository.findOneById(userGroup.getId()));
    }

    @Override
    public Set<UserGroup> getAllUserGroups() {
        return StreamSupport.stream(userGroupRepository.findAll().spliterator(), false)
            .collect(Collectors.toSet());
    }

    @Override
    public UserGroup getById(long id) throws NullPointerException, IllegalArgumentException {
        requireNonNull(userGroupRepository.findOneById(id), "id must not be null!");

        if (id <= 0) {
            throw new IllegalArgumentException("id must not be less than or equal to 0!");
        }

        return userGroupRepository.findOneById(id);
    }

    @Override
    public UserGroup getByName(String name) throws NullPointerException, IllegalArgumentException {
        requireNonNull(userGroupRepository.findOneByName(name), "name must not be null!");

        if(name.isEmpty()) {
            throw new IllegalArgumentException("name must not be empty!");
        }

        return userGroupRepository.findOneByName(name);
    }

    @Override
    public void modifyName(UserGroup userGroup, String newName) throws NullPointerException, IllegalArgumentException {
        requireNonNull(userGroup, "userGroup must not be null!");

        if (newName.isEmpty()) {
            throw new IllegalArgumentException("newName must not be empty!");
        }
        if (userGroupRepository.findOneByName(newName) != null) {
            throw new IllegalArgumentException("newName already exists!");
        }

        userGroup.setName(newName);
        userGroupRepository.save(userGroup);
    }

    @Override
    public void save(UserGroup userGroup) throws NullPointerException {
        requireNonNull(userGroup, "userGroup must not be null!");
        userGroupRepository.save(userGroup);

    }
}
