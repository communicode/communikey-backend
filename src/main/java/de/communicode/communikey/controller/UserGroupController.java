/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2016
 */
package de.communicode.communikey.controller;

import static de.communicode.communikey.controller.RequestMappings.USER_GROUPS;
import static de.communicode.communikey.controller.RequestMappings.USER_GROUP_ID;
import static java.util.Objects.requireNonNull;

import de.communicode.communikey.domain.UserGroup;
import de.communicode.communikey.exception.UserGroupNotFoundException;
import de.communicode.communikey.service.UserGroupRestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * The REST API controller to process {@link UserGroup} entities.
 * <p>
 *     Mapped to the {@value RequestMappings#USER_GROUPS} endpoint.
 *
 * @author sgreb@communicode.de
 * @since 0.2.0
 */
@RestController
@RequestMapping(USER_GROUPS)
public class UserGroupController {

    private final UserGroupRestService userGroupService;

    @Autowired
    public UserGroupController(UserGroupRestService userGroupService) {
        this.userGroupService = requireNonNull(userGroupService, "userGroupService must not be null!");
    }

    /**
     * Gets all {@link UserGroup} entities.
     * <p>
     *     This endpoint is mapped to "{@value RequestMappings#USER_GROUPS}{@value RequestMappings#USER_GROUP_ID}".
     *
     * @return a collection of user group entities
     */
    @GetMapping
    Set<UserGroup> getAll() {
        return userGroupService.getAll().stream()
            .collect(Collectors.toSet());
    }

    /**
     * Gets the {@link UserGroup} entity with the specified ID.
     * <p>
     *     This endpoint is mapped to "{@value RequestMappings#USER_GROUPS}{@value RequestMappings#USER_GROUP_ID}".
     *
     * @param userGroupId the ID of the user group entity to get
     * @return the user group entity
     * @throws UserGroupNotFoundException if the user group entity with the specified ID has not been found
     */
    @GetMapping(value = USER_GROUP_ID)
    UserGroup get(@PathVariable long userGroupId) throws UserGroupNotFoundException {
        return userGroupService.getById(userGroupId);
    }
}