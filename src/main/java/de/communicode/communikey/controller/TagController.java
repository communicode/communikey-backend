/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2017
 */
package de.communicode.communikey.controller;

import de.communicode.communikey.domain.Tag;
import de.communicode.communikey.exception.HashidNotValidException;
import de.communicode.communikey.exception.TagNotFoundException;
import de.communicode.communikey.security.AuthoritiesConstants;
import de.communicode.communikey.service.TagService;
import de.communicode.communikey.service.payload.TagPayload;
import org.hashids.Hashids;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Set;

import static de.communicode.communikey.controller.PathVariables.TAG_ID;
import static de.communicode.communikey.controller.RequestMappings.TAGS;
import static de.communicode.communikey.controller.RequestMappings.TAG_HASHID;
import static java.util.Objects.requireNonNull;

/**
 * The REST API controller to process {@link Tag} entities.
 *
 * <p>Mapped to the "{@value RequestMappings#TAGS}" endpoint.
 *
 * @author dvonderbey@communicode.de
 * @since 0.18.0
 */
@RestController
@RequestMapping(TAGS)
public class TagController {
    private final TagService tagService;
    private final Hashids hashids;

    @Autowired
    public TagController(TagService tagService, Hashids hashids) {
        this.tagService = requireNonNull(tagService, "tagService must not be null!");
        this.hashids = requireNonNull(hashids, "hashids must not be null!");
    }

    /**
     * Creates a new tag with the specified payload.
     *
     * <p>This endpoint is mapped to "{@value RequestMappings#TAGS}{@value RequestMappings#TAG_HASHID}".
     *
     * @param payload the tag request payload
     * @return the tag as response entity
     * @since 0.18.0
     */
    @PostMapping
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<Tag> create(@Valid @RequestBody TagPayload payload) {
        return new ResponseEntity<>(tagService.create(payload), HttpStatus.CREATED);
    }

    /**
     * Deletes the tag with the specified Hashid.
     *
     * <p>This endpoint is mapped to "{@value RequestMappings#TAGS}{@value RequestMappings#TAG_HASHID}".
     *
     * @param tagHashid the Hashid of the tag to delete
     * @return a empty response entity
     */
    @DeleteMapping(value = TAG_HASHID)
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<Void> delete(@PathVariable(name = TAG_ID) String tagHashid) {
        tagService.delete(decodeSingleValueHashid(tagHashid));
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Deletes all keys.
     *
     * <p>This endpoint is mapped to "{@value RequestMappings#KEYS}".
     *
     * @return a empty response entity
     */
    @DeleteMapping
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<Void> deleteAll() {
        tagService.deleteAll();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Gets the tag with the specified Hashid.
     *
     * <p>This endpoint is mapped to "{@value RequestMappings#TAGS}{@value RequestMappings#TAG_HASHID}".
     *
     * @param tagHashid the Hashid of the tag entity to get
     * @return the tag as response entity
     */
    @GetMapping(value = TAG_HASHID)
    @Secured(AuthoritiesConstants.USER)
    public ResponseEntity get(@PathVariable(name = TAG_ID) String tagHashid) {
        return new ResponseEntity<>(tagService.get(decodeSingleValueHashid(tagHashid)), HttpStatus.OK);
    }

    /**
     * Gets all tags.
     *
     * <p>This endpoint is mapped to "{@value RequestMappings#TAGS}".
     *
     * @return a collection of tags as response entity
     */
    @GetMapping
    @Secured(AuthoritiesConstants.USER)
    public ResponseEntity<Set<Tag>> getAll() {
        return new ResponseEntity<>(tagService.getAll(), HttpStatus.OK);
    }

    /**
     * Updates a tag with the specified payload.
     *
     * <p>This endpoint is mapped to "{@value RequestMappings#TAGS}{@value RequestMappings#TAG_HASHID}".
     *
     * @param tagHashid the Hashid of the tag entity to update
     * @param payload the tag request payload to update the tag entity with
     * @return the updated tag as response entity
     */
    @PutMapping(value = TAG_HASHID)
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<Tag> update(@PathVariable(name = TAG_ID) String tagHashid, @Valid @RequestBody TagPayload payload) {
        return new ResponseEntity<>(tagService.update(decodeSingleValueHashid(tagHashid), payload), HttpStatus.OK);
    }

    /**
     * Decodes the specified Hashid.
     *
     * @param hashid the Hashid of the tag to decode
     * @return the decoded Hashid if valid
     * @throws TagNotFoundException if the Hashid is invalid and the tag has not been found
     * @since 0.12.0
     */
    private Long decodeSingleValueHashid(String hashid) throws HashidNotValidException {
        long[] decodedHashid = hashids.decode(hashid);
        if (decodedHashid.length == 0) {
            throw new HashidNotValidException();
        }
        return decodedHashid[0];
    }
}
