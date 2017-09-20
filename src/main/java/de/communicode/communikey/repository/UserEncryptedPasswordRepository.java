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

    UserEncryptedPassword findOneByOwnerAndKey(User owner, Key key);

    Set<UserEncryptedPassword> findAllByOwner(User owner);

    /**
     * Finds all UserEncryptedPassword entities of the repository with the specified key.
     *
     * @return a collection of found UserEncryptedPassword entities
     */
    Set<UserEncryptedPassword> findAllByKey(Key key);

    @Transactional
    void removeAllByOwner(User owner);

    @Transactional
    void deleteByKey(Key key);
}
