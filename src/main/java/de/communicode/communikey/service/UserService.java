/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2017
 */
package de.communicode.communikey.service;

import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;
import de.communicode.communikey.domain.Authority;
import de.communicode.communikey.domain.Key;
import de.communicode.communikey.domain.User;
import de.communicode.communikey.exception.ActivationKeyNotFoundException;
import de.communicode.communikey.exception.ResetKeyNotFoundException;
import de.communicode.communikey.exception.UserConflictException;
import de.communicode.communikey.exception.UserNotFoundException;
import de.communicode.communikey.repository.AuthorityRepository;
import de.communicode.communikey.repository.UserRepository;
import de.communicode.communikey.security.AuthoritiesConstants;
import de.communicode.communikey.security.SecurityUtils;
import de.communicode.communikey.service.payload.UserPayload;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The REST API service to process {@link User} via a {@link UserRepository}.
 *
 * @author sgreb@communicode.de
 * @since 0.2.0
 */
@Service
public class UserService {

    private static final Logger log = LogManager.getLogger();
    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;
    private final PasswordEncoder passwordEncoder;

    private final JdbcTokenStore jdbcTokenStore;

    @Autowired
    public UserService(UserRepository userRepository, AuthorityRepository authorityRepository, PasswordEncoder passwordEncoder, JdbcTokenStore jdbcTokenStore) {
        this.userRepository = requireNonNull(userRepository, "userRepository must not be null!");
        this.authorityRepository = requireNonNull(authorityRepository, "authorityRepository must not be null!");
        this.passwordEncoder = requireNonNull(passwordEncoder, "passwordEncoder must not be null!");
        this.jdbcTokenStore = requireNonNull(jdbcTokenStore, "jdbcTokenStore must not be null!");
    }

    /**
     * Activates the user for the specified activation key.
     *
     * @param activationKey the activation key
     * @return the activated user
     * @throws ActivationKeyNotFoundException if the specified activation key has not been found
     */
    public User activate(String activationKey) throws ActivationKeyNotFoundException {
        return ofNullable(userRepository.findOneByActivationKey(activationKey))
            .map(user -> {
                user.setActivated(true);
                user.setActivationKey(null);
                userRepository.save(user);
                log.debug("Activated user '{}' with activation key '{}'", user.getLogin(), activationKey);
                return user;
            }).orElseThrow(() -> new ActivationKeyNotFoundException(activationKey));
    }

    /**
     * Adds the key to the user with the specified login.
     *
     * @param userLogin the login of the user the key will be added to
     * @param key the key to be added
     * @return the updated user
     * @throws UserNotFoundException if the user with the specified login has not been found
     */
    public User addKey(String userLogin, Key key) {
        User user = validate(userLogin);
        user.addCreatedKey(key);
        return userRepository.save(user);
    }

    /**
     * Creates a new user.
     *
     * @param payload the payload for the new user
     * @return the created user
     * @throws UserConflictException if a user with the specified email already exists
     */
    public User create(UserPayload payload) throws UserConflictException {
        validateUniqueEmail(payload.getEmail());

        User user = new User();
        user.setEmail(payload.getEmail().toLowerCase(Locale.ENGLISH));
        user.setLogin(extractLoginFromEmail(payload.getEmail()));
        user.setFirstName(payload.getFirstName());
        user.setLastName(payload.getLastName());
        user.setPassword(passwordEncoder.encode(payload.getPassword()));
        user.setActivationKey(SecurityUtils.generateRandomActivationKey());
        user.setActivated(true);
        Set<Authority> authorities = Sets.newConcurrentHashSet();
        Authority authority = authorityRepository.findOne(AuthoritiesConstants.USER);
        authorities.add(authority);
        user.addAuthorities(authorities);

        userRepository.save(user);
        log.debug("Created new user: {}", user);
        return user;
    }

