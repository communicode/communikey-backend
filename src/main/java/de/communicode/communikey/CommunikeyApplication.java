/*
 * This file is part of communikey.
 * Copyright (C) 2016-2018  communicode AG <communicode.de>
 *
 * communikey is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package de.communicode.communikey;

import static de.communicode.communikey.config.util.DefaultProfileUtil.COMMUNIKEY_PROFILE_DEVELOPMENT;
import static de.communicode.communikey.config.util.DefaultProfileUtil.COMMUNIKEY_PROFILE_PRODUCTION;
import static java.util.Objects.requireNonNull;

import de.communicode.communikey.config.CommunikeyProperties;
import de.communicode.communikey.config.util.DefaultProfileUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;

import java.util.Arrays;
import java.util.Collection;

/**
 * A simple centralized, cross-platform credential manager.
 *
 * <p>This is the application boot class.
 *
 * @author sgreb@communicode.de
 * @since 0.1.0
 */
@SpringBootApplication
@EnableConfigurationProperties(CommunikeyProperties.class)
public class CommunikeyApplication {

    private static final Logger log = LogManager.getLogger();

    private final Environment env;
    public static final String COMMUNIKEY_REST_API_VERSION = "0.2.0";

    @Autowired
    public CommunikeyApplication(Environment env) {
        this.env = requireNonNull(env, "env must not be null!");
    }

    /**
     * Initializes the communikey application.
     */
    @PostConstruct
    public void bootstrap() {
        Collection<String> activeProfiles = Arrays.asList(env.getActiveProfiles());
        if (activeProfiles.contains(COMMUNIKEY_PROFILE_DEVELOPMENT) && activeProfiles.contains(COMMUNIKEY_PROFILE_PRODUCTION)) {
            log.error("Application misconfiguration detected, it should not run with both the 'dev' and 'prod' profiles at the same time!");
        }
    }

    /**
     * Runs the application.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(CommunikeyApplication.class);
        DefaultProfileUtil.addDefaultProfile(app);
        Environment env = app.run(args).getEnvironment();
        log.info("Application '{}' is running! Active Profile(s): {}", env.getProperty("spring.application.name"), env.getActiveProfiles());
    }
}
