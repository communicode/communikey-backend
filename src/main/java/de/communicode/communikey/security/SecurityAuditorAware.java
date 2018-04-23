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
package de.communicode.communikey.security;

import de.communicode.communikey.config.CommunikeyProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;

import java.util.Objects;
import java.util.Optional;

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
    public Optional<String> getCurrentAuditor() {
        String userName = SecurityUtils.getCurrentUserLogin();
        if (userName != null && !Objects.equals(userName, "anonymousUser")) {
            return ofNullable(userName);
        }
        return ofNullable(communikeyProperties.getSecurity().getRoot().getLogin());
    }
}
