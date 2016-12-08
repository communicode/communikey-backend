/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2016
 */
package de.communicode.communikey.controller;

import static de.communicode.communikey.controller.RequestMappings.ROLES;
import static de.communicode.communikey.controller.RequestMappings.ROLE_ID;
import static java.util.Objects.requireNonNull;

import de.communicode.communikey.domain.Role;
import de.communicode.communikey.exception.RoleNotFoundException;
import de.communicode.communikey.service.RoleRestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * The REST API controller to process {@link Role} entities.
 * <p>
 *     Mapped to the {@value RequestMappings#ROLES} endpoint.
 *
 * @author sgreb@communicode.de
 * @since 0.2.0
 */
@RestController
@RequestMapping(ROLES)
public class RoleController {

    private final RoleRestService roleService;

    @Autowired
    public RoleController(RoleRestService roleService) {
        this.roleService = requireNonNull(roleService, "roleService must not be null!");
    }

    /**
     * Gets all {@link Role} entities.
     * <p>
     *     This endpoint is mapped to "{@value RequestMappings#ROLES}".
     *
     * @return a collection of role entities
     */
    @GetMapping
    Set<Role> getAll() {
        return roleService.getAll().stream()
            .collect(Collectors.toSet());
    }

    /**
     * Gets the {@link Role} entity with the specified ID.
     * <p>
     *     This endpoint is mapped to "{@value RequestMappings#ROLES}{@value RequestMappings#ROLE_ID}".
     *
     * @param roleId the ID of the role entity to get
     * @return the role entity
     * @throws RoleNotFoundException if the role entity with the specified ID has not been found
     */
    @GetMapping(value = ROLE_ID)
    Role get(@PathVariable long roleId) throws RoleNotFoundException {
        return roleService.getById(roleId);
    }
}
