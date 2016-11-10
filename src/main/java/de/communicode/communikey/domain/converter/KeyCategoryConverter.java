/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2016
 */
package de.communicode.communikey.domain.converter;

import de.communicode.communikey.domain.KeyCategory;
import de.communicode.communikey.domain.KeyCategoryDto;
import org.springframework.core.convert.converter.Converter;

import java.util.function.Function;

/**
 * A converter to convert {@link KeyCategory} entities.
 *
 * @author sgreb@communicode.de
 * @since 0.2.0
 */
public interface KeyCategoryConverter extends Converter<KeyCategory, KeyCategoryDto>, Function<KeyCategory, KeyCategoryDto> {
    default KeyCategoryDto apply(KeyCategory keyCategory) {
        return convert(keyCategory);
    }
}
