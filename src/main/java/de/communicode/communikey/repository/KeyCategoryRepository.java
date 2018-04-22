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

import de.communicode.communikey.domain.KeyCategory;
import de.communicode.communikey.domain.UserGroup;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

/**
 * A repository for {@link KeyCategory} entities.
 *
 * @author sgreb@communicode.de
 * @since 0.2.0
 */
@Repository
public interface KeyCategoryRepository extends CrudRepository<KeyCategory, Long> {

    /**
     * Finds all key category entities of the repository.
     *
     * @return a collection of found key category entities
     */
    @Override
    Set<KeyCategory> findAll();

    /**
     * Finds all key category entities where the parent key category is {@code null}.
     *
     * @return a collection of found key category entities
     */
    Set<KeyCategory> findAllByParentIsNull();

    /**
     * Finds all key category entities which are in the specified usergroup.
     *
     * @param userGroup the usergroup the key categories should contain
     * @return a collection of found key category entities
     */
    Set<KeyCategory> findAllByGroupsContains(UserGroup userGroup);
}
