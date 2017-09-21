/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2017
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
