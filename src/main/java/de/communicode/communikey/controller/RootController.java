/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2016
 */
package de.communicode.communikey.controller;

import static de.communicode.communikey.controller.RequestMappings.KEYS;
import static de.communicode.communikey.controller.RequestMappings.ROOT;
import static de.communicode.communikey.util.CommunikeyConstantsUtil.asRedirect;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * The controller for the root endpoint which represents the main endpoint of this application.
 * <p>
 *     Mapped to the "{@value RequestMappings#ROOT}" endpoint.
 *
 * @author sgreb@communicode.de
 * @since 0.2.0
 */
@RequestMapping(ROOT)
public class RootController {

    /**
     * The root endpoint without any parameters.
     * <p>
     *     Defaults to the "{@value RequestMappings#KEYS}" endpoint.
     *
     * @return the string to the endpoint redirection
     */
    @GetMapping
    String root() {
        return asRedirect(KEYS);
    }
}
