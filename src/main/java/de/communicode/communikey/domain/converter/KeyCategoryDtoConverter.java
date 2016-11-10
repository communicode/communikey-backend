/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2016
 */
package de.communicode.communikey.domain.converter;

import static java.util.Objects.requireNonNull;

import de.communicode.communikey.domain.KeyCategory;
import de.communicode.communikey.domain.KeyCategoryDto;
import de.communicode.communikey.exception.KeyCategoryNotFoundException;
import org.springframework.stereotype.Service;

/**
 * A converter to convert {@link KeyCategory} entities into {@link KeyCategoryDto} data transfer objects.
 *
 * @author sgreb@communicode.de
 * @since 0.2.0
 */
@Service
public class KeyCategoryDtoConverter implements KeyCategoryConverter {

    /**
     *
     * @param source the source {@link KeyCategory} entity to convert
     * @return the converted {@link KeyCategoryDto} data transfer object
     * @throws KeyCategoryNotFoundException if the given {@code source} entity has not been found
     */
    @Override
    public KeyCategoryDto convert(final KeyCategory source) throws KeyCategoryNotFoundException { //TODO: exception
        requireNonNull(source, "source must not be null");

        KeyCategoryDto keyCategoryDto = new KeyCategoryDto();
        keyCategoryDto.setId(source.getId());
        keyCategoryDto.setCreatorId(source.getCreator().getId());
        if (source.getParent().isPresent()) {
            keyCategoryDto.setParentId(source.getParent().get().getId());
        }
        keyCategoryDto.setResponsibleId(source.getResponsible().getId());
        keyCategoryDto.setName(source.getName());
        return keyCategoryDto;
    }
}
