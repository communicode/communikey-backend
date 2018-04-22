/*
 * This file is part of communikey.
 * Copyright (C) 2016-2018  communicode AG <communicode.de>
 *
 * communikey is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package de.communicode.communikey.exception;

import de.communicode.communikey.controller.CrowdEncryptionController;
import de.communicode.communikey.service.payload.EncryptionJobStatusPayload;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.web.bind.annotation.ControllerAdvice;

/**
 * The exception handler for the {@link CrowdEncryptionController} that returns a error as response entity.
 *
 * @author dvonderbey@communicode.de
 * @since 0.15.0
 */
@ControllerAdvice
public class CrowdEncryptionControllerExceptionHandler extends GlobalControllerExceptionHandler {

    /**
     * Handles all exceptions of type {@link EncryptionJobNotFoundException}.
     *
     * @param exception the exception to handle
     * @return the error as response entity
     */
    @MessageExceptionHandler(EncryptionJobNotFoundException.class)
    @SendToUser(value="/queue/errors")
    public EncryptionJobStatusPayload handleKeyConflictException(EncryptionJobNotFoundException exception) {
        return new EncryptionJobStatusPayload("Error: " + exception);
    }
}
