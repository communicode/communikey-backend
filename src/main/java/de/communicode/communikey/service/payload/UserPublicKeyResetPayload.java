/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2017
 */
package de.communicode.communikey.service.payload;

import de.communicode.communikey.domain.User;
import org.hibernate.validator.constraints.NotBlank;

/**
 * A payload object to reset the password of a {@link User} with a specified reset token.
 *
 * @author sgreb@communicode.de
 * @since 0.2.0
 */
public class UserPublicKeyResetPayload {

    @NotBlank
    private String publicKey;

    @NotBlank
    private String resetToken;

    public String getPublicKey() {
        return publicKey;
    }

    public String getResetToken() {
        return resetToken;
    }

    @Override
    public String toString() {
        return "UserPublicKeyResetPayload{" +
            "publicKey='" + publicKey + '\'' +
            ", resetToken='" + resetToken + '\'' +
            '}';
    }
}
