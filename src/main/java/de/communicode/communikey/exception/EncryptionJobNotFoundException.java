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
package de.communicode.communikey.exception;

import de.communicode.communikey.domain.EncryptionJob;

/**
 * Thrown to indicate that a method has been passed a non existing {@link EncryptionJob}.
 *
 * @author dvonderbey@communicode.de
 * @since 0.15.0
 */
public class EncryptionJobNotFoundException extends RuntimeException {

    /**
     * Constructs a {@code EncryptionJobNotFoundException}.
     */
    public EncryptionJobNotFoundException() {
        super("encryption job not found");
    }

    /**
     * Constructs an {@code EncryptionJobNotFoundException} with the specified {@link EncryptionJob} token.
     *
     * @param encryptionJobToken the token of the encryption job that has not been found
     */
    public EncryptionJobNotFoundException(String encryptionJobToken) {
        super("enryption job with token '" + encryptionJobToken + "' not found");
    }
}
