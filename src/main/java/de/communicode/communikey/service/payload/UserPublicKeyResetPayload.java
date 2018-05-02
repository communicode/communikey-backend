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
package de.communicode.communikey.service.payload;

import de.communicode.communikey.domain.User;
import javax.validation.constraints.NotBlank;

/**
 * A payload object to reset the password of a {@link User} with a specified reset token.
 *
 * @author sgreb@communicode.de
 * @since 0.2.0
 */
public class UserPublicKeyResetPayload {

    @NotBlank
    private String publicKey;

    @NotBlank
    private String resetToken;

    public UserPublicKeyResetPayload() {}

    public UserPublicKeyResetPayload(String publicKey, String resetToken) {
        this.publicKey = publicKey;
        this.resetToken = resetToken;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public String getResetToken() {
        return resetToken;
    }

    @Override
    public String toString() {
        return "UserPublicKeyResetPayload{" +
            "publicKey='" + publicKey + '\'' +
            ", resetToken='" + resetToken + '\'' +
            '}';
    }
}
