/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2016
 */
package de.communicode.communikey.exception;

import org.springframework.http.HttpStatus;

import java.sql.Timestamp;

/**
 * Represents a error response.
 *
 * @author sgreb@communicode.de
 * @since 0.2.0
 */
public class ErrorResponse {

    private Timestamp timestamp;
    private int status;
    private String reason;
    private String description;

    /**
     * Constructs a new error  response with the specified {@link HttpStatus}, the timestamp of the error and error description.
     *
     * @param httpStatus the HTTP status of the error
     * @param timestamp the timestamp of the error
     * @param description the description about this error
     */
    public ErrorResponse(HttpStatus httpStatus, Timestamp timestamp, String description) {
        this.status = httpStatus.value();
        this.reason = httpStatus.getReasonPhrase();
        this.timestamp = timestamp;
        this.description = description;
    }

    public Object getDescription() {
        return description;
    }

    public String getReason() {
        return reason;
    }

    public int getStatus() {
        return status;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}