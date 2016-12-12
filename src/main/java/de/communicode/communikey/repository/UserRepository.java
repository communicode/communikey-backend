/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2016
 */
package de.communicode.communikey.repository;

import de.communicode.communikey.exception.UserNotFoundException;
import org.springframework.data.repository.CrudRepository;
import de.communicode.communikey.domain.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * A repository for {@link User} entities.
 *
 * @author sgreb@communicode.de
 * @since 0.2.0
 */
@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    /**
     * Finds the user entity with the specified email.
     *
     * @param email the email of the user to find
     * @return the found user entity
     * @throws UserNotFoundException if the user entity with the specified email has not been found
     */
    User findOneByEmail(String email) throws UserNotFoundException;
}
