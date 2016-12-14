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

import java.security.Principal;
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
    public User create(String email, String password) throws UserConflictException, IllegalArgumentException {
        if (email.trim().isEmpty() || password.trim().isEmpty()) {
            throw new IllegalArgumentException("email or password must not be empty!");
        }

        Optional.of(userRepository.findOneByEmail(email)).orElseThrow(() -> new UserConflictException(
            "User with email \"" + email + "\"already " + "exists!"));

        User user = new User(email, password);
        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.findOneByName("ROLE_USER"));
        user.setRoles(roles);
        return userRepository.save(user);
    }

    @Override
    public void delete(Principal principal) throws UserNotFoundException {
        userRepository.delete(validate(principal));
    }

    @Override
    public Set<User> getAll() {
        return StreamSupport.stream(userRepository.findAll().spliterator(), false)
            .collect(Collectors.toSet());
    }

    @Override
    public User getByEmail(String email) throws UserNotFoundException, IllegalArgumentException {
        if (email.trim().isEmpty()) {
            throw new IllegalArgumentException("email must not be empty!");
        }
        return Optional.ofNullable(userRepository.findOneByEmail(email)).orElseThrow(() -> new UserNotFoundException(email));
    }

    @Override
    public User getById(long userId) throws UserNotFoundException {
        return Optional.ofNullable(userRepository.findOne(userId)).orElseThrow(() -> new UserNotFoundException(userId));
    }

    @Override
    public void modifyEmail(Principal principal, String newEmail) throws UserNotFoundException, UserConflictException, IllegalArgumentException {
        if (newEmail.trim().isEmpty()) {
            throw new IllegalArgumentException("new email must not be empty!");
        }
        Optional.of(userRepository.findOneByEmail(newEmail)).orElseThrow(() -> new UserConflictException(
            "User with email \"" + newEmail + "\"already " + "exists!"));
        User user = validate(principal);
        user.setEmail(newEmail);
        userRepository.save(user);
    }

    @Override
    public User save(User user) throws NullPointerException {
        return requireNonNull(userRepository.save(user), "user must not be null!");
    }

    @Override
    public void setEnabled(Principal principal, boolean isEnabled) throws UserNotFoundException {
        User user = validate(principal);
        user.setEnabled(isEnabled);
        userRepository.save(user);
    }

    @Override
    public User validate(Principal principal) throws UserNotFoundException {
        return Optional.ofNullable(userRepository.findOneByEmail(principal.getName())).orElseThrow(() -> new UserNotFoundException(principal.getName()));
    }
}
