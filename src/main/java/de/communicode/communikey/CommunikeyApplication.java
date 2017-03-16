/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2017
 */
package de.communicode.communikey;

import static de.communicode.communikey.config.util.DefaultProfileUtil.COMMUNIKEY_PROFILE_DEVELOPMENT;
import static de.communicode.communikey.config.util.DefaultProfileUtil.COMMUNIKEY_PROFILE_PRODUCTION;
import static java.util.Objects.requireNonNull;

import de.communicode.communikey.config.CommunikeyProperties;
import de.communicode.communikey.config.util.DefaultProfileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Collection;

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
@EnableConfigurationProperties(CommunikeyProperties.class)
public class CommunikeyApplication {

    private static final Logger log = LoggerFactory.getLogger(CommunikeyApplication.class);

    private final Environment env;

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
     * @throws UnknownHostException if the local host name could not be resolved into an address
     */
    public static void main(String[] args) throws UnknownHostException {
        SpringApplication app = new SpringApplication(CommunikeyApplication.class);
        DefaultProfileUtil.addDefaultProfile(app);
        Environment env = app.run(args).getEnvironment();
        log.info(
                "\nApplication '{}' is running!\n" +
                "Active Profile(s): {}\n" +
                "Access URLs:\n" +
                "  Local:    http://localhost:{}\n" +
                "  External: http://{}:{}\n",
            env.getProperty("spring.application.name"),
            env.getActiveProfiles(),
            env.getProperty("server.port"),
            InetAddress.getLocalHost().getHostAddress(),
            env.getProperty("server.port")
        );
    }
}
