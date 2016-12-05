/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2016
 */
package de.communicode.communikey.domain.converter;

import de.communicode.communikey.domain.KeyCategory;
import de.communicode.communikey.domain.KeyCategoryDto;
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
     * Converts the given source key category entity entity into the associated key category data transfer object.
     *
     * @param source the source source key category entity to convert
     * @return the converted key category data transfer object
     */
    @Override
    public KeyCategoryDto convert(final KeyCategory source)  {
        KeyCategoryDto keyCategoryDto = new KeyCategoryDto();
        keyCategoryDto.setId(source.getId());
        keyCategoryDto.setChilds(source.getChilds());
        keyCategoryDto.setCreatorId(source.getCreator().getId());
        if (source.getParent().isPresent()) {
            keyCategoryDto.setParentId(source.getParent().get().getId());
        }
        keyCategoryDto.setResponsibleId(source.getResponsible().getId());
        keyCategoryDto.setName(source.getName());
        return keyCategoryDto;
    }
}