    /**
     * Deactivates the user with the specified login.
     *
     * @param login the login of the user to deactivate
     * @return the deactivated user
     * @throws UserNotFoundException if the user with the specified login has not been found
     */
    public User deactivate(String login) throws UserNotFoundException {
        return ofNullable(userRepository.findOneByLogin(login))
            .map(user -> {
                user.setActivated(false);
                user.setActivationKey(SecurityUtils.generateRandomActivationKey());
                log.debug("Generated new activation key '{}' for user with login '{}'", user.getActivationKey(), user.getLogin());
                deleteOauth2AccessTokens(login);
                userRepository.save(user);
                log.debug("Deactivated user with login '{}'", login);
                return user;
            }).orElseThrow(() -> new UserNotFoundException(login));
    }

    /**
     * Deletes the user with the specified login.
     *
     * @param login the login of the user to delete
     * @throws UserNotFoundException if the user with the specified login has not been found
     */
    public void delete(String login) throws UserNotFoundException {
        deleteOauth2AccessTokens(login);
        userRepository.delete(ofNullable(validate(login)).orElseThrow(() -> new UserNotFoundException(login)));
        log.debug("Deleted user with login '{}'", login);
    }

    /**
     * Generates a random generated password reset key for a user specified by the email.
     *
     * @param email the email of the user to generate a password reset key for
     * @return the generated reset key
     */
    public Map<String, String> generatePasswordResetKey(String email) {
        return ofNullable(userRepository.findOneByEmail(email))
            .filter(User::isActivated)
            .map(user -> {
                if (Objects.nonNull(user.getResetKey())) {
                    log.debug("Rejected to generate already existing password reset key '{}'", user.getResetKey());
                    throw new UserConflictException("password reset key has already been generated");
                }
                user.setResetKey(SecurityUtils.generateRandomResetKey());
                user.setResetDate(ZonedDateTime.now());
                userRepository.save(user);
                log.debug("Generated reset key '{}' for user with email '{}'", user.getResetKey(), email);
                return ImmutableMap.<String, String>builder().
                    put("resetKey", user.getResetKey()).
                    build();
            }).orElseThrow(() -> new UserNotFoundException(email));
    }

    /**
     * Gets all users.
     *
     * @return a collection of all user
     */
    public Set<User> getAll() {
        return Sets.newConcurrentHashSet(userRepository.findAll());
    }

    @Transactional(readOnly = true)
    public User getWithAuthoritiesByEmail(String email) {
        return userRepository.findOneWithAuthoritiesByEmail(email);
    }

    @Transactional(readOnly = true)
    public User getWithAuthoritiesByLogin(String login) {
        return validateWithAuthorities(login);
    }

    @Transactional(readOnly = true)
    public User getWithAuthorities(Long id) {
        return userRepository.findOneWithAuthoritiesById(id);
    }

    @Transactional(readOnly = true)
    public User getWithAuthorities() {
        return Optional.ofNullable(userRepository.findOneWithAuthoritiesByEmail(SecurityUtils.getCurrentUserLogin())).orElse(null);
    }

    /**
     * Resets the password of a user for the specified reset key.
     *
     * @param newPassword the new password
     * @param resetKey the reset key of a user to reset the password of
     * @return {@code true} if the password has been reset, {@code false} otherwise
     */
    public boolean resetPassword(String newPassword, String resetKey) {
        return ofNullable(userRepository.findOneByResetKey(resetKey))
            .map(user -> {
                user.setPassword(passwordEncoder.encode(newPassword));
                user.setResetKey(null);
                user.setResetDate(null);
                userRepository.save(user);
                log.debug("Reset password with reset key '{}' for user with login '{}'", resetKey, user.getLogin());
                return true;
            }).orElseThrow(() -> new ResetKeyNotFoundException(resetKey));
    }

