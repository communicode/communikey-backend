/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2016
 */
package de.communicode.communikey.controller;

import static de.communicode.communikey.config.CommunikeyConstants.ENDPOINT_PASSWORDS;
import static de.communicode.communikey.config.CommunikeyConstants.ENDPOINT_ROOT;
import static de.communicode.communikey.util.CommunikeyConstantsUtil.asRedirect;

import de.communicode.communikey.config.CommunikeyConstants;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * The controller for the root endpoint which represents the main endpoint of this application.
 *
 * @author sgreb@communicode.de
 * @since 0.2.0
 */
public class RootController {

    /**
     * The root endpoint without any parameters.
     *
     * <p>
     *     Defaults to the {@value CommunikeyConstants#ENDPOINT_PASSWORDS} endpoint.
     *
     * @return the string to the endpoint redirection
     */
    @GetMapping(value = ENDPOINT_ROOT)
    String root() {
        return asRedirect(ENDPOINT_PASSWORDS);
    }
}
