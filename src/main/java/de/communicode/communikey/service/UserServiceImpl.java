/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2016
*/
package de.communicode.communikey.service;

import static java.util.Objects.requireNonNull;

import de.communicode.communikey.domain.User;
import de.communicode.communikey.repository.UserRepository;
import de.communicode.communikey.type.UserRoleType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
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
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void deleteUser(User user) {
        requireNonNull(user, "user must not be null!");
        userRepository.delete(userRepository.findOneById(user.getId()));
    }

    @Override
    public Set<User> getAllUsers() {
        return StreamSupport.stream(userRepository.findAll().spliterator(), false)
            .collect(Collectors.toSet());

    }

    @Override
    public User getUserById(long id) {
        requireNonNull(userRepository.findOneById(id), "Invalid user id!");
        return userRepository.findOneById(id);
    }

    @Override
    public User getUserByUsername(String username) {
        requireNonNull(userRepository.findOneByUsername(username), "Invalid username!");
        return userRepository.findOneByUsername(username);
    }

    @Override
    public void modifyUsername(User user, String newUsername) throws IllegalArgumentException {
        requireNonNull(user, "user must not be null!");

        if (newUsername.isEmpty()) {
            throw new IllegalArgumentException("New username can not be empty!");
        }
        if (userRepository.findOneByUsername(newUsername) != null) {
            throw new IllegalArgumentException("Username already exists!");
        }

        user.setUsername(newUsername);
        userRepository.save(user);
    }

    @Override
    public void modifyUserRole(User user, UserRoleType newRole) {
        requireNonNull(user, "user must not be null!");
        user.setRole(newRole);
        userRepository.save(user);
    }

    @Override
    public void setUserEnabled(User user, boolean isEnabled) {
        requireNonNull(user, "user must not be null!");
        if (!user.isEnabled() == isEnabled) {
            user.setEnabled(isEnabled);
            userRepository.save(user);
        }
    }
}
