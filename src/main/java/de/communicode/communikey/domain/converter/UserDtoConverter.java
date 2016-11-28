/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2016
 */
package de.communicode.communikey.domain.converter;

import de.communicode.communikey.domain.Key;
import de.communicode.communikey.domain.KeyCategory;
import de.communicode.communikey.domain.User;
import de.communicode.communikey.domain.UserDto;
import de.communicode.communikey.domain.UserGroup;
import org.springframework.stereotype.Service;

/**
 * A converter to convert {@link User} entities into {@link UserDto} data transfer objects.
 *
 * @author sgreb@communicode.de
 * @since 0.2.0
 */
@Service
public class UserDtoConverter implements UserConverter {

    /**
     * Converts the given source user entity entity into the associated user data transfer object.
     *
     * @param source the source user entity to convert
     * @return the converted user data transfer object
     */
    @Override
    public UserDto convert(final User source) {
        UserDto userDto = new UserDto();
        userDto.setId(source.getId());
        userDto.setKeys(source.getKeys().stream()
            .mapToLong(Key::getId)
            .toArray()
        );
        userDto.setKeyCategories(source.getKeyCategories().stream()
            .mapToLong(KeyCategory::getId)
            .toArray()
        );
        userDto.setResponsibleKeyCategories(source.getResponsibleKeyCategories().stream()
            .mapToLong(KeyCategory::getId)
            .toArray()
        );
        userDto.setGroups(source.getGroups().stream()
            .mapToLong(UserGroup::getId)
            .toArray()
        );
        userDto.setEnabled(source.isEnabled());
        userDto.setUsername(source.getUsername());
        userDto.setRole(source.getRole());
        return userDto;
    }
}
