/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2017
 */
package de.communicode.communikey.exception;

/**
 * Thrown to indicate that a method has been passed a not existing activation key.
 *
 * @author sgreb@communicode.de
 * @since 0.2.0
 */
public class ActivationKeyNotFoundException extends RuntimeException {

    /**
     * Constructs an {@code ActivationKeyNotFoundException} for the specified activation key.
     *
     * @param activationKey the activation key that has not been found
     */
    public ActivationKeyNotFoundException(String activationKey) {
        super("activation key '" + activationKey +"' not found");
    }
}