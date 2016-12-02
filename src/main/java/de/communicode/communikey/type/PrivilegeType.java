/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2016
 */
package de.communicode.communikey.type;

import de.communicode.communikey.domain.Privilege;

/**
 * Provides {@link Privilege} types.
 *
 * @author sgreb@communicode.de
 * @since 0.2.0
 */
public enum PrivilegeType {
    /**
     * The privilege for read operations.
     */
    READ,

    /**
     * The privilege for write operations.
     */
    WRITE
}