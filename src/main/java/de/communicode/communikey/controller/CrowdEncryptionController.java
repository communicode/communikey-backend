package de.communicode.communikey.controller;

import de.communicode.communikey.security.SecurityUtils;
import de.communicode.communikey.service.EncryptionJobService;
import de.communicode.communikey.service.payload.EncryptionJobPayload;
import de.communicode.communikey.service.payload.EncryptionJobStatusPayload;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

import javax.validation.Valid;

import static de.communicode.communikey.controller.RequestMappings.QUEUE_JOBS;
import static de.communicode.communikey.controller.PathVariables.JOB_TOKEN;
import static de.communicode.communikey.controller.RequestMappings.JOBS_FULFILL;
import static de.communicode.communikey.controller.RequestMappings.QUEUE_REPLY;
import static java.util.Objects.requireNonNull;

@Controller
public class CrowdEncryptionController {

    private static final Logger log = LogManager.getLogger();
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
     */
    @MessageMapping(value = JOBS_FULFILL)
    @SendToUser(value = QUEUE_REPLY)
    public EncryptionJobStatusPayload fulfill(@DestinationVariable(value = JOB_TOKEN) String jobToken, @Payload @Valid EncryptionJobPayload payload) {
        return encryptionJobService.fulfill(jobToken, payload);
    }
}
