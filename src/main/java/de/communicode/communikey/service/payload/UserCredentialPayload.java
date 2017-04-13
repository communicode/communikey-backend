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
 * A payload object to provide the {@link User} credentials consisting of the login and the password.
 *
 * @author sgreb@communicode.de
 * @author dvonderbey@communicode.de
 * @since 0.4.0
 */
public class UserCredentialPayload {

    @NotBlank
    private String login;

    @NotBlank
    private String password;

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "UserCredentialPayload{" +
            "login='" + login + '\'' +
            ", password='" + password + '\'' +
            '}';
    }
}
