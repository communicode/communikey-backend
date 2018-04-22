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
