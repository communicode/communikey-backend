/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2017
 */
package de.communicode.communikey.service.payload;

import de.communicode.communikey.domain.KeyCategory;
import org.hibernate.validator.constraints.NotBlank;

/**
 * A payload object for a {@link KeyCategory}.
 *
 * @author sgreb@communicode.de
 * @since 0.2.0
 */
public class KeyCategoryPayload {

    @NotBlank
    private String name;

    public KeyCategoryPayload() {}

    public String getName() {
        return name.trim();
    }

    @Override
    public String toString() {
        return "KeyCategoryPayload{" + "name='" + name + '\'' + '}';
    }
}
