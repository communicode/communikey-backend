/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2016
 */
package de.communicode.communikey;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * A simple centralized, cross-platform credential manager.
 *
 * <p>
 *   This is the application boot class.
 *
 * @author sgreb@communicode.de
 * @since 0.1.0
 */
@SpringBootApplication
public class CommunikeyApplication {

    /**
     * The ID of the application for the REST API and OAuth2 security flow.
     *
     * @since 0.2.0
     */
    public static final String APP_ID = "communikey";

    public static void main(String[] args) {
        SpringApplication.run(CommunikeyApplication.class, args);
    }
}
