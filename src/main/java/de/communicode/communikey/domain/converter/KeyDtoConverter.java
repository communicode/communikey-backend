/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2016
 */
package de.communicode.communikey.domain.converter;

import de.communicode.communikey.domain.Key;
import de.communicode.communikey.domain.KeyDto;
import org.springframework.stereotype.Service;

/**
 * A converter to convert {@link Key} entities into {@link KeyDto} data transfer objects.
 *
 * @author sgreb@communicode.de
 * @since 0.2.0
 */
@Service
public class KeyDtoConverter implements KeyConverter {

    /**
     * Converts the given source key entity entity into the associated key data transfer object.
     *
     * @param source the source key entity to convert
     * @return the converted key data transfer object
     */
    @Override
    public KeyDto convert(final Key source) {
        KeyDto keyDto = new KeyDto();
        keyDto.setId(source.getId());
        keyDto.setCategoryId(source.getCategory().getId());
        keyDto.setCreatorId(source.getCreator().getId());
        keyDto.setCreationTimestamp(source.getCreationTimestamp());
        keyDto.setValue(source.getValue());
        return keyDto;
    }
}
