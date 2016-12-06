/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2016
 */
package de.communicode.communikey.exception;

import de.communicode.communikey.domain.Privilege;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Thrown to indicate that a method has been passed an not existing {@link Privilege} entity.
 *
 * @author sgreb@communicode.de
 * @since 0.2.0
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class PrivilegeNotFoundException extends RuntimeException {

    /**
     * Constructs an {@code PrivilegeNotFoundException} with the specified {@link Privilege} ID applied to the message.
     *
     * @param privilegeId the ID of the privilege entity that has not been found
     */
    public PrivilegeNotFoundException(long privilegeId) {
        super("could not find privilege with ID " + privilegeId);
    }

    /**
     * Constructs an {@code PrivilegeNotFoundException} with the specified {@link Privilege} name applied to the message.
     *
     * @param name the name of the privilege entity that has not been found
     */
    public PrivilegeNotFoundException(String name) {
        super("could not find privilege with name " + name);
    }
}
