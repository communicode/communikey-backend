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
package de.communicode.communikey.config.util;

import com.google.common.collect.Maps;
import org.springframework.boot.SpringApplication;
import org.springframework.core.env.Environment;

import java.util.Map;

/**
 * Utility class to load a Spring profile to be used as default when the {@code spring.profiles.active} is not set in the environment or as command line
 * argument.
 *
 * <p>If the value is not available in {@code application.yml} then the {@code dev} profile will be used as default.
 *
 * @author sgreb@communicode.de
 * @since 0.2.0
 */
public final class DefaultProfileUtil {

    private static final String SPRING_PROFILE_DEFAULT = "spring.profiles.default";

    public static final String COMMUNIKEY_PROFILE_DEVELOPMENT = "dev";
    public static final String COMMUNIKEY_PROFILE_TEST = "test";
    public static final String COMMUNIKEY_PROFILE_PRODUCTION = "prod";

    private DefaultProfileUtil() {}

    /**
     * Set the default profile to use when no profile is configured.
     *
     * @param app the Spring application
     */
    public static void addDefaultProfile(SpringApplication app) {
        Map<String, Object> defProperties = Maps.newConcurrentMap();
        defProperties.put(SPRING_PROFILE_DEFAULT, COMMUNIKEY_PROFILE_DEVELOPMENT);
        app.setDefaultProperties(defProperties);
    }

    /**
     * Get the profiles that are applied else get default profiles.
     *
     * @param env the environment
     */
    public static String[] getActiveProfiles(Environment env) {
        String[] profiles = env.getActiveProfiles();
        if (profiles.length == 0) {
            return env.getDefaultProfiles();
        }
        return profiles;
    }
}
