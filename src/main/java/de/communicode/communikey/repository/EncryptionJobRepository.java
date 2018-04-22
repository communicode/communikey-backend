/*
 * This file is part of communikey.
 * Copyright (C) 2016-2018  communicode AG <communicode.de>
 *
 * communikey is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
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
     * @param token the token
     * @return the encryption job
     */
    EncryptionJob findByToken(String token);

    /**
     * Finds the encryption job with the specified user and token
     *
     * @param user the user for whom the job is there
     * @param key the key contained in the job
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

    /**
     * Deletes all encryption job of the specified user
     *
     * @param user The user of the encryption job
     */
    @Transactional
    void removeAllByUser(User user);
}
