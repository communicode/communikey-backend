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
package de.communicode.communikey.config;

import de.communicode.communikey.domain.Key;
import org.hashids.Hashids;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for the {@link Key} {@link Hashids} en- and decoder.
 *
 * <p>Provides the {@link Hashids} bean.
 *
 * @author sgreb@communicode.de
 * @since 0.12.0
 */
@Configuration
public class HashidsConfig {
    /**
     * The Hashids encoder- and decoder bean.
     *
     * <p><strong>The salt and minimum hash length must not be changed to prevent the invalidation of already generated and persisted Hashids!</strong>
     *
     * @return the Hashids bean
     */
    @Bean
    public Hashids hashids() {
        final String HASHIDS_SALT = "_et#9mKb4)&);!F(pNs6%wNXfo#'R2tG`PQ`6DN@kR^2U;faXZw?:]LF.39fQvX<";
        final int HASHIDS_MIN_LENGTH = 12;
        return new Hashids(HASHIDS_SALT, HASHIDS_MIN_LENGTH);
    }
}
