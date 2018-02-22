/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2017
 */
package de.communicode.communikey.service.payload;

import de.communicode.communikey.domain.Tag;
import org.hibernate.validator.constraints.NotBlank;

/**
 * A payload object for a {@link Tag}.
 *
 * @author dvonderbey@communicode.de
 * @since 0.18.0
 */
public class TagPayload {

    @NotBlank
    private String name;

    @NotBlank
    private String color;

    public TagPayload() {}

    public String getName() {
        return name.trim();
    }

    public String getColor() {
        return color;
    }
}
