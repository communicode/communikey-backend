package de.communicode.communikey.controller;

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
import org.springframework.stereotype.Controller;

import javax.validation.Valid;

import static de.communicode.communikey.controller.PathVariables.JOB_TOKEN;
import static de.communicode.communikey.controller.RequestMappings.FULFILL;
import static de.communicode.communikey.controller.RequestMappings.JOBS;
import static java.util.Objects.requireNonNull;

@Controller
@MessageMapping(JOBS)
public class CrowdEncryptionController {

    private static final Logger log = LogManager.getLogger();
    private final EncryptionJobService encryptionJobService;

    @Autowired
    public CrowdEncryptionController(EncryptionJobService encryptionJobService) {
        this.encryptionJobService = requireNonNull(encryptionJobService, "encryptionJobService must not be null!");
    }

    @MessageMapping(value = FULFILL)
    @SendToUser(value = "/queue/reply")
    public EncryptionJobStatusPayload fulfill(@DestinationVariable(value = JOB_TOKEN) String jobToken, @Payload @Valid EncryptionJobPayload payload) {
        return encryptionJobService.fulfill(jobToken, payload);
    }
}
