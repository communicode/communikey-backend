/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2017
 */
package de.communicode.communikey.exception;

import de.communicode.communikey.domain.Tag;

/**
 * Thrown to indicate that a method has been passed a not existing {@link Tag}.
 *
 * @author dvonderbey@communicode.de
 * @since 0.18.0
 */
public class TagNotFoundException extends RuntimeException {

    /**
     * Constructs a {@code TagNotFoundException}.
     */
    public TagNotFoundException() {
        super("tag not found");
    }

    /**
     * Constructs a {@code TagNotFoundException} with the specified {@link Tag} Hashid.
     *
     * @param tagHashid the Hashid of the tag that has not been found
     * @since 0.12.0
     */
    public TagNotFoundException(String tagHashid) {
        super("tag with ID '" + tagHashid + "' not found");
    }
}
