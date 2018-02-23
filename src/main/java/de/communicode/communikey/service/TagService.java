/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2017
 */
package de.communicode.communikey.service;

import de.communicode.communikey.domain.Tag;
import de.communicode.communikey.domain.User;
import de.communicode.communikey.exception.HashidNotValidException;
import de.communicode.communikey.exception.KeyNotFoundException;
import de.communicode.communikey.exception.TagNotFoundException;
import de.communicode.communikey.repository.TagRepository;
import de.communicode.communikey.service.payload.TagPayload;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hashids.Hashids;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

import static de.communicode.communikey.controller.RequestMappings.QUEUE_UPDATES_TAGS;
import static de.communicode.communikey.controller.RequestMappings.QUEUE_UPDATES_TAGS_DELETE;
import static de.communicode.communikey.security.SecurityUtils.getCurrentUserLogin;
import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;

/**
 * The REST API service to process {@link Tag} entities via a {@link TagRepository}.
 *
 * @author dvonderbey@communicode.de
 * @since 0.18.0
 */
@Service
public class TagService {

    private static final Logger log = LogManager.getLogger();
    private final TagRepository tagRepository;
    private final UserService userService;
    private final Hashids hashids;
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public TagService(TagRepository tagRepository,
                      UserService userService,
                      Hashids hashids,
                      SimpMessagingTemplate messagingTemplate) {
        this.tagRepository = requireNonNull(tagRepository, "tagRepository must not be null!");
        this.userService = requireNonNull(userService, "userService must not be null!");
        this.hashids = requireNonNull(hashids, "hashids must not be null!");
        this.messagingTemplate = requireNonNull(messagingTemplate, "messagingTemplate must not be null!");
    }

    /**
     * Creates a new key.
     *
     * @param payload the key payload
     * @return the created key
     */
    public Tag create(TagPayload payload) {
        Tag tag = new Tag();
        String userLogin = getCurrentUserLogin();
        User user = userService.validate(userLogin);
        tag.setName(payload.getName());
        tag.setColor(payload.getColor());
        tag.setCreator(user);

        Tag persistedTag = tagRepository.save(tag);
        persistedTag.setHashid(hashids.encode(persistedTag.getId()));
        tagRepository.save(tag);

        sendUpdates(tag);

        log.debug("Created tag '{}' with ID '{}'", persistedTag.getName(), persistedTag.getId());
        return persistedTag;
    }

    /**
     * Updates a tag with the specified payload.
     *
     * @param tagId the ID of the tag to update
     * @param payload the payload to update the tag with
     * @return the updated tag
     */
    public Tag update(Long tagId, TagPayload payload) {
        Tag tag = validate(tagId);
        tag.setName(payload.getName());
        tag.setColor(payload.getColor());
        final Tag savedTag = tagRepository.save(tag);
        sendUpdates(tag);
        log.debug("Updated tag with ID '{}'", savedTag.getId());
        return savedTag;
    }

    /**
     * Deletes the tag with the specified ID.
     *
     * @param tagId the ID of the key to delete
     * @throws TagNotFoundException if the tag with the specified ID has not been found
     */
    public void delete(Long tagId) {
        Tag tag = validate(tagId);
        tagRepository.delete(tag);
        sendRemovalUpdates(tag);
        log.debug("Deleted tag with ID '{}'", tagId);
    }

    /**
     * Deletes all tags.
     */
    public void deleteAll() {
        tagRepository.deleteAll();
        log.debug("Deleted all tags");
    }

    /**
     * Gets the tag with the specified ID.
     *
     * @param tagId the ID of the tag to get
     * @return the tag
     * @throws TagNotFoundException if the key with the specified ID has not been found
     */
    public Tag get(Long tagId) {
        return validate(tagId);
    }

    /**
     * Gets all tags
     *
     * @return a collection of tags
     */
    public Set<Tag> getAll() {
        return new HashSet<>(tagRepository.findAll());
    }

    /**
     * Validates a tag with the specified ID.
     *
     * @param tagId the ID of the tag to validate
     * @return the tag if validated
     * @throws KeyNotFoundException if the key with the specified ID has not been found
     */
    private Tag validate(Long tagId) {
        return ofNullable(tagRepository.findOne(tagId)).orElseThrow(TagNotFoundException::new);
    }

    /**
     * Decodes the specified Hashid.
     *
     * @param hashid the Hashid of the key to decode
     * @return the decoded Hashid if valid
     * @throws KeyNotFoundException if the Hashid is invalid and the key has not been found
     * @since 0.13.0
     */
    private Long decodeSingleValueHashid(String hashid) {
        long[] decodedHashid = hashids.decode(hashid);
        if (decodedHashid.length == 0) {
            throw new HashidNotValidException();
        }
        return decodedHashid[0];
    }


    /**
     * Sends out websocket messages to users for live updates.
     *
     * @param tag the tag that was updated
     */
    private void sendUpdates(Tag tag) {
        messagingTemplate.convertAndSend(QUEUE_UPDATES_TAGS, tag);
        log.debug("Sent out updates for tag '{}'.", tag.getId());
    }

    /**
     * Sends out websocket messages to users for live removals.
     *
     * @param tag the tag that was removed
     */
    private void sendRemovalUpdates(Tag tag) {
        messagingTemplate.convertAndSend(QUEUE_UPDATES_TAGS_DELETE, tag);
        log.debug("Sent out removal updates for tag '{}'.", tag.getId());
    }
}
