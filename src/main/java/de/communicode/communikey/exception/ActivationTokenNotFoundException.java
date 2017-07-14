/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2017
 */
package de.communicode.communikey.exception;

/**
 * Thrown to indicate that a method has been passed a not existing activation token.
 *
 * @author sgreb@communicode.de
 * @since 0.2.0
 */
public class ActivationTokenNotFoundException extends RuntimeException {

    /**
     * Constructs an {@code ActivationTokenNotFoundException} for the specified activation token.
     *
     * @param activationToken the activation token that has not been found
     */
    public ActivationTokenNotFoundException(String activationToken) {
        super("activation token '" + activationToken +"' not found");
    }
}