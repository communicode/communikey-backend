/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2017
 */
package de.communicode.communikey.config.util;

import de.communicode.communikey.repository.UserRepository;
import de.communicode.communikey.service.AuthorityService;
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

    private EncryptionJobService encryptionJobService;
    private UserRepository userRepository;

    @Autowired
    public ApplicationStartup(EncryptionJobService encryptionJobService,
                              UserRepository userRepository,
                              AuthorityService authorityService) {
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
