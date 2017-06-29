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
 * A payload object to reset the password of a {@link User} with a specified reset key.
 *
 * @author sgreb@communicode.de
 * @since 0.2.0
 */
public class UserPasswordResetPayload {

    @NotBlank
    private String password;

    @NotBlank
    private String resetKey;

    public String getPassword() {
        return password;
    }

    public String getResetKey() {
        return resetKey;
    }

    @Override
    public String toString() {
        return "UserPasswordResetPayload{" +
            "password='" + password + '\'' +
            ", resetKey='" + resetKey + '\'' +
            '}';
    }
}
