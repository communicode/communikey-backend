/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2016
 */
package de.communicode.communikey.service;

import static java.util.Objects.requireNonNull;

import de.communicode.communikey.domain.Role;
import de.communicode.communikey.domain.User;
import de.communicode.communikey.exception.UserConflictException;
import de.communicode.communikey.exception.UserNotFoundException;
import de.communicode.communikey.repository.RoleRepository;
import de.communicode.communikey.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * The REST API service to process {@link User} entities via a {@link UserRepository}.
 *
 * @author sgreb@communicode.de
 * @since 0.2.0
 */
@Service
public class UserRestService implements UserService {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    @Autowired
    public UserRestService(UserRepository userRepository, RoleRepository roleRepository) {
        this.roleRepository = requireNonNull(roleRepository, "roleRepository must not be null!");
        this.userRepository = requireNonNull(userRepository, "userRepository must not be null!");
    }

    @Override
    public User create(String username, String password) throws UserConflictException, IllegalArgumentException {
        if (username.trim().isEmpty() || password.trim().isEmpty()) {
            throw new IllegalArgumentException("username or password must not be empty!");
        }

        Optional.of(userRepository.findOneByUsername(username)).orElseThrow(() -> new UserConflictException(
            "User with username \"" + username + "\"already " + "exists!"));

        User user = new User(username, password);
        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.findOneByName("ROLE_USER"));
        user.setRoles(roles);
        return userRepository.save(user);
    }

    @Override
    public void delete(long userId) throws UserNotFoundException {
        userRepository.delete(validate(userId));
    }

    @Override
    public Set<User> getAll() {
        return StreamSupport.stream(userRepository.findAll().spliterator(), false)
            .collect(Collectors.toSet());

    }

    @Override
    public User getById(long userId) throws UserNotFoundException {
        return validate(userId);
    }

    @Override
    public Optional<User> getByUsername(String username) throws IllegalArgumentException {
        if (username.trim().isEmpty()) {
            throw new IllegalArgumentException("username must not be empty!");
        }
        return userRepository.findOneByUsername(username);
    }

    @Override
    public void modifyUsername(long userId, String newUsername) throws UserNotFoundException, UserConflictException, IllegalArgumentException {
        if (newUsername.trim().isEmpty()) {
            throw new IllegalArgumentException("new username must not be empty!");
        }
        Optional.of(userRepository.findOneByUsername(newUsername)).orElseThrow(() -> new UserConflictException(
            "User with username \"" + newUsername + "\"already " + "exists!"));
        User user = validate(userId);
        user.setUsername(newUsername);
        userRepository.save(user);
    }

    @Override
    public User save(User user) throws NullPointerException {
        return requireNonNull(userRepository.save(user), "user must not be null!");
    }

    @Override
    public void setEnabled(long userId, boolean isEnabled) throws UserNotFoundException {
        User user = validate(userId);
        user.setEnabled(isEnabled);
        userRepository.save(user);
    }

    @Override
    public User validate(long userId) throws UserNotFoundException {
        return Optional.ofNullable(userRepository.findOne(userId)).orElseThrow(() -> new UserNotFoundException(userId));
    }
}
