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
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toSet;

import de.communicode.communikey.domain.Key;
import de.communicode.communikey.domain.KeyCategory;
import de.communicode.communikey.domain.User;
import de.communicode.communikey.domain.UserGroup;
import de.communicode.communikey.domain.UserEncryptedPassword;
import de.communicode.communikey.exception.HashidNotValidException;
import de.communicode.communikey.exception.KeyNotAccessibleByUserException;
import de.communicode.communikey.exception.UserEncryptedPasswordNotFoundException;
import de.communicode.communikey.repository.UserEncryptedPasswordRepository;
import de.communicode.communikey.security.AuthoritiesConstants;
import de.communicode.communikey.security.SecurityUtils;
import de.communicode.communikey.service.payload.KeyPayload;
import de.communicode.communikey.exception.KeyNotFoundException;
import de.communicode.communikey.repository.KeyRepository;
import de.communicode.communikey.repository.UserRepository;
import de.communicode.communikey.service.payload.KeyPayloadEncryptedPasswords;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hashids.Hashids;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

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
    private final UserRepository userRepository;
    private final AuthorityService authorityService;

    @Autowired
    public KeyService(KeyRepository keyRepository, @Lazy KeyCategoryService keyCategoryService,
                      UserService userRestService, Hashids hashids, UserEncryptedPasswordRepository
                      userEncryptedPasswordRepository, UserRepository userRepository,
                      AuthorityService authorityService) {
        this.keyRepository = requireNonNull(keyRepository, "keyRepository must not be null!");
        this.userEncryptedPasswordRepository = requireNonNull(userEncryptedPasswordRepository, "userEncryptedPasswordRepository must not be null!");
        this.keyCategoryService = requireNonNull(keyCategoryService, "keyCategoryService must not be null!");
        this.userService = requireNonNull(userRestService, "userService must not be null!");
        this.hashids = requireNonNull(hashids, "hashids must not be null!");
        this.userRepository = requireNonNull(userRepository, "userRepository must not be null!");
        this.authorityService = requireNonNull(authorityService, "authorityService must not be null!");
    }

    /**
     * Creates a new key.
     *
     * @param payload the key payload
     */
    public Key create(KeyPayload payload) {
        Key key = new Key();
        checkPayloadKeyAccess(key, payload);
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
        for (KeyPayloadEncryptedPasswords encryptedPasswordsPayload : payload.getEncryptedPasswords()) {
            createUserEncryptedPasswords(key, encryptedPasswordsPayload);
        }
        if (ofNullable(payload.getCategoryId()).isPresent()) {
            keyCategoryService.addKey(decodeSingleValueHashid(payload.getCategoryId()), persistedKey.getId());
            persistedKey = keyRepository.findOne(persistedKey.getId());
        }
        userService.addKey(userLogin, persistedKey);
        return persistedKey;
    }

    /**
     * Creates new userEncryptedPassword from KeyPayloadEncryptedPasswords.
     *
     * @param payload the array of userEncryptedPasswords from a key payload
     */
    private void createUserEncryptedPasswords(Key key, KeyPayloadEncryptedPasswords payload) {
        UserEncryptedPassword newUserEncryptedPassword = new UserEncryptedPassword();
        newUserEncryptedPassword.setKey(key);
        newUserEncryptedPassword.setOwner(userService.validate(payload.getLogin()));
        newUserEncryptedPassword.setPassword(payload.getEncryptedPassword());
        userEncryptedPasswordRepository.save(newUserEncryptedPassword);
        key.addUserEncryptedPassword(newUserEncryptedPassword);
        keyRepository.save(key);
        userService.addUserEncryptedPassword(payload.getLogin(), newUserEncryptedPassword);
        log.debug("Created userencryptedPassword for key {} and user {}", key.getId(), payload.getLogin());
    }

    /**
     * Deletes the key with the specified ID.
     *
     * @param keyId the ID of the key to delete
     * @throws KeyNotFoundException if the key with the specified ID has not been found
     */
    public void delete(Long keyId) throws KeyNotFoundException {
        Key key = validate(keyId);
        userEncryptedPasswordRepository.deleteByKey(key);
        keyRepository.delete(key);
        log.debug("Deleted key with ID '{}'", keyId);
    }

    /**
     * Deletes all keys.
     */
    public void deleteAll() {
        userEncryptedPasswordRepository.deleteAll();
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
     *
     * @return a userEncryptedPassword
     */
    public Optional<UserEncryptedPassword> getUserEncryptedPassword(Long hashid) throws KeyNotFoundException, UserEncryptedPasswordNotFoundException {
        String login = SecurityUtils.getCurrentUserLogin();
        User user = userService.validate(login);
        Key key = validate(hashid);
        UserEncryptedPassword userEncryptedPassword = userEncryptedPasswordRepository.findOneByOwnerAndKey(user, key);
        ofNullable(userEncryptedPassword)
            .orElseThrow(UserEncryptedPasswordNotFoundException::new);
        return Optional.of(userEncryptedPassword);
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
        checkPayloadKeyAccess(key, payload);
        key.setLogin(payload.getLogin());
        key.setName(payload.getName());
        key.setLogin(payload.getLogin());
        key.setNotes(payload.getNotes());
        key = keyRepository.save(key);
        for (KeyPayloadEncryptedPasswords encryptedPasswordsPayload : payload.getEncryptedPasswords()) {
            UserEncryptedPassword userEncryptedPassword = userEncryptedPasswordRepository.findOneByOwnerAndKey(
                userService.validate(encryptedPasswordsPayload.getLogin()), validate(keyId));
            if(userEncryptedPassword != null) {
                userEncryptedPassword.setPassword(encryptedPasswordsPayload.getEncryptedPassword());
                userEncryptedPasswordRepository.save(userEncryptedPassword);
            } else {
                createUserEncryptedPasswords(key, encryptedPasswordsPayload);
            }
        }
        key = keyRepository.save(key);
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
                    Set<UserGroup> userGroups = category.getGroups();
                    if (userGroups.isEmpty()) deleteUserEncryptedPassword(key, userEncryptedPassword);
                    else userGroups.forEach(userGroup -> {
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
     * Checks if the intended owner of the userEncryptedPassword has access to the key itself.
     * Returns true, if the user who intents to PUT a new encrypted Password in the Set has indeed
     * access to it. Otherwise returns false
     *
     * @author lleifermann@communicode.de
     * @param key the key
     * @param payload the payload to inspect
     * @return boolean
     * @since 0.15.0
     */
    private boolean checkKeyAccess(Key key, KeyPayloadEncryptedPasswords payload) {
        KeyCategory keyCategory = key.getCategory();
        User user = userService.validate(payload.getLogin());
        if(user.getAuthorities().contains(authorityService.get(AuthoritiesConstants.ADMIN)))
            return true;
        for (UserGroup userGroup:keyCategory.getGroups()) {
            if(user.getGroups().contains(userGroup)) return true;
        }
        log.info("User '{}' tried to add an encryptedPassword for user {} without access to the key.", getCurrentUserLogin(), user.getLogin());
        return false;
    }

    public void removeAllUserEncryptedPasswordsForUser(User user) {
        userEncryptedPasswordRepository.removeAllByOwner(user);
        log.debug("Removed all encrypted passwords for user '{}'", user.getLogin());
    }

    /**
     * Check encryptedPasswords in a given payload for key access.
     *
     * @author lleifermann@communicode.de
     * @param key the key
     * @param keyPayload the payload to inspect
     * @since 0.15.0
     */
    private void checkPayloadKeyAccess(Key key, KeyPayload keyPayload) {
        for (KeyPayloadEncryptedPasswords encryptedPasswordsPayload : keyPayload.getEncryptedPasswords()) {
            if (!checkKeyAccess(key, encryptedPasswordsPayload)) throw new KeyNotAccessibleByUserException();
        }
    }

    /**
     * Returns a list of public keys and the names of the subscribers for a specific key
     * Goes through the category of the key to find all groups and their members that
     * are able to "see" the key. Also adds all admins to the list.
     *
     * @param keyId the keyId of the key of which the subscribers are wanted
     * @author dvonderbey@communicode.de
     * @since 0.15.0
     */
    public Optional<Set<User.SubscriberInfo>> getSubscribers(Long keyId) {
        Stream<User.SubscriberInfo> subscriberStream = get(keyId)
            .map(Key::getCategory)
            .map(KeyCategory::getGroups)
            .map(Collection::stream)
            .orElse(Stream.empty())
            .flatMap(userGroup -> userGroup.getUsers().stream())
            .filter(user -> user.getPublicKey() != null)
            .map(User::getSubscriberInfo);

        Stream<User.SubscriberInfo> adminStream = userRepository.findAllByAuthorities(authorityService.get(AuthoritiesConstants.ADMIN))
            .stream()
            .map(User::getSubscriberInfo);

        return Stream.concat(subscriberStream, adminStream)
            .collect(collectingAndThen(toSet(), Optional::of));
    }

    /**
     * Deletes an userEncryptedPassword from a key and from the repository
     *
     * @param key the key of the userEncryptedPassword
     * @param userEncryptedPassword the userEncryptedPassword to delete
     * @author dvonderbey@communicode.de
     * @since 0.15.0
     */
    private void deleteUserEncryptedPassword(Key key, UserEncryptedPassword userEncryptedPassword) {
        User owner = userEncryptedPassword.getOwner();
        owner.removeUserEncryptedPassword(userEncryptedPassword);
        userRepository.save(owner);
        key.removeUserEncryptedPassword(userEncryptedPassword);
        keyRepository.save(key);
        userEncryptedPasswordRepository.delete(userEncryptedPassword);
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
