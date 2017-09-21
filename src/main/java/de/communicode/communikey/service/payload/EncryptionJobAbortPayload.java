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
 * A payload object for a {@link EncryptionJob} abort.
 *
 * @author dvonderbey@communicode.de
 * @since 0.15.0
 */
public class EncryptionJobAbortPayload {

    @NotBlank
    private String token;

    public EncryptionJobAbortPayload() {}

    public EncryptionJobAbortPayload(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
