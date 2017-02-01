/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2017
 */
package de.communicode.communikey.domain.request;

import de.communicode.communikey.domain.Key;
import org.hibernate.validator.constraints.NotBlank;

/**
 * A request payload object for a {@link Key} entity.
 *
 * @author sgreb@communicode.de
 * @since 0.2.0
 */
public class KeyRequest {
    @NotBlank
    private String value;
    @NotBlank
    private String name;

    private KeyRequest() {}

    /**
     * Constructs a key entity request payload object with the required fields.
     *
     * @param name the name of the key
     * @param value the value of the key
     */
    public KeyRequest(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
