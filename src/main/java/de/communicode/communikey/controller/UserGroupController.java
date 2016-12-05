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
import de.communicode.communikey.domain.UserGroupDto;
import de.communicode.communikey.domain.converter.UserGroupDtoConverter;
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

    private final UserGroupDtoConverter userGroupConverter;

    @Autowired
    public UserGroupController(UserGroupRestService userGroupService, UserGroupDtoConverter userGroupConverter) {
        this.userGroupService = requireNonNull(userGroupService, "userGroupService must not be null!");
        this.userGroupConverter = requireNonNull(userGroupConverter, "userGroupConverter must not be null!");
    }

    /**
     * Gets all {@link UserGroup} entities.
     * <p>
     *     This endpoint is mapped to "{@value RequestMappings#USER_GROUPS}{@value RequestMappings#USER_GROUP_ID}".
     *
     * @return a collection of user group data transfer objects
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
     * @return the user group data transfer object
     * @throws UserGroupNotFoundException if the user group entity with the specified ID has not been found
     */
    @GetMapping(value = USER_GROUP_ID)
    UserGroup get(@PathVariable long userGroupId) throws UserGroupNotFoundException {
        return userGroupService.getById(userGroupId);
    }

    /**
     * Converts a user group entity to the associated user group data transfer object.
     *
     * @param userGroup the user group entity to convert
     * @return the converted user group data transfer object
     */
    private UserGroupDto convertToDto(UserGroup userGroup) {
        return userGroupConverter.convert(userGroup);
    }

    /**
     * Converts a user group data transfer object to the associated user group entity.
     *
     * @param userGroupDto the user group data transfer object to convert
     * @return the converted user group entity
     * @throws UserGroupNotFoundException if the associated use group entity of the user group data transfer object has not been found
     */
    private UserGroup convertToEntity(UserGroupDto userGroupDto) throws UserGroupNotFoundException {
        return userGroupService.getById(userGroupDto.getId());
    }
}