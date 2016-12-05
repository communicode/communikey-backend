/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2016
 */
package de.communicode.communikey.exception;


import de.communicode.communikey.domain.Role;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Thrown to indicate that a method has been passed an not existing {@link Role} entity.
 *
 * @author sgreb@communicode.de
 * @since 0.2.0
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class RoleNotFoundException extends RuntimeException {

    /**
     * Constructs an {@code RoleNotFoundException} with the specified {@link Role} ID applied to the message.
     *
     * @param roleId the ID of the role entity that has not been found
     */
    public RoleNotFoundException(long roleId) {
        super("could not find role with ID " + roleId);
    }

    /**
     * Constructs an {@code RoleNotFoundException} with the specified {@link Role} name applied to the message.
     *
     * @param name the name of the role entity that has not been found
     */
    public RoleNotFoundException(String name) {
        super("could not find role with name " + name);
    }
}
