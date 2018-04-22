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
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional
    public EncryptionJobStatusPayload fulfill(@DestinationVariable(value = JOB_TOKEN) String jobToken, @Payload @Valid EncryptionJobPayload payload) {
        return encryptionJobService.fulfill(jobToken, payload);
    }
}
