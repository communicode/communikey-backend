/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2017
 */
package de.communicode.communikey.repository;

import de.communicode.communikey.domain.Tag;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

/**
 * A repository for {@link Tag} entities.
 *
 * @author dvonderbey@communicode.de
 * @since 0.18.0
 */
@Repository
public interface TagRepository extends CrudRepository<Tag, Long> {
    /**
     * Finds all tag entities of the repository.
     *
     * @return a collection of found tag entities
     */
    @Override
    Set<Tag> findAll();
}
