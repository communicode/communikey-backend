/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2017
 */
package de.communicode.communikey.repository;

import de.communicode.communikey.domain.Key;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * A repository for {@link Key} entities.
 *
 * @author sgreb@communicode.de
 * @since 0.1.0
 */
@Repository
public interface KeyRepository extends JpaRepository<Key, Long> {
}