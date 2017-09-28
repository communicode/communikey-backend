/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2017
 */
package de.communicode.communikey.repository;

import de.communicode.communikey.domain.Key;
import de.communicode.communikey.domain.User;
import de.communicode.communikey.domain.UserEncryptedPassword;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

/**
 * A repository for {@link UserEncryptedPassword} entities.
 *
 * @author dvonderbey@communicode.de
 * @since 0.15.0
 */
@Repository
public interface UserEncryptedPasswordRepository extends CrudRepository<UserEncryptedPassword, Long> {
    /**
     * Finds all key entities of the repository.
     *
     * @return a collection of found key entities
     */
    @Override
    Set<UserEncryptedPassword> findAll();

    /**
     * Finds all UserEncryptedPassword entities of the
     * repository with the specified key.
     *
     * @param owner the user that owns the passwords
     * @param key the key of the password
     * @return the found UserEncryptedPassword entity
     */
    UserEncryptedPassword findOneByOwnerAndKey(User owner, Key key);

    /**
     * Finds all UserEncryptedPassword entities of the
     * repository with the specified key.
     *
     * @param owner the user that owns the passwords
     * @return a collection of found UserEncryptedPassword entities
     */
    Set<UserEncryptedPassword> findAllByOwner(User owner);

    /**
     * Finds all UserEncryptedPassword entities of the repository with the specified key.
     *
     * @param key the key
     * @return a collection of found UserEncryptedPassword entities
     */
    Set<UserEncryptedPassword> findAllByKey(Key key);

    /**
     * Deletes all user encrypted passwords owned by
     * the specified user.
     *
     * @param owner the user that owns the password
     */
    @Transactional
    void removeAllByOwner(User owner);

    /**
     * Deletes all user encrypted passwords that reference
     * the specified key.
     *
     * @param key the key
     */
    @Transactional
    void deleteByKey(Key key);
}
