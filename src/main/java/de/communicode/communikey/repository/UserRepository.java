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

import de.communicode.communikey.domain.Authority;
import de.communicode.communikey.domain.UserGroup;
import org.springframework.data.jpa.repository.EntityGraph;
import de.communicode.communikey.domain.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

/**
 * A repository for {@link User} entities.
 *
 * @author sgreb@communicode.de
 * @since 0.2.0
 */
@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    /**
     * Finds all user entities of the repository.
     *
     * @return a collection of found user entities
     * @since 0.9.0
     */
    @Override
    Set<User> findAll();

    /**
     * Finds all user entities with a specific authority of the repository.
     *
     * @param authority the authority the users should be in
     * @return a collection of found user entities
     * @since 0.15.0
     */
    Set<User> findAllByAuthorities(Authority authority);

    /**
     * Finds all user entities with a specific usergroup.
     *
     * @param userGroup the userGroup the users should be in
     * @return a collection of found user entities
     * @since 0.15.0
     */
    Set<User> findAllByGroupsContains(UserGroup userGroup);

    /**
     * Finds the user entity with the specified activation token.
     *
     * @param activationToken the activation token of the user to find
     * @return the user entity if found, {@code null} otherwise
     */
    User findOneByActivationToken(String activationToken);

    /**
     * Finds the user entity with the specified reset token.
     *
     * @param resetToken the reset token of the user to find
     * @return the user entity if found, {@code null} otherwise
     */
    User findOneByResetToken(String resetToken);

    /**
     * Finds the user entity with the specified publicKeyResetToken.
     *
     * @param publicKeyResetToken the publicKeyResetToken of the user to find
     * @return the user entity if found, {@code null} otherwise
     */
    User findOneByPublicKeyResetToken(String publicKeyResetToken);

    /**
     * Finds the user entity with the specified email.
     *
     * @param email the email of the user to find
     * @return the found user entity, {@code null} otherwise
     */
    User findOneByEmail(String email);

    /**
     * Finds the user entity with the specified login.
     *
     * @param login the login of the user to find
     * @return the found user entity, {@code null} otherwise
     */
    User findOneByLogin(String login);

    /**
     * Finds the user entity with the specified ID including its granted authorities.
     *
     * @param id the ID of the user to find
     * @return the found user entity
     */
    @EntityGraph(attributePaths = "authorities")
    User findOneWithAuthoritiesById(Long id);

    /**
     * Finds the user entity with the specified email including its granted authorities.
     *
     * @param email the email of the user to find
     * @return the user entity if found, {@code null} otherwise
     */
    @EntityGraph(attributePaths = "authorities")
    User findOneWithAuthoritiesByEmail(String email);

    /**
     * Finds the user entity with the specified login including its granted authorities.
     *
     * @param login the login of the user to find
     * @return the user entity if found, {@code null} otherwise
     */
    @EntityGraph(attributePaths = "authorities")
    User findOneWithAuthoritiesByLogin(String login);
}
