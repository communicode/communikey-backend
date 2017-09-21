/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2017
 */
package de.communicode.communikey.service;

import de.communicode.communikey.domain.EncryptionJob;
import de.communicode.communikey.domain.Key;
import de.communicode.communikey.domain.User;
import de.communicode.communikey.domain.UserEncryptedPassword;
import de.communicode.communikey.exception.EncryptionJobNotFoundException;
import de.communicode.communikey.repository.EncryptionJobRepository;
import de.communicode.communikey.repository.KeyRepository;
import de.communicode.communikey.repository.UserEncryptedPasswordRepository;
import de.communicode.communikey.repository.UserRepository;
import de.communicode.communikey.service.payload.EncryptionJobAbortPayload;
import de.communicode.communikey.service.payload.EncryptionJobPayload;
import de.communicode.communikey.service.payload.EncryptionJobStatusPayload;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

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

    @Autowired
    public EncryptionJobService(EncryptionJobRepository encryptionJobRepository, KeyService keyService,
                                SimpMessagingTemplate messagingTemplate,
                                UserEncryptedPasswordRepository userEncryptedPasswordRepository,
                                UserRepository userRepository, KeyRepository keyRepository) {
        this.encryptionJobRepository = requireNonNull(encryptionJobRepository, "encryptionJobRepository must not be null!");
        this.keyService = requireNonNull(keyService, "keyService must not be null!");
        this.messagingTemplate = requireNonNull(messagingTemplate, "messagingTemplate must not be null!");
        this.userEncryptedPasswordRepository = requireNonNull(userEncryptedPasswordRepository, "userEncryptedPasswordRepository must not be null!");
        this.userRepository = requireNonNull(userRepository, "userRepository must not be null!");
        this.keyRepository = requireNonNull(keyRepository, "keyRepository must not be null!");
    }

    /**
     * Adds a key category as a child of a parent key category.
     *
     * @param key the key that should be encrypted
     * @param user the user for whom the key should be encrypted
     * @return the created encryption job
     */
    public EncryptionJob create(Key key, User user) {
        EncryptionJob encryptionJob = new EncryptionJob(key, user);
        encryptionJobRepository.save(encryptionJob);
        advertise(encryptionJob);
        log.debug("Created EncryptionJob for key '{}' and user '{}'.", key.getId(), user.getId());
        return encryptionJob;
    }

    /**
     * Creates encryption jobs for a key.
     *
     * @param key the key that should be encrypted
     */
    public void createForKey(Key key) {
        keyService.getAccessors(key)
            .stream()
            .filter(user -> userEncryptedPasswordRepository.findOneByOwnerAndKey(user, key) != null)
            .filter(user -> encryptionJobRepository.findByUserAndKey(user, key) == null)
            .forEach(user -> {
                EncryptionJob encryptionJob = new EncryptionJob(key, user);
                encryptionJobRepository.save(encryptionJob);
                advertise(encryptionJob);
            });
        log.debug("Created all EncryptionJobs for key '{}'.", key.getId());
    }

    /**
     * Sends out the websocket messages to users that should be able to fulfill the encryption job.
     *
     * @param encryptionJob the encryptionJob that should be advertised
     */
    private void advertise(EncryptionJob encryptionJob) {
        keyService.getQualifiedEncoders(encryptionJob.getKey())
            .forEach(subscriberInfo -> {
                messagingTemplate.convertAndSendToUser(subscriberInfo.getUser(), "/queue/encryption/jobs", encryptionJob);
                log.debug("Sent out advertisement for EncryptionJob '{}' and user '{}'.", encryptionJob.getId(), subscriberInfo.getUser());
            });
        log.debug("Sent out advertisements for EncryptionJob '{}'.", encryptionJob.getId());
    }

    /**
     * Validates a key with the specified ID.
     *
     * @param token the token of the encryption job to validate
     * @return the encryption job if validated
     * @throws EncryptionJobNotFoundException if the encryption job with the specified token has not been found
     */
    public EncryptionJob validate(String token) throws EncryptionJobNotFoundException {
        return ofNullable(encryptionJobRepository.findByToken(token)).orElseThrow(EncryptionJobNotFoundException::new);
    }

    /**
     * Sends out the websocket messages to users that should be able to fulfill the encryption job.
     *
     * @param encryptionJobPayload the encryptionJobPayload that should fulfill an encryptionJob
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
