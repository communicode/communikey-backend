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
 * A payload object to create a {@link User}.
 *
 * @author sgreb@communicode.de
 * @since 0.5.0
 */
public class UserCreationPayload extends UserPayload {

    @NotBlank
    private String password;

    public UserCreationPayload() {}

    public UserCreationPayload(User user) {
        super(user);
        this.password = user.getPassword();
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "UserCreationPayload{" + "password='" + password + '\'' + '}';
    }
}
