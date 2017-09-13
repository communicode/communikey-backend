/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2017
 */
package de.communicode.communikey.exception;

import de.communicode.communikey.controller.KeyController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.Timestamp;
import java.util.Calendar;

/**
 * The exception handler for the {@link KeyController} that returns a error as response entity.
 *
 * @author sgreb@communicode.de
 * @since 0.2.0
 */
@ControllerAdvice
public class KeyControllerExceptionHandler extends GlobalControllerExceptionHandler {

    /**
     * Handles all exceptions of type {@link KeyConflictException}.
     *
     * @param exception the exception to handle
     * @return the error as response entity
     */
    @ExceptionHandler(KeyConflictException.class)
    public ResponseEntity<ErrorResponse> handleKeyConflictException(KeyConflictException exception) {
        return createErrorResponse(HttpStatus.NOT_FOUND, new Timestamp(Calendar.getInstance().getTimeInMillis()), exception.getMessage());
    }

    /**
     * Handles all exceptions of type {@link KeyNotFoundException}.
     *
     * @param exception the exception to handle
     * @return the error as response entity
     */
    @ExceptionHandler(KeyNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleKeyNotFoundException(KeyNotFoundException exception) {
        return createErrorResponse(HttpStatus.NOT_FOUND, new Timestamp(Calendar.getInstance().getTimeInMillis()), exception.getMessage());
    }

    /**
     * Handles all exceptions of type {@link UserEncryptedPasswordNotFoundException}.
     *
     * @param exception the exception to handle
     * @return the error as response entity
     */
    @ExceptionHandler(UserEncryptedPasswordNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleKeyNotFoundException(UserEncryptedPasswordNotFoundException exception) {
        return createErrorResponse(HttpStatus.NOT_FOUND, new Timestamp(Calendar.getInstance().getTimeInMillis()), exception.getMessage());
    }

    /**
     * Handles all exceptions of type {@link HashidNotValidException}.
     *
     * @param exception the exception to handle
     * @return the error as response entity
     */
    @ExceptionHandler(HashidNotValidException.class)
    public ResponseEntity<ErrorResponse> handleHashidNotValidException(HashidNotValidException exception) {
        return createErrorResponse(HttpStatus.NOT_FOUND, new Timestamp(Calendar.getInstance().getTimeInMillis()), exception.getMessage());
    }
}
