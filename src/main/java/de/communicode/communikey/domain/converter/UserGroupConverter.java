/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2016
 */
package de.communicode.communikey.domain.converter;

import de.communicode.communikey.domain.UserGroup;
import de.communicode.communikey.domain.UserGroupDto;
import org.springframework.core.convert.converter.Converter;

import java.util.function.Function;

/**
 * A converter to convert {@link UserGroup} entities.
 *
 * @author sgreb@communicode.de
 * @since 0.2.0
 */
public interface UserGroupConverter extends Converter<UserGroup, UserGroupDto>, Function<UserGroup, UserGroupDto> {
    default UserGroupDto apply(UserGroup userGroup) {
        return convert(userGroup);
    }
}