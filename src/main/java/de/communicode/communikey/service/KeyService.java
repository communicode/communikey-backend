/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2017
 */
package de.communicode.communikey.service;

import static de.communicode.communikey.security.AuthoritiesConstants.ADMIN;
import static de.communicode.communikey.security.SecurityUtils.getCurrentUserLogin;
import static de.communicode.communikey.security.SecurityUtils.isCurrentUserInRole;
import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toSet;

import de.communicode.communikey.domain.*;
import de.communicode.communikey.exception.HashidNotValidException;
import de.communicode.communikey.repository.UserEncryptedPasswordRepository;
import de.communicode.communikey.security.SecurityUtils;
import de.communicode.communikey.service.payload.KeyPayload;
import de.communicode.communikey.exception.KeyNotFoundException;
import de.communicode.communikey.repository.KeyRepository;
import de.communicode.communikey.service.payload.KeyPayloadEncryptedPasswords;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hashids.Hashids;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * The REST API service to process {@link Key} entities via a {@link KeyRepository}.
 *
 * @author sgreb@communicode.de
 * @since 0.1.0
 */
@Service
public class KeyService {

    private static final Logger log = LogManager.getLogger();
    private final KeyRepository keyRepository;
    private final UserEncryptedPasswordRepository userEncryptedPasswordRepository;
    private final KeyCategoryService keyCategoryService;
    private final UserService userService;
    private final Hashids hashids;

    @Autowired
    public KeyService(KeyRepository keyRepository, @Lazy KeyCategoryService keyCategoryService, UserService userRestService, Hashids hashids,
                      UserEncryptedPasswordRepository userEncryptedPasswordRepository) {
        this.keyRepository = requireNonNull(keyRepository, "keyRepository must not be null!");
        this.userEncryptedPasswordRepository = requireNonNull(userEncryptedPasswordRepository, "userEncryptedPasswordRepository must not be null!");
        this.keyCategoryService = requireNonNull(keyCategoryService, "keyCategoryService must not be null!");
        this.userService = requireNonNull(userRestService, "userService must not be null!");
        this.hashids = requireNonNull(hashids, "hashids must not be null!");
    }

    /**
     * Creates a new key.
     *
     * @param payload the key payload
     */
    public Key create(KeyPayload payload) {
        Key key = new Key();
        String userLogin = getCurrentUserLogin();
        User user = userService.validate(userLogin);
        key.setCreator(user);
        key.setName(payload.getName());
        key.setLogin(payload.getLogin());
        key.setNotes(payload.getNotes());
        Key persistedKey = keyRepository.save(key);
        persistedKey.setHashid(hashids.encode(persistedKey.getId()));
        persistedKey = keyRepository.save(persistedKey);
        log.debug("Created new key with ID '{}'", persistedKey.getId());

        if (ofNullable(payload.getCategoryId()).isPresent()) {
            keyCategoryService.addKey(decodeSingleValueHashid(payload.getCategoryId()), persistedKey.getId());
            persistedKey = keyRepository.findOne(persistedKey.getId());
        }

        userService.addKey(userLogin, persistedKey);
        return persistedKey;
    }

    /**
     * Deletes the key with the specified ID.
     *
     * @param keyId the ID of the key to delete
     * @throws KeyNotFoundException if the key with the specified ID has not been found
     */
    public void delete(Long keyId) throws KeyNotFoundException {
        keyRepository.delete(validate(keyId));
        log.debug("Deleted key with ID '{}'", keyId);
    }

    /**
     * Deletes all keys.
     */
    public void deleteAll() {
        keyRepository.deleteAll();
        log.debug("Deleted all keys");
    }

    /**
     * Gets the key with the specified ID if the current user is authorized to receive.
     *
     * <p> The returned key is based on the linked {@link KeyCategory} which is filtered by the {@link UserGroup} the user is assigned to.
     *
     * @param keyId the ID of the key to get
     * @return the key, {@link Optional#empty()} otherwise
     * @throws KeyNotFoundException if the key with the specified ID has not been found
     */
    public Optional<Key> get(Long keyId) throws KeyNotFoundException {
        if (isCurrentUserInRole(ADMIN)) {
            return Optional.ofNullable(validate(keyId));
        }

        Key key = validate(keyId);
        if (userService.validate(getCurrentUserLogin()).getGroups().stream()
            .anyMatch(userGroup -> ofNullable(key.getCategory()).map(cat -> cat.getGroups().contains(userGroup)).orElse(false))) {
            return Optional.of(key);
        }
        return Optional.empty();
    }

    /**
     * Gets all keys the current user is authorized to receive.
     *
     * <p> The returned keys are based on their linked {@link KeyCategory} which are filtered by the {@link UserGroup} the user is assigned to.
     *
     * @return a collection of keys
     */
    public Set<Key> getAll() {
        if (isCurrentUserInRole(ADMIN)) {
            return new HashSet<>(keyRepository.findAll());
        }
        return userService.validate(getCurrentUserLogin()).getGroups().stream()
            .flatMap(userGroup -> userGroup.getCategories().stream())
            .flatMap(keyCategory -> keyCategory.getKeys().stream())
            .collect(toSet());
    }

