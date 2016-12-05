/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2016
 */
package de.communicode.communikey.domain.converter;

import de.communicode.communikey.domain.User;
import de.communicode.communikey.domain.UserGroup;
import de.communicode.communikey.domain.UserGroupDto;
import org.springframework.stereotype.Service;

/**
 * A converter to convert {@link UserGroup} entities into {@link UserGroupDto} data transfer objects.
 *
 * @author sgreb@communicode.de
 * @since 0.2.0
 */
@Service
public class UserGroupDtoConverter implements UserGroupConverter {

    /**
     * Converts the given source user group entity entity into the associated user group data transfer object.
     *
     * @param source the source user group entity to convert
     * @return the converted user group data transfer object
     */
    @Override
    public UserGroupDto convert(final UserGroup source) {
        UserGroupDto userGroupDto = new UserGroupDto();
        userGroupDto.setId(source.getId());
        userGroupDto.setName(source.getName());
        userGroupDto.setUsers(source.getUsers());
        return userGroupDto;
    }
}
