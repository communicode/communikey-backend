/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2016
 */
package de.communicode.communikey.exception;

import de.communicode.communikey.controller.PrivilegeController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

import java.sql.Timestamp;
import java.util.Calendar;

/**
 * The exception handler for the {@link PrivilegeController} that returns {@link ResponseEntity} of type {@link ErrorResponse}.
 *
 * @author sgreb@communicode.de
 * @since 0.2.0
 */
@ControllerAdvice
public class PrivilegeControllerExceptionHandler extends GlobalControllerExceptionHandler {

    /**
     * Handles all exceptions of type {@link PrivilegeNotFoundException}.
     *
     * @param exception the exception to handle
     * @param request the HTTP servlet request to extract data for the response entity of
     * @return the response entity
     */
    @ExceptionHandler(PrivilegeNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlePrivilegeNotFoundException(final PrivilegeNotFoundException exception, HttpServletRequest request) {
        return createErrorResponse(HttpStatus.NOT_FOUND, new Timestamp(Calendar.getInstance().getTimeInMillis()), exception.getMessage(),
            request);
    }

    /**
     * Handles all exceptions of type {@link PrivilegeConflictException}.
     *
     * @param exception the exception to handle
     * @param request the HTTP servlet request to extract data for the response entity of
     * @return the response entity
     */
    @ExceptionHandler(PrivilegeConflictException.class)
    public ResponseEntity<ErrorResponse> handlePrivilegeConflictException(final PrivilegeConflictException exception, HttpServletRequest request) {
        return createErrorResponse(HttpStatus.CONFLICT, new Timestamp(Calendar.getInstance().getTimeInMillis()), exception.getMessage(),
            request);
    }
}
