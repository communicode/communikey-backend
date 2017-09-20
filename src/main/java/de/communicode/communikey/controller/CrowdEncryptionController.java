package de.communicode.communikey.controller;

import de.communicode.communikey.domain.User;
import de.communicode.communikey.repository.UserRepository;
import de.communicode.communikey.service.EncryptionJobService;
import de.communicode.communikey.service.payload.EncryptionJobPayload;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import javax.validation.Valid;

import static de.communicode.communikey.security.SecurityUtils.getCurrentUserLogin;
import static java.util.Objects.requireNonNull;

@Controller
public class CrowdEncryptionController {

    private static final Logger log = LogManager.getLogger();
    private final UserRepository userRepository;
    private final EncryptionJobService encryptionJobService;

    @Autowired
    public CrowdEncryptionController(UserRepository userRepository, EncryptionJobService encryptionJobService) {
        this.userRepository = requireNonNull(userRepository, "userRepository must not be null!");
        this.encryptionJobService = requireNonNull(encryptionJobService, "encryptionJobService must not be null!");
    }

    @MessageMapping("/encrypt")
//    @SendToUser("/queue/reply")
    public void encrypt(@Payload EncryptionJobPayload payload) {
        encryptionJobService.fulfill(payload);
//        return "Hello '" + userLogin + "'. Your text: '" + greeting +"'";
    }

}
