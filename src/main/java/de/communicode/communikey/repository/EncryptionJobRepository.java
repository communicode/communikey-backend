/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2017
 */
package de.communicode.communikey.repository;

import de.communicode.communikey.domain.EncryptionJob;
import de.communicode.communikey.domain.Key;
import de.communicode.communikey.domain.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

/**
 * A repository for {@link EncryptionJob} entities.
 *
 * @author dvonderbey@communicode.de
 * @since 0.15.0
 */
@Repository
public interface EncryptionJobRepository extends CrudRepository<EncryptionJob, Long> {

    /**
     * Finds all encryption job entities of the repository.
     *
     * @return a collection of found encryption job entities
     */
    @Override
    Set<EncryptionJob> findAll();

    /**
     * Finds the encryption job with the specified token
     *
     * @return the encryption job
     */
    EncryptionJob findByToken(String token);

    /**
     * Finds the encryption job with the specified user and token
     *
     * @return the encryption job
     */
    EncryptionJob findByUserAndKey(User user, Key key);

    /**
     * Deletes every encryption job of the specified key.
     *
     * @param key The key of which the encryption jobs should be deleted
     */
    @Transactional
    void deleteByKey(Key key);

    /**
     * Deletes the encryption job with the specified token
     *
     * @param token The token of the encryption job
     */
    @Transactional
    void deleteByToken(String token);
}
