/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2017
 */
package de.communicode.communikey.repository;

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
     * Finds the user entity with the specified activation key.
     *
     * @param activationKey the activation key of the user to find
     * @return the user entity if found, {@code null} otherwise
     */
    User findOneByActivationKey(String activationKey);

    /**
     * Finds the user entity with the specified reset key.
     *
     * @param resetKey the reset key of the user to find
     * @return the user entity if found, {@code null} otherwise
     */
    User findOneByResetKey(String resetKey);

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
