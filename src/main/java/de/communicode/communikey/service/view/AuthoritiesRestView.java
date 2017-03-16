/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2017
 */
package de.communicode.communikey.service.view;

import de.communicode.communikey.domain.Authority;

/**
 * Defines REST views based on the {@link Authority}s granted to the current user.
 *
 * @author sgreb@communicode.de
 * @since 0.2.0
 */
public class AuthoritiesRestView {
    public interface User {}

    public interface Admin extends User {}
}