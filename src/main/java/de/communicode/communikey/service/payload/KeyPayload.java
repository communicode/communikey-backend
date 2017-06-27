/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2017
 */
package de.communicode.communikey.service.payload;

import de.communicode.communikey.domain.Key;
import org.hibernate.validator.constraints.NotBlank;

/**
 * A payload object for a {@link Key}.
 *
 * @author sgreb@communicode.de
 * @since 0.2.0
 */
public class KeyPayload {

    private Long categoryId;

    @NotBlank
    private String login;

    @NotBlank
    private String name;

    @NotBlank
    private String password;

    public KeyPayload() {}

    /**
     * @return the category ID
     * @since 0.8.0
     */
    public Long getCategoryId() {
        return this.categoryId;
    }

    public String getLogin() {
        return login;
    }

    public String getName() {
        return name.trim();
    }

    public String getPassword() {
        return password;
    }
}
