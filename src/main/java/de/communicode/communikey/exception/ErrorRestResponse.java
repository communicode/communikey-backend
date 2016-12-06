/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2016
 */
package de.communicode.communikey.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;

import java.sql.Timestamp;

/**
 * Represents a entity for error REST responses.
 *
 * @author sgreb@communicode.de
 * @since 0.2.0
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
class ErrorRestResponse {

    private Timestamp timestamp;
    private int status;
    private String errorMessage;

    /**
     * Constructs a new error REST response with the specified {@link HttpStatus} value, the timestamp of the error and error message.
     *
     * @param status the HTTP status value
     * @param timestamp the timestamp of the error
     * @param errorMessage the message about this error
     */
    ErrorRestResponse(int status, Timestamp timestamp, String errorMessage) {
        this.status =status;
        this.timestamp = timestamp;
        this.errorMessage = errorMessage;
    }

    Object getErrorMessage() {
        return errorMessage;
    }

    int getStatus() {
        return status;
    }

    Timestamp getTimestamp() {
        return timestamp;
    }

    void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    void setStatus(int status) {
        this.status = status;
    }

    void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}