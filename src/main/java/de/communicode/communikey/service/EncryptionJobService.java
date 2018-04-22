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
package de.communicode.communikey.service;

import de.communicode.communikey.domain.EncryptionJob;
import de.communicode.communikey.domain.User;
import de.communicode.communikey.domain.Key;
import de.communicode.communikey.domain.KeyCategory;
import de.communicode.communikey.domain.UserGroup;
import de.communicode.communikey.domain.UserEncryptedPassword;
import de.communicode.communikey.exception.EncryptionJobNotFoundException;
import de.communicode.communikey.exception.UserNotFoundException;
import de.communicode.communikey.repository.EncryptionJobRepository;
import de.communicode.communikey.repository.UserEncryptedPasswordRepository;
import de.communicode.communikey.repository.UserRepository;
import de.communicode.communikey.repository.KeyRepository;
import de.communicode.communikey.repository.KeyCategoryRepository;
import de.communicode.communikey.security.AuthoritiesConstants;
import de.communicode.communikey.service.payload.EncryptionJobAbortPayload;
import de.communicode.communikey.service.payload.EncryptionJobPayload;
import de.communicode.communikey.service.payload.EncryptionJobStatusPayload;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static de.communicode.communikey.controller.RequestMappings.QUEUE_JOBS;
import static de.communicode.communikey.controller.RequestMappings.QUEUE_JOB_ABORT;
import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;

/**
 * The service to process {@link EncryptionJob} entities via a {@link EncryptionJobRepository}.
 *
 * @author dvonderbey@communicode.de
 * @since 0.15.0
 */
@Service
public class EncryptionJobService {

    private static final Logger log = LogManager.getLogger();
    private final EncryptionJobRepository encryptionJobRepository;
    private final KeyService keyService;
    private final SimpMessagingTemplate messagingTemplate;
    private final UserEncryptedPasswordRepository userEncryptedPasswordRepository;
    private final UserRepository userRepository;
    private final KeyRepository keyRepository;
    private final KeyCategoryRepository keyCategoryRepository;
    private final UserService userService;

    @Autowired
    public EncryptionJobService(EncryptionJobRepository encryptionJobRepository, KeyService keyService,
                                SimpMessagingTemplate messagingTemplate,
                                UserEncryptedPasswordRepository userEncryptedPasswordRepository,
                                UserRepository userRepository, KeyRepository keyRepository,
                                KeyCategoryRepository keyCategoryRepository,
                                UserService userService) {
        this.encryptionJobRepository = requireNonNull(encryptionJobRepository, "encryptionJobRepository must not be null!");
        this.keyService = requireNonNull(keyService, "keyService must not be null!");
        this.messagingTemplate = requireNonNull(messagingTemplate, "messagingTemplate must not be null!");
        this.userEncryptedPasswordRepository = requireNonNull(userEncryptedPasswordRepository, "userEncryptedPasswordRepository must not be null!");
        this.userRepository = requireNonNull(userRepository, "userRepository must not be null!");
        this.keyRepository = requireNonNull(keyRepository, "keyRepository must not be null!");
        this.keyCategoryRepository = requireNonNull(keyCategoryRepository, "keyCategoryRepository must not be null!");
        this.userService = requireNonNull(userService, "userService must not be null!");
    }

    /**
     * Adds a key category as a child of a parent key category.
     *
     * @param key the key that should be encrypted
     * @param user the user for whom the key should be encrypted
     * @return the created encryption job
     */
    public Optional<EncryptionJob> create(Key key, User user) {
        if (encryptionJobRepository.findByUserAndKey(user, key) == null &&
            userEncryptedPasswordRepository.findOneByOwnerAndKey(user, key) == null &&
            user.getPublicKey() != null) {

            EncryptionJob encryptionJob = new EncryptionJob(key, user);
            encryptionJobRepository.save(encryptionJob);
            advertise(encryptionJob);
            log.debug("Created EncryptionJob for key '{}' and user '{}'.", key.getId(), user.getId());
            return Optional.of(encryptionJob);
        }
        return Optional.empty();
    }

    /**
     * Creates encryption jobs for a key.
     *
     * @param key the key that should be encrypted
     */
    public void createForKey(Key key) {
        keyService.getAccessors(key).stream()
            .filter(user -> user.getPublicKey() != null)
            .forEach(user -> this.create(key, user));
        log.debug("Created all EncryptionJobs for key '{}'.", key.getId());
    }

    /**
     * Creates encryption jobs for the keys of a user in a specific user group.
     *
     * @param userGroup the usergroup of the keys
     * @param user the user for whom the jobs should be created
     */
    public void createForUsergroupForUser(UserGroup userGroup, User user) {
        keyCategoryRepository.findAllByGroupsContains(userGroup).stream()
            .flatMap(keyCategory -> keyRepository.findAllByCategory(keyCategory).stream())
            .forEach(key -> create(key, user));
        log.debug("Created all EncryptionJobs for the keys of user '{}' in usergroup '{}'.", user.getId(), userGroup.getId());
    }

    /**
     * Creates encryption jobs for the keys of a user in a specific user group.
     *
     * @param keyCategory the category of the keys
     * @param userGroup the userGroup of the users for whom the jobs should be created
     */
    public void createForCategoryForUsergroup(KeyCategory keyCategory, UserGroup userGroup) {
        keyRepository.findAllByCategory(keyCategory)
            .forEach(key -> userRepository.findAllByGroupsContains(userGroup).forEach(user -> create(key, user)));
        log.debug("Created all EncryptionJobs for the keys in category '{}' for users in usergroup '{}'.", keyCategory.getId(), userGroup.getId());
    }

