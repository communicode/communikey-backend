/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2016
 */
package de.communicode.communikey.controller;

import static de.communicode.communikey.controller.RequestMappings.ROLES;
import static java.util.Objects.requireNonNull;

import de.communicode.communikey.domain.Role;
import de.communicode.communikey.service.RoleRestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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
}
