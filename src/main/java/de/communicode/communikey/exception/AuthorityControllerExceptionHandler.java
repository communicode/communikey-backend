/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2017
 */
package de.communicode.communikey.exception;

import de.communicode.communikey.controller.AuthorityController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.Timestamp;
import java.util.Calendar;

/**
 * The exception handler for the {@link AuthorityController} that returns a error as response entity.
 *
 * @author sgreb@communicode.de
 * @since 0.3.0
 */
@ControllerAdvice
public class AuthorityControllerExceptionHandler extends GlobalControllerExceptionHandler {
    /**
     * Handles all exceptions of type {@link AuthorityNotFoundException}.
     *
     * @param exception the exception to handle
     * @return the error as response entity
     */
    @ExceptionHandler(AuthorityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleAuthorityNotFoundException(final AuthorityNotFoundException exception) {
        return createErrorResponse(HttpStatus.NOT_FOUND, new Timestamp(Calendar.getInstance().getTimeInMillis()), exception.getMessage());
    }
}
