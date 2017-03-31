/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2017
 */
package de.communicode.communikey.controller;

import static de.communicode.communikey.controller.PathVariables.USER_LOGIN;
import static de.communicode.communikey.controller.RequestMappings.USER_GROUPS;
import static de.communicode.communikey.controller.RequestMappings.USER_GROUPS_NAME;
import static de.communicode.communikey.controller.RequestMappings.USER_GROUPS_USERS;
import static java.util.Objects.requireNonNull;

import de.communicode.communikey.domain.UserGroup;
import de.communicode.communikey.security.AuthoritiesConstants;
import de.communicode.communikey.service.UserGroupService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import java.util.Set;

/**
 * The REST API controller to process {@link UserGroup}s.
 *
 * <p>Mapped to the {@value RequestMappings#USER_GROUPS} endpoint.
 *
 * @author sgreb@communicode.de
 * @since 0.2.0
 */
@RestController
@RequestMapping(USER_GROUPS)
public class UserGroupController {

    private final UserGroupService userGroupService;

    @Autowired
    public UserGroupController(UserGroupService userGroupService) {
        this.userGroupService = requireNonNull(userGroupService, "userGroupService must not be null!");
    }

    /**
     * Adds a user to a user group.
     *
     * <p>This endpoint is mapped to "{@value RequestMappings#USER_GROUPS}{@value RequestMappings#USER_GROUPS_NAME}{@value RequestMappings#USER_GROUPS_USERS}".
     * <p>Required parameter:
     * <ul>
     *   <li>{@value PathVariables#USER_LOGIN}</li>
     * </ul>
     *
     * @param userGroupName the name of the user group to add the user to
     * @param login the login of the user to be added
     * @return the user group as response entity
     */
    @GetMapping(value = USER_GROUPS_USERS)
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<UserGroup> addUser(@PathVariable String userGroupName, @RequestParam(name = USER_LOGIN) String login) {
        return new ResponseEntity<>(userGroupService.addUser(userGroupName, login), HttpStatus.OK);
    }

    /**
     * Creates a new user group.
     *
     * <p>This endpoint is mapped to "{@value RequestMappings#USER_GROUPS}".
     *
     * @param payload the payload for the new user group
     * @return the user group as response entity
     */
    @PostMapping
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<UserGroup> create(@Valid @RequestBody UserGroup payload) {
        return new ResponseEntity<>(userGroupService.create(payload), HttpStatus.CREATED);
    }


    /**
     * Deletes the user group with the specified ID.
     *
     * <p>This endpoint is mapped to "{@value RequestMappings#USER_GROUPS}{@value RequestMappings#USER_GROUPS_NAME}".
     *
     * @param userGroupName the name of the user group to delete
     * @return the user group as response entity
     */
    @DeleteMapping(value = USER_GROUPS_NAME)
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<UserGroup> delete(@PathVariable String userGroupName) {
        userGroupService.delete(userGroupName);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Deletes all user groups.
     * <p>This endpoint is mapped to "{@value RequestMappings#USER_GROUPS}".
     *
     * @return a empty response entity
     * @since 0.3.0
     */
    @DeleteMapping
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<UserGroup> deleteAll() {
        userGroupService.deleteAll();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Gets the user group with the specified name.
     *
     * <p>This endpoint is mapped to "{@value RequestMappings#USER_GROUPS}{@value RequestMappings#USER_GROUPS_NAME}".
     *
     * @param userGroupName the name of the user group to get
     * @return the user group as response entity
     */
    @GetMapping(value = USER_GROUPS_NAME)
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<UserGroup> get(@PathVariable String userGroupName) {
        return new ResponseEntity<>(userGroupService.getByName(userGroupName), HttpStatus.OK);
    }

    /**
     * Gets all user groups.
     *
     * @return a collection of user groups as response entity
     */
    @GetMapping
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<Set<UserGroup>> getAll() {
        return new ResponseEntity<>(userGroupService.getAll(), HttpStatus.OK);
    }

    /**
     * Removes a user from a user group.
     *
     * <p>This endpoint is mapped to "{@value RequestMappings#USER_GROUPS}{@value RequestMappings#USER_GROUPS_USERS}".
     * <p>Required parameter:
     * <ul>
     *   <li>{@value PathVariables#USER_LOGIN}</li>
     * </ul>
     *
     * @param userGroupName the name of the user group to remove the user from
     * @param login the login of the user to be removed
     * @return the user group as response entity
     */
    @DeleteMapping(value = USER_GROUPS_USERS)
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<UserGroup> removeUser(@PathVariable String userGroupName, @RequestParam(name = USER_LOGIN) String login) {
        return new ResponseEntity<>(userGroupService.removeUser(userGroupName, login), HttpStatus.OK);
    }

    /**
     * Updates a user group with the specified payload.
     *
     * <p>This endpoint is mapped to "{@value RequestMappings#USER_GROUPS}{@value RequestMappings#USER_GROUPS_NAME}".
     *
     * @param userGroupName the name of the user group to update
     * @param payload the user group payload to update the user group with
     * @return the updated user group as response entity
     */
    @PutMapping(value = USER_GROUPS_NAME)
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<UserGroup> update(@PathVariable String userGroupName, @Valid @RequestBody UserGroup payload) {
        return new ResponseEntity<>(userGroupService.update(userGroupName, payload), HttpStatus.OK);
    }
}