    /**
     * Updates the user with the specified payload.
     *
     * @param login the login of the user to update
     * @param payload the payload to update the user with
     * @return the updated user
     * @throws UserNotFoundException if the user with the specified login has not been found
     */
    public User update(String login, UserPayload payload) throws UserNotFoundException {
        validateUniqueEmail(payload.getEmail());
        return ofNullable(userRepository.findOneByLogin(login))
            .map(user -> {
                if (!user.getEmail().equals(payload.getEmail())) {
                    user.setEmail(payload.getEmail());
                    user.setLogin(extractLoginFromEmail(payload.getEmail()));
                    deactivate(login);
                    deleteOauth2AccessTokens(login);
                }
                user.setFirstName(payload.getFirstName());
                user.setLastName(payload.getLastName());
                user.setPassword(passwordEncoder.encode(payload.getPassword()));

                userRepository.save(user);
                log.debug("Updated user with login '{}'", user.getLogin());
                return user;
            }).orElseThrow(() -> new UserNotFoundException(login));
    }

    /**
     * Updates the authorities of a user with the specified login.
     *
     * @param login the login of the user to update
     * @param payload the payload to update the user with
     * @return the updated user
     * @throws UserNotFoundException if the user with the specified login has not been found
     */
    public User updateAuthorities(String login, Set<String> payload) throws UserNotFoundException {
        return ofNullable(userRepository.findOneByLogin(login))
            .map(user -> {
                Set<Authority> payloadAuthorities = payload.stream()
                    .map(authorityRepository::findOne)
                    .collect(Collectors.toSet());
                if (!payloadAuthorities.equals(user.getAuthorities())) {
                    user.removeAllAuthorities();
                    payload.stream()
                        .map(authorityRepository::findOne)
                        .forEach(user::addAuthority);
                }
                deleteOauth2AccessTokens(login);
                userRepository.save(user);
                log.debug("Updated authorities of user with login '{}': {}", user.getLogin(), user.getAuthorities());
                return user;
            }).orElseThrow(() -> new UserNotFoundException(login));
    }

    /**
     * Validates the user by the specified login.
     *
     * @param login the login of the user to validate
     * @return the user if validated
     * @throws UserNotFoundException if the user with the specified login has not been found
     */
    public User validate(String login) throws UserNotFoundException {
        return ofNullable(userRepository.findOneByLogin(login)).orElseThrow(() -> new UserNotFoundException(login));
    }

    /**
     * Validates the user by the specified login.
     *
     * @param login the login of the user to validate
     * @return the user if validated
     * @throws UserNotFoundException if the user with the specified login has not been found
     */
    public User validateWithAuthorities(String login) throws UserNotFoundException {
        return ofNullable(userRepository.findOneWithAuthoritiesByLogin(login)).orElseThrow(() -> new UserNotFoundException(login));
    }

    /**
     * Deletes all OAuth2 access tokens of the user with the specified login.
     *
     * @param login the login of the user to delete the OAuth2 access token from
     * @throws UserNotFoundException if the user with the specified login has not been found
     */
    private void deleteOauth2AccessTokens(String login) {
        jdbcTokenStore.findTokensByUserName(login).forEach(accessToken -> {
            jdbcTokenStore.removeAccessToken(accessToken);
            log.debug("Removed OAuth2 access token '{}' of user with login '{}'", accessToken.getValue(), login);
        });
    }

    /**
     * Extracts the login of a user from the specified email.
     *
     * @param email the email to extract the login of
     * @return the extracted login
     */
    private String extractLoginFromEmail(String email) {
        return email.substring(0, email.indexOf('@')).toLowerCase(Locale.ENGLISH);
    }

    /**
     * Validates that the specified email is unique.
     *
     * @param email the email to validate
     * @throws UserConflictException if the specified email is not unique
     */
    private void validateUniqueEmail(String email) throws UserConflictException {
        if (ofNullable(userRepository.findOneByEmail(email)).isPresent()) {
            throw new UserConflictException("email '" + email + "' already exists");
        }
    }
}
