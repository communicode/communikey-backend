/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2016
 */
package de.communicode.communikey.domain.converter;

import static java.util.Objects.requireNonNull;

import de.communicode.communikey.domain.Key;
import de.communicode.communikey.domain.KeyDto;
import de.communicode.communikey.exception.KeyNotFoundException;
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
     *
     * @param source the source {@link Key} entity to convert
     * @return the converted {@link KeyDto} data transfer object
     * @throws KeyNotFoundException if the given {@code source} entity has not been found
     */
    @Override
    public KeyDto convert(final Key source) throws KeyNotFoundException { //TODO: exception
        requireNonNull(source, "source must not be null");

        KeyDto keyDto = new KeyDto();
        keyDto.setId(source.getId());
        keyDto.setCategoryId(source.getCategory().getId());
        keyDto.setCreatorId(source.getCreator().getId());
        keyDto.setCreationTimestamp(source.getCreationTimestamp());
        keyDto.setValue(source.getValue());
        return keyDto;
    }
}