    /**
     * Creates encryption jobs for a key for users that have access to a specific category.
     *
     * @param key the key
     * @param keyCategory the category of the usergroups of the users for whom the jobs should be created
     */
    public void createForKeyInCategory(Key key, KeyCategory keyCategory) {
        keyCategory.getGroups().stream()
            .flatMap(userGroup -> userGroup.getUsers().stream())
            .forEach(user -> create(key, user));
        log.debug("Created all EncryptionJobs for the key '{}' for users with access to category '{}'.", key.getId(), keyCategory.getId());
    }

    /**
     * Creates encryption jobs for the keys of a user.
     *
     * @param user the user
     */
    public void createForUser(User user) {
        if (user.getAuthorities().stream().anyMatch(authority -> authority.getName().equals(AuthoritiesConstants.ADMIN))) {
            keyRepository.findAll().forEach(key -> create(key, user));
        } else {
            user.getGroups().forEach(userGroup -> createForUsergroupForUser(userGroup, user));
        }
        log.debug("Created all EncryptionJobs for the user '{}'.", user.getLogin());
    }

    /**
     * Sends out the websocket messages to users that should be able to fulfill the encryption job.
     *
     * @param encryptionJob the encryptionJob that should be advertised
     */
    private void advertiseJobToUser(String userLogin, EncryptionJob encryptionJob) {
        messagingTemplate.convertAndSendToUser(userLogin, QUEUE_JOBS, encryptionJob);
        log.debug("Sent out advertisement for EncryptionJob '{}' and user '{}'.", encryptionJob.getId(), userLogin);
    }

    /**
     * Sends out the websocket messages to users that should be able to fulfill the encryption job.
     *
     * @param encryptionJob the encryptionJob that should be advertised
     */
    private void advertise(EncryptionJob encryptionJob) {
        keyService.getQualifiedEncoders(encryptionJob.getKey())
            .forEach(subscriberInfo -> advertiseJobToUser(subscriberInfo.getUser(), encryptionJob));
        log.debug("Sent out advertisements for EncryptionJob '{}'.", encryptionJob.getId());
    }

    /**
     * Sends the user all the past encryption jobs that he can fulfill.
     *
     * @param userLogin the login of the user whom the jobs should be sent to.
     * @throws UserNotFoundException if the encryption job with the specified token has not been found
     */
    public void advertiseJobsToUser(String userLogin) {
        User user = userService.validate(userLogin);
        encryptionJobRepository.findAll().stream()
            .filter(encryptionJob -> userEncryptedPasswordRepository.findOneByOwnerAndKey(user, encryptionJob.getKey()) != null)
            .forEach(encryptionJob -> advertiseJobToUser(user.getLogin(), encryptionJob));
        log.debug("Sent out the encryption jobs for the user '{}'.", user.getId());
    }

    /**
     * Validates a key with the specified ID.
     *
     * @param token the token of the encryption job to validate
     * @return the encryption job if validated
     * @throws EncryptionJobNotFoundException if the encryption job with the specified token has not been found
     */
    public EncryptionJob validate(String token) {
        return ofNullable(encryptionJobRepository.findByToken(token)).orElseThrow(EncryptionJobNotFoundException::new);
    }

    /**
     * Sends out the websocket messages to users that should be able to fulfill the encryption job.
     *
     * @param jobToken the token of the job
     * @param encryptionJobPayload the encryptionJobPayload that should fulfill an encryptionJob
     * @return the EncryptionJobStatusPayload that is sent to the user via websocket
     */
    public EncryptionJobStatusPayload fulfill(String jobToken, EncryptionJobPayload encryptionJobPayload) {
        EncryptionJob encryptionJob = validate(jobToken);
        Key key = encryptionJob.getKey();
        User user = encryptionJob.getUser();

        UserEncryptedPassword userEncryptedPassword = userEncryptedPasswordRepository.findOneByOwnerAndKey(user, key);

        if(userEncryptedPassword != null) {
            log.debug("Websocket- Updating old userEncryptedPassword");
            userEncryptedPassword.setPassword(encryptionJobPayload.getEncryptedPassword());
            userEncryptedPasswordRepository.save(userEncryptedPassword);
        } else {
            log.debug("Websocket- Creating new userEncryptedPassword");
            UserEncryptedPassword newUserEncryptedPassword = new UserEncryptedPassword();
            newUserEncryptedPassword.setOwner(user);
            newUserEncryptedPassword.setKey(key);
            newUserEncryptedPassword.setPassword(encryptionJobPayload.getEncryptedPassword());
            userEncryptedPasswordRepository.save(newUserEncryptedPassword);
            user.addUserEncryptedPassword(newUserEncryptedPassword);
            userRepository.save(user);
            key.addUserEncryptedPassword(newUserEncryptedPassword);
            keyRepository.save(key);
        }
        encryptionJobRepository.deleteByToken(jobToken);

        messagingTemplate.convertAndSend(QUEUE_JOB_ABORT, new EncryptionJobAbortPayload(jobToken));
        log.debug("Fulfilled encryptionJob with ID '{}' for user '{}' and key '{}'.", encryptionJob.getId(), user.getId(), key.getId());
        return new EncryptionJobStatusPayload("Success");
    }
}
