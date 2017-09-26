/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2017
 */
package de.communicode.communikey.controller;

import de.communicode.communikey.domain.EncryptionJob;
import de.communicode.communikey.security.SecurityUtils;
import de.communicode.communikey.service.EncryptionJobService;
import de.communicode.communikey.service.payload.EncryptionJobPayload;
import de.communicode.communikey.service.payload.EncryptionJobStatusPayload;

import javax.validation.Valid;
import static java.util.Objects.requireNonNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

import static de.communicode.communikey.controller.RequestMappings.JOBS_FULFILL;
import static de.communicode.communikey.controller.PathVariables.JOB_TOKEN;
import static de.communicode.communikey.controller.RequestMappings.QUEUE_JOBS;
import static de.communicode.communikey.controller.RequestMappings.QUEUE_REPLY;

/**
 * The WSS controller to process {@link EncryptionJob} services.
 *
 * @author dvonderbey@communicode.de
 * @since 0.15.0
 */
@Controller
public class CrowdEncryptionController {

    private final EncryptionJobService encryptionJobService;

    @Autowired
    public CrowdEncryptionController(EncryptionJobService encryptionJobService) {
        this.encryptionJobService = requireNonNull(encryptionJobService, "encryptionJobService must not be null!");
    }

    /**
     * Sends the user all his encryption jobs when subscribing.
     *
     * <p>This endpoint is mapped to "{@value RequestMappings#QUEUE_JOBS}".
     *
     */
    @SubscribeMapping(value = QUEUE_JOBS)
    public void subscribe() {
        encryptionJobService.advertiseJobsToUser(SecurityUtils.getCurrentUserLogin());
    }

    /**
     * Handles a job fulfillment from the client to create a user encrypted password.
     *
     * <p>This endpoint is mapped to "{@value RequestMappings#JOBS_FULFILL}".
     *
     * @param jobToken the token of the encryption job
     * @param payload the fulfillment payload
     * @return the encryption job fulfillment result
     *
     */
    @MessageMapping(value = JOBS_FULFILL)
    @SendToUser(value = QUEUE_REPLY)
    public EncryptionJobStatusPayload fulfill(@DestinationVariable(value = JOB_TOKEN) String jobToken, @Payload @Valid EncryptionJobPayload payload) {
        return encryptionJobService.fulfill(jobToken, payload);
    }
}
