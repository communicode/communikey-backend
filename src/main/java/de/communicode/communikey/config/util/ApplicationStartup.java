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

import de.communicode.communikey.repository.UserRepository;
import de.communicode.communikey.service.EncryptionJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static java.util.Objects.requireNonNull;

/**
 * This class acts on startup events of the application.
 *
 * @author dvonderbey@communicode.de
 * @since 0.17.2
 */
@Component
public class ApplicationStartup
    implements ApplicationListener<ApplicationReadyEvent> {

    private final EncryptionJobService encryptionJobService;
    private final UserRepository userRepository;

    @Autowired
    public ApplicationStartup(EncryptionJobService encryptionJobService,
                              UserRepository userRepository) {
        this.encryptionJobService = requireNonNull(encryptionJobService, "encryptionJobService must not be null!");
        this.userRepository = requireNonNull(userRepository, "userRepository must not be null!");
    }

    @Override
    @Transactional
    public void onApplicationEvent(final ApplicationReadyEvent event) {
        userRepository.findAll()
            .forEach(user -> encryptionJobService.createForUser(user));
        return;
    }
}
