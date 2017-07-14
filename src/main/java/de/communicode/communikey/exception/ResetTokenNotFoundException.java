/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2017
 */
package de.communicode.communikey.exception;

/**
 * Thrown to indicate that a method has been passed a not existing reset token.
 *
 * @author sgreb@communicode.de
 * @since 0.2.0
 */
public class ResetTokenNotFoundException extends RuntimeException {

    /**
     * Constructs an {@code ResetTokenNotFoundException} for the specified reset token.
     *
     * @param resetToken the reset token that has not been found
     */
    public ResetTokenNotFoundException(String resetToken) {
        super("reset token '" + resetToken + "' not found");
    }
}