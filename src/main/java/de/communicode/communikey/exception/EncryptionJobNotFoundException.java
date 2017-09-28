/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2017
 */
package de.communicode.communikey.exception;

import de.communicode.communikey.domain.EncryptionJob;

/**
 * Thrown to indicate that a method has been passed a non existing {@link EncryptionJob}.
 *
 * @author dvonderbey@communicode.de
 * @since 0.15.0
 */
public class EncryptionJobNotFoundException extends RuntimeException {

    /**
     * Constructs a {@code EncryptionJobNotFoundException}.
     */
    public EncryptionJobNotFoundException() {
        super("encryption job not found");
    }

    /**
     * Constructs an {@code EncryptionJobNotFoundException} with the specified {@link EncryptionJob} token.
     *
     * @param encryptionJobToken the token of the encryption job that has not been found
     */
    public EncryptionJobNotFoundException(String encryptionJobToken) {
        super("enryption job with token '" + encryptionJobToken + "' not found");
    }
}
