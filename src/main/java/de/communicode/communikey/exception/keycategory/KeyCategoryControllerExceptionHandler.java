/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2017
 */
package de.communicode.communikey.exception.keycategory;

import de.communicode.communikey.controller.KeyCategoryController;
import de.communicode.communikey.exception.ErrorResponse;
import de.communicode.communikey.exception.GlobalControllerExceptionHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.Timestamp;
import java.util.Calendar;

/**
 * The exception handler for the {@link KeyCategoryController} that returns a error as response entity.
 *
 * @author sgreb@communicode.de
 * @since 0.2.0
 */
@ControllerAdvice
public class KeyCategoryControllerExceptionHandler extends GlobalControllerExceptionHandler {

    /**
     * Handles all exceptions of type {@link KeyCategoryNotFoundException}.
     *
     * @param exception the exception to handle
     * @return the error as response entity
     */
    @ExceptionHandler(KeyCategoryNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleKeyCategoryNotFoundException(final KeyCategoryNotFoundException exception) {
        return createErrorResponse(HttpStatus.NOT_FOUND, new Timestamp(Calendar.getInstance().getTimeInMillis()), exception.getMessage());
    }

    /**
     * Handles all exceptions of type {@link InvalidKeyCategoryNameException}.
     *
     * @param exception the exception to handle
     * @return the error as response entity
     */
    @ExceptionHandler(InvalidKeyCategoryNameException.class)
    public ResponseEntity<ErrorResponse> handleInvalidKeyCategoryNameException(final InvalidKeyCategoryNameException exception) {
        return createErrorResponse(HttpStatus.BAD_REQUEST, new Timestamp(Calendar.getInstance().getTimeInMillis()), exception.getMessage());
    }
}