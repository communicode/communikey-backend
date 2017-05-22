/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2017
 */
package de.communicode.communikey.service.payload;

import de.communicode.communikey.domain.Key;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Size;

/**
 * A payload object for a {@link Key}.
 *
 * @author sgreb@communicode.de
 * @since 0.2.0
 */
public class KeyPayload {

    @NotBlank
    private String login;

    @NotBlank
    private String password;

    @NotBlank
    @Size(max = 100)
    private String name;

    public KeyPayload() {}

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name.trim();
    }
}
