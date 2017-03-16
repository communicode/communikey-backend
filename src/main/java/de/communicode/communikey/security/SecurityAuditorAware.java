/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2017
 */
package de.communicode.communikey.security;

import de.communicode.communikey.config.CommunikeyProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import static java.util.Objects.requireNonNull;

import java.util.Objects;

/**
 * Implementation of AuditorAware based on Spring Security.
 *
 * @author sgreb@communicode.de
 * @since 0.2.0
 */
@Component
public class SecurityAuditorAware implements AuditorAware<String> {

    private final CommunikeyProperties communikeyProperties;

    @Autowired
    public SecurityAuditorAware(CommunikeyProperties communikeyProperties) {
        this.communikeyProperties = requireNonNull(communikeyProperties, "communikeyProperties must not be null!");
    }

    @Override
    public String getCurrentAuditor() {
        String userName = SecurityUtils.getCurrentUserLogin();
        if (userName != null && !Objects.equals(userName, "anonymousUser")) {
            return userName;
        }
        return communikeyProperties.getSecurity().getRoot().getLogin();
    }
}
