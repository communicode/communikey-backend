/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2016
 */
package de.communicode.communikey.controller;

import static de.communicode.communikey.controller.RequestMappings.PRIVILEGES;
import static java.util.Objects.requireNonNull;

import de.communicode.communikey.domain.Privilege;
import de.communicode.communikey.service.PrivilegeRestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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
}