    /**
     * Gets a userEncryptedPassword for the specified hashid
     **
     * @return a userEncryptedPassword
     */
    public UserEncryptedPassword getUserEncryptedPassword(Long hashid) {
        String login = SecurityUtils.getCurrentUserLogin();
        User user = userService.validate(login);
        Key key = validate(hashid);
        return userEncryptedPasswordRepository.findOneByOwnerAndKey(user, key);
    }

    /**
     * Updates a key with the specified payload.
     *
     * @param keyId the ID of the key to update
     * @param payload the payload to update the key with
     * @return the updated key
     * @since 0.2.0
     */
    public Key update(Long keyId, KeyPayload payload) {
        Key key = validate(keyId);
        key.setLogin(payload.getLogin());
        key.setName(payload.getName());
        key.setLogin(payload.getLogin());
        key.setNotes(payload.getNotes());
        key = keyRepository.save(key);
        for (KeyPayloadEncryptedPasswords encryptedPasswordsPayload : payload.getEncryptedPasswords()) {
            UserEncryptedPassword userEncryptedPassword = userEncryptedPasswordRepository.findOneByOwnerAndKey(userService.validate(encryptedPasswordsPayload.getLogin()), validate(keyId));
            if(userEncryptedPassword != null) {
                userEncryptedPassword.setPassword(encryptedPasswordsPayload.getEncryptedPassword());
                userEncryptedPasswordRepository.save(userEncryptedPassword);
            } else {
                UserEncryptedPassword newUserEncryptedPassword = new UserEncryptedPassword();
                newUserEncryptedPassword.setKey(key);
                newUserEncryptedPassword.setOwner(userService.validate(encryptedPasswordsPayload.getLogin()));
                newUserEncryptedPassword.setPassword(encryptedPasswordsPayload.getEncryptedPassword());
                userEncryptedPasswordRepository.save(newUserEncryptedPassword);
                key.addUserEncryptedPassword(newUserEncryptedPassword);
            }
        }
        key = keyRepository.save(key);
        log.debug("Updated key with ID '{}'", key.getId());
        return key;
    }

    /**
     * Removes keys of a user that are obsolete because their visibility to the user changed
     *
     * @param user the user user to update
     * @author dvonderbey@communicode.de
     * @since 0.15.0
     */
    public void removeObsoletePasswords(User user) {
        userEncryptedPasswordRepository.findAllByOwner(user)
            .forEach(userEncryptedPassword -> {
                Key key = userEncryptedPassword.getKey();
                KeyCategory category = key.getCategory();
                if (category != null) {
                    Set<UserGroup> usergroups = category.getGroups();
                    if (usergroups.isEmpty()) deleteUserEncryptedPassword(key, userEncryptedPassword);
                    else usergroups.forEach(userGroup -> {
                        if (!user.getGroups().contains(userGroup)) {
                            deleteUserEncryptedPassword(key, userEncryptedPassword);
                        }
                    });

                } else if (!key.getCreator().equals(user)){
                    deleteUserEncryptedPassword(key, userEncryptedPassword);
                }
            });
    }

    /**
     * Deletes an userEncryptedKey from a key and from the repository
     *
     * @param key the key of the userEncryptedPassword
     * @param userEncryptedPassword the userEncryptedPassword to delete
     * @author dvonderbey@communicode.de
     * @since 0.15.0
     */
    private void deleteUserEncryptedPassword(Key key, UserEncryptedPassword userEncryptedPassword) {
        User owner = userEncryptedPassword.getOwner();
        key.removeUserEncryptedPassword(userEncryptedPassword);
        userEncryptedPasswordRepository.delete(userEncryptedPassword);
        keyRepository.save(key);
        log.debug("Removed encryptedPassword '{}’ of key '{}' of user {}.",
            userEncryptedPassword.getId(),
            key.getId(),
            owner.getId());
    }

    /**
     * Validates a key with the specified ID.
     *
     * @param keyId the ID of the key to validate
     * @return the key if validated
     * @throws KeyNotFoundException if the key with the specified ID has not been found
     */
    public Key validate(Long keyId) throws KeyNotFoundException {
        return ofNullable(keyRepository.findOne(keyId)).orElseThrow(KeyNotFoundException::new);
    }

    /**
     * Decodes the specified Hashid.
     *
     * @param hashid the Hashid of the key to decode
     * @return the decoded Hashid if valid
     * @throws KeyNotFoundException if the Hashid is invalid and the key has not been found
     * @since 0.13.0
     */
    private Long decodeSingleValueHashid(String hashid) throws HashidNotValidException {
        long[] decodedHashid = hashids.decode(hashid);
        if (decodedHashid.length == 0) {
            throw new HashidNotValidException();
        }
        return decodedHashid[0];
    }
}
