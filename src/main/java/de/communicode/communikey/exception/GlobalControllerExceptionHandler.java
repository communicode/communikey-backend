/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2016
 */
package de.communicode.communikey.exception;

import static org.springframework.http.MediaType.APPLICATION_JSON;

import org.springframework.boot.autoconfigure.web.DefaultErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;

/**
 * The global exception handler that provides a method to create {@link ErrorResponse} entities.
 * <p>
 *     Exceptions for each controller are handled by the specific controller exception handler.
 *
 * @author sgreb@communicode.de
 * @since 0.2.0
 */
public class GlobalControllerExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Handles all mismatched type arguments.
     *
     * @param exception the exception to handle
     * @return the error response entity
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeException(MethodArgumentTypeMismatchException exception) {
        return createErrorResponse(
            HttpStatus.BAD_REQUEST,
            new Timestamp(Calendar.getInstance().getTimeInMillis()),
            String.format("%s should be of type %s", exception.getName(), exception.getRequiredType().getName())
        );
    }

    /**
     * Creates a new error response with {@link MediaType#APPLICATION_JSON}.
     * <p>
     *     Used by all controller exception handlers.
     *
     * @param status the HTTP status of the error
     * @param timestamp the timestamp of the error
     * @param description the description about the error
     * @return the error response
     */
    ResponseEntity<ErrorResponse> createErrorResponse(final HttpStatus status, final Timestamp timestamp, final String description) {
        final ErrorResponse errorResponse = new ErrorResponse(status, timestamp, description);
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(APPLICATION_JSON);
        return new ResponseEntity<>(errorResponse, headers, status);
    }

    /**
     * The bean to configure the default error response attributes.
     *
     * @return the configured error response attributes bean
     */
    @Bean
    public ErrorAttributes errorAttributes() {
        return new DefaultErrorAttributes() {
            @Override
            public Map<String, Object> getErrorAttributes(RequestAttributes requestAttributes, boolean includeStackTrace) {
                Map<String, Object> errorAttributes = super.getErrorAttributes(requestAttributes, false);
                errorAttributes.remove("exception");
                return errorAttributes;
            }
        };
    }
}