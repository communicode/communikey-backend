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
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

/**
 * A repository for {@link Authority} entities.
 *
 * @author sgreb@communicode.de
 * @since 0.2.0
 */
public interface AuthorityRepository extends CrudRepository<Authority, String> {
    /**
     * Finds all authority entities of the repository.
     *
     * @return a collection of found authority entities
     * @since 0.9.0
     */
    @Override
    Set<Authority> findAll();

    /**
     * Finds the authority entity with the specified name.
     *
     * @param name the name of the authority to find
     * @return the found authority entity, {@code null} otherwise
     * @since 0.3.0
     */
    Authority findOneByName(String name);
}
