/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2017
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
