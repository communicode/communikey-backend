/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2016
 */
package de.communicode.communikey.controller;

import static de.communicode.communikey.controller.RequestMappings.USERS;
import static de.communicode.communikey.controller.RequestMappings.USER_ID;
import static java.util.Objects.requireNonNull;

import de.communicode.communikey.domain.User;
import de.communicode.communikey.exception.UserNotFoundException;
import de.communicode.communikey.service.UserRestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * The REST API controller to process {@link User} entities.
 * <p>
 *     Mapped to the {@value RequestMappings#USERS} endpoint.
 *
 * @author sgreb@communicode.de
 * @since 0.2.0
 */
@RestController
@RequestMapping(USERS)
public class UserController {

    private final UserRestService userService;

    @Autowired
    public UserController(UserRestService userService) {
        this.userService = requireNonNull(userService, "userService must not be null!");
    }

    /**
     * Gets all {@link User} entities.
     * <p>
     *     This endpoint is mapped to "{@value RequestMappings#USERS}{@value RequestMappings#USER_ID}".
     *
     * @return a collection of all user entities
     */
    @GetMapping
    Set<User> getAll() {
        return userService.getAll().stream()
            .collect(Collectors.toSet());
    }

    /**
     * Gets the {@link User} entity with the specified ID.
     * <p>
     *     This endpoint is mapped to "{@value RequestMappings#USERS}{@value RequestMappings#USER_ID}".
     *
     * @param userId the ID of the user entity to get
     * @return the user data transfer object
     * @throws UserNotFoundException if the user entity with the specified ID has not been found
     */
    @GetMapping(value = USER_ID)
    User get(@PathVariable long userId) throws UserNotFoundException {
        return userService.getById(userId);
    }
}