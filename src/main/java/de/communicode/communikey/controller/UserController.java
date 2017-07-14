/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2017
 */
package de.communicode.communikey.controller;

import static de.communicode.communikey.controller.PathVariables.USER_ACTIVATION_TOKEN;
import static de.communicode.communikey.controller.PathVariables.USER_EMAIL;
import static de.communicode.communikey.controller.PathVariables.USER_LOGIN;
import static de.communicode.communikey.controller.RequestMappings.USERS;
import static de.communicode.communikey.controller.RequestMappings.USERS_ACTIVATE;
import static de.communicode.communikey.controller.RequestMappings.USERS_DEACTIVATE;
import static de.communicode.communikey.controller.RequestMappings.USERS_LOGIN;
import static de.communicode.communikey.controller.RequestMappings.USERS_REGISTER;
import static de.communicode.communikey.controller.RequestMappings.USERS_PASSWORD_RESET;
import static de.communicode.communikey.controller.RequestMappings.USER_AUTHORITIES;
import static java.util.Objects.requireNonNull;

import de.communicode.communikey.domain.User;
import de.communicode.communikey.security.AuthoritiesConstants;
import de.communicode.communikey.service.UserService;
import de.communicode.communikey.service.payload.UserCreationPayload;
import de.communicode.communikey.service.payload.UserPasswordResetPayload;
import de.communicode.communikey.service.payload.UserPayload;
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

import java.util.Map;
import java.util.Set;

/**
 * The REST API controller to process {@link User}.
 *
 * <p>Mapped to the {@value RequestMappings#USERS} endpoint.
 *
 * @author sgreb@communicode.de
 * @since 0.2.0
 */
@RestController
@RequestMapping(USERS)
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = requireNonNull(userService, "userService must not be null!");
    }

    /**
     * Activates the user with the specified activation token.
     *
     * <p>This endpoint is mapped to "{@value RequestMappings#USERS}{@value RequestMappings#USERS_ACTIVATE}".
     *
     * <p>Required parameter:
     * <ul>
     *   <li>{@value PathVariables#USER_ACTIVATION_TOKEN}</li>
     * </ul>
     *
     * @param activationToken the activation token of the user to activate
     * @return the response entity
     */
    @GetMapping(value = USERS_ACTIVATE)
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<User> activate(@RequestParam(value = USER_ACTIVATION_TOKEN) String activationToken) {
        return new ResponseEntity<>(userService.activate(activationToken), HttpStatus.OK);
    }

    /**
     * Adds a authority to a user with the specified login.
     *
     * <p>This endpoint is mapped to "{@value RequestMappings#USERS}{@value RequestMappings#USER_AUTHORITIES}".
     *
     * @param login the login of the user to add the authority to
     * @param authorityName the name of the authority to be added
     * @return the updated user as response entity
     * @since 0.11.0
     */
    @GetMapping(value = USER_AUTHORITIES)
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<User> addUserGroup(@PathVariable (value = USER_LOGIN) String login, @RequestParam String authorityName) {
        return new ResponseEntity<>(userService.addAuthority(login, authorityName), HttpStatus.OK);
    }

    /**
     * Deactivates the user with the specified login.
     *
     * <p>This endpoint is mapped to "{@value RequestMappings#USERS}{@value RequestMappings#USERS_DEACTIVATE}".
     *
     * <p>Required parameter:
     * <ul>
     *   <li>{@value RequestMappings#USERS_LOGIN}</li>
     * </ul>
     *
     * @param login the login of the user to deactivate
     * @return the response entity
     */
    @GetMapping(value = USERS_DEACTIVATE)
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<User> deactivate(@RequestParam(value = USER_LOGIN) String login) {
        return new ResponseEntity<>(userService.deactivate(login), HttpStatus.OK);
    }

    /**
     * Creates a new user.
     *
     * <p>This endpoint is mapped to "{@value RequestMappings#USERS}{@value RequestMappings#USERS_REGISTER}".
     *
     * @param payload the payload for the new user
     * @return the created user as response entity
     */
    @PostMapping(value = USERS_REGISTER)
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<User> create(@Valid @RequestBody UserCreationPayload payload) {
        return new ResponseEntity<>(userService.create(payload), HttpStatus.CREATED);
    }

    /**
     * Deletes the user with the specified login.
     *
     * <p>This endpoint is mapped to "{@value RequestMappings#USERS}{@value RequestMappings#USERS_LOGIN}".
     *
     * @param login the login of the user to delete
     * @return the response entity
     */
    @DeleteMapping(value = USERS_LOGIN)
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<Void> delete(@PathVariable String login) {
        userService.delete(login);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Gets the user with the specified login.
     *
     * <p>This endpoint is mapped to "{@value RequestMappings#USERS}{@value RequestMappings#USERS_LOGIN}".
     *
     * @param login the login of the user to get
     * @return the user as response entity
     */
    @GetMapping(value = USERS_LOGIN)
    @Secured(AuthoritiesConstants.ADMIN)
    ResponseEntity<User> get(@PathVariable String login) {
        return new ResponseEntity<>(userService.getWithAuthoritiesByLogin(login), HttpStatus.OK);
    }

    /**
     * Gets all users.
     *
     * @return a collection of users as response entity
     */
    @GetMapping
    @Secured(AuthoritiesConstants.ADMIN)
    ResponseEntity<Set<User>> getAll() {
        return new ResponseEntity<>(userService.getAll(), HttpStatus.OK);
    }

    /**
     * Gets a random generated user password reset token for the specified email.
     *
     * <p>This endpoint is mapped to "{@value RequestMappings#USERS}{@value RequestMappings#USERS_PASSWORD_RESET}".
     *
     * <p>Required parameter:
     * <ul>
     *   <li>{@code email}</li>
     * </ul>
     *
     * @param email the email of the user to generate a password reset token for
     * @return the random generated reset token
     */
    @GetMapping(value = USERS_PASSWORD_RESET)
    public ResponseEntity<Map<String, String>> getPasswordResetToken(@RequestParam(value = USER_EMAIL) String email) {
        return new ResponseEntity<>(userService.generatePasswordResetToken(email), HttpStatus.OK);
    }

    /**
     * Removes a authority from a user with the specified login.
     *
     * <p>This endpoint is mapped to "{@value RequestMappings#USERS}{@value RequestMappings#USER_AUTHORITIES}".
     *
     * @param login the login of the user to remove the authority from
     * @param authorityName the name of the authority to be removed
     * @return the updated user as response entity
     * @since 0.11.0
     */
    @DeleteMapping(value = USER_AUTHORITIES)
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<User> removeAuthority(@PathVariable (value = USER_LOGIN) String login, @RequestParam String authorityName) {
        return new ResponseEntity<>(userService.removeAuthority(login, authorityName), HttpStatus.OK);
    }

    /**
     * Resets the password of a user.
     *
     * <p>This endpoint is mapped to "{@value RequestMappings#USERS}{@value RequestMappings#USERS_PASSWORD_RESET}".
     *
     * @param payload the payload of the user
     * @return the random generated reset token
     */
    @PostMapping(value = USERS_PASSWORD_RESET)
    public ResponseEntity resetPassword(@Valid @RequestBody UserPasswordResetPayload payload) {
        userService.resetPassword(payload.getPassword(), payload.getResetToken());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Updates a user with the specified payload.
     *
     * <p>This endpoint is mapped to "{@value RequestMappings#USERS}{@value RequestMappings#USERS_LOGIN}".
     *
     * @param login the login of the user to update
     * @param payload the user payload to update the user entity with
     * @return the updated user as response entity
     */
    @PutMapping(value = USERS_LOGIN)
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<User> update(@PathVariable String login, @Valid @RequestBody UserPayload payload) {
        return new ResponseEntity<>(userService.update(login, payload), HttpStatus.OK);
    }

    /**
     * Updates the authorities of a user with the specified login.
     *
     * <p>This endpoint is mapped to "{@value RequestMappings#USERS}{@value RequestMappings#USER_AUTHORITIES}".
     *
     * @param login the login of the user to update
     * @param payload the payload as collection of authority names to update the user entity with
     * @return the updated user as response entity
     */
    @PutMapping(value = USER_AUTHORITIES)
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<User> updateAuthorities(@PathVariable String login, @RequestBody Set<String> payload) {
        return new ResponseEntity<>(userService.updateAuthorities(login, payload), HttpStatus.OK);
    }
}