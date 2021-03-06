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

import static java.util.Arrays.asList;
import static org.springframework.http.MediaType.APPLICATION_JSON;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolationException;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * A global exception handler that provides methods to create {@link ErrorResponse} entities.
 *
 * <p>Exceptions for each controller are handled by the specific controller exception handler.
 *
 * @author sgreb@communicode.de
 * @since 0.2.0
 */
public class GlobalControllerExceptionHandler {

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeException(MethodArgumentTypeMismatchException exception) {
        return createErrorResponse(
            HttpStatus.BAD_REQUEST,
            new Timestamp(Calendar.getInstance().getTimeInMillis()),
            String.format("Type mismatch: %s", asList(exception.getName(), exception.getValue()))
        );
    }

    @ExceptionHandler(UnrecognizedPropertyException.class)
    public ResponseEntity<ErrorResponse> handleUnrecognizedPropertyException(UnrecognizedPropertyException exception) {
        return createErrorResponse(
            HttpStatus.BAD_REQUEST,
            new Timestamp(Calendar.getInstance().getTimeInMillis()),
            String.format("Unknown field: %s", exception.getPropertyName())
        );
    }

    @ExceptionHandler(InvalidFormatException.class)
    public ResponseEntity<ErrorResponse> handleInvalidFormatException(InvalidFormatException exception) {
        return createErrorResponse(
            HttpStatus.BAD_REQUEST,
            new Timestamp(Calendar.getInstance().getTimeInMillis()),
            String.format("Invalid value format: %s", asList(exception.getPath().get(0).getFieldName(), exception.getValue()))
        );
    }

    @ExceptionHandler(JsonProcessingException.class)
    public ResponseEntity<ErrorResponse> handleJsonProcessingException() {
        return createErrorResponse(
            HttpStatus.BAD_REQUEST,
            new Timestamp(Calendar.getInstance().getTimeInMillis()),
            "Malformed JSON"
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        return createErrorResponse(
            HttpStatus.BAD_REQUEST,
            new Timestamp(Calendar.getInstance().getTimeInMillis()),
            exception.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> fieldError.getField() + " " +fieldError.getDefaultMessage())
                .collect(Collectors.toList())
        );
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException exception) {
        return createErrorResponse(
            HttpStatus.PRECONDITION_FAILED,
            new Timestamp(Calendar.getInstance().getTimeInMillis()),
            exception.getConstraintViolations().stream()
                .map(constraintViolation -> constraintViolation.getPropertyPath().toString() + " " + constraintViolation.getMessage())
                .collect(Collectors.toList())
        );
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingServletRequestParameterException(MissingServletRequestParameterException exception) {
        return createErrorResponse(
            HttpStatus.BAD_REQUEST,
            new Timestamp(Calendar.getInstance().getTimeInMillis()),
            exception.getMessage()
        );
    }

    /**
     * Creates a new error response with the specified reason.
     *
     * <p>Used by all controller exception handlers.
     *
     * @param status the HTTP status of the error
     * @param timestamp the timestamp of the error
     * @param error the error reason
     * @return the error as response entity
     */
    ResponseEntity<ErrorResponse> createErrorResponse(HttpStatus status, Timestamp timestamp, String error) {
        final ErrorResponse errorResponse = new ErrorResponse(status, timestamp, error);
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(APPLICATION_JSON);
        return new ResponseEntity<>(errorResponse, headers, status);
    }

    /**
     * Creates a new error response with the specified  list of reasons.
     *
     * <p>Used by all controller exception handlers.
     *
     * @param status the HTTP status of the error
     * @param timestamp the timestamp of the error
     * @param errors the list of error reasons
     * @return the error as response entity
     */
    private ResponseEntity<ErrorResponse> createErrorResponse(HttpStatus status, Timestamp timestamp, List<String> errors) {
        final ErrorResponse errorResponse = new ErrorResponse(status, timestamp, errors);
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
            public Map<String, Object> getErrorAttributes(ServerRequest request, boolean includeStackTrace) {
                Map<String, Object> errorAttributes = super.getErrorAttributes(request, false);
                errorAttributes.remove("exception");
                errorAttributes.remove("path");
                errorAttributes.remove("message");
                return errorAttributes;
            }
        };
    }
}
