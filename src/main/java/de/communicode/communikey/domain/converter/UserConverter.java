/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2016
 */
package de.communicode.communikey.domain.converter;

import de.communicode.communikey.domain.User;
import de.communicode.communikey.domain.UserDto;
import org.springframework.core.convert.converter.Converter;

import java.util.function.Function;

/**
 * A converter to convert {@link User} entities.
 *
 * @author sgreb@communicode.de
 * @since 0.2.0
 */
public interface UserConverter extends Converter<User, UserDto>, Function<User, UserDto> {
    default UserDto apply(User user) {
        return convert(user);
    }
}