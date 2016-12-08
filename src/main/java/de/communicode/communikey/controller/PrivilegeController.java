/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2016
 */
package de.communicode.communikey.controller;

import static de.communicode.communikey.controller.RequestMappings.PRIVILEGES;
import static de.communicode.communikey.controller.RequestMappings.PRIVILEGE_ID;
import static java.util.Objects.requireNonNull;

import de.communicode.communikey.domain.Privilege;
import de.communicode.communikey.exception.PrivilegeNotFoundException;
import de.communicode.communikey.service.PrivilegeRestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * The REST API controller to process {@link Privilege} entities.
 * <p>
 *     Mapped to the {@value RequestMappings#PRIVILEGES} endpoint.
 *
 * @author sgreb@communicode.de
 * @since 0.2.0
 */
@RestController
@RequestMapping(PRIVILEGES)
public class PrivilegeController {

    private final PrivilegeRestService privilegeRestService;

    @Autowired
    public PrivilegeController(PrivilegeRestService privilegeRestService) {
        this.privilegeRestService = requireNonNull(privilegeRestService, "privilegeRestService must not be null!");
    }

    /**
     * Gets all {@link Privilege} entities.
     * <p>
     *     This endpoint is mapped to "{@value RequestMappings#PRIVILEGES}".
     *
     * @return a collection of privilege entities
     */
    @GetMapping
    Set<Privilege> getAll() {
        return privilegeRestService.getAll().stream()
            .collect(Collectors.toSet());
    }

    /**
     * Gets the {@link Privilege} entity with the specified ID.
     * <p>
     *     This endpoint is mapped to "{@value RequestMappings#PRIVILEGES}{@value RequestMappings#PRIVILEGE_ID}".
     *
     * @param privilegeId the ID of the privilege entity to get
     * @return the privilege entity
     * @throws PrivilegeNotFoundException if the privilege entity with the specified ID has not been found
     */
    @GetMapping(value = PRIVILEGE_ID)
    Privilege get(@PathVariable long privilegeId) throws PrivilegeNotFoundException {
        return privilegeRestService.getById(privilegeId);
    }
}
