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

import de.communicode.communikey.controller.UserController;
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
     * Handles all exceptions of type {@link ResetTokenNotFoundException}.
     *
     * @param exception the exception to handle
     * @return the error as response entity
     */
    @ExceptionHandler(ResetTokenNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResetTokenNotFoundException(final ResetTokenNotFoundException exception) {
        return createErrorResponse(HttpStatus.NOT_FOUND, new Timestamp(Calendar.getInstance().getTimeInMillis()), exception.getMessage());
    }

    /**
     * Handles all exceptions of type {@link ActivationTokenNotFoundException}.
     *
     * @param exception the exception to handle
     * @return the error as response entity
     */
    @ExceptionHandler(ActivationTokenNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleActivationTokenNotFoundException(final ActivationTokenNotFoundException exception) {
        return createErrorResponse(HttpStatus.NOT_FOUND, new Timestamp(Calendar.getInstance().getTimeInMillis()), exception.getMessage());
    }

    /**
     * Handles all exceptions of type {@link UserNotActivatedException}.
     *
     * @param exception the exception to handle
     * @return the error as response entity
     */
    @ExceptionHandler(UserNotActivatedException.class)
    public ResponseEntity<ErrorResponse> handleUserNotActivatedException(final UserNotActivatedException exception) {
        return createErrorResponse(HttpStatus.UNAUTHORIZED, new Timestamp(Calendar.getInstance().getTimeInMillis()), exception.getMessage());
    }

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
