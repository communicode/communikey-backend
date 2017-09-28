/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2017
 */
package de.communicode.communikey.service.payload;

import de.communicode.communikey.domain.EncryptionJob;
import org.hibernate.validator.constraints.NotBlank;

/**
 * A payload object for a {@link EncryptionJob} fulfillment.
 *
 * @author dvonderbey@communicode.de
 * @since 0.15.0
 */
public class EncryptionJobPayload {

    @NotBlank
    private String encryptedPassword;

    public EncryptionJobPayload() {}

    public String getEncryptedPassword() {
        return encryptedPassword;
    }

    public void setEncryptedPassword(String encryptedPassword) {
        this.encryptedPassword = encryptedPassword;
    }
}
