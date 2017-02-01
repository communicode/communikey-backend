/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2017
 */
package de.communicode.communikey.exception.user;

import de.communicode.communikey.controller.UserController;
import de.communicode.communikey.exception.ErrorResponse;
import de.communicode.communikey.exception.GlobalControllerExceptionHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.Timestamp;
import java.util.Calendar;

/**
 * The exception handler for the {@link UserController} that returns a error as response entity.
 *
 * @author sgreb@communicode.de
 * @since 0.2.0
 */
@ControllerAdvice
public class UserControllerExceptionHandler extends GlobalControllerExceptionHandler {

    /**
     * Handles all exceptions of type {@link UserNotFoundException}.
     *
     * @param exception the exception to handle
     * @return the error as response entity
     */
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(final UserNotFoundException exception) {
        return createErrorResponse(HttpStatus.NOT_FOUND, new Timestamp(Calendar.getInstance().getTimeInMillis()), exception.getMessage());
    }

    /**
     * Handles all exceptions of type {@link UserConflictException}.
     *
     * @param exception the exception to handle
     * @return the error as response entity
     */
    @ExceptionHandler(UserConflictException.class)
    public ResponseEntity<ErrorResponse> handleUserConflictException(final UserConflictException exception) {
        return createErrorResponse(HttpStatus.CONFLICT, new Timestamp(Calendar.getInstance().getTimeInMillis()), exception.getMessage());
    }
}
