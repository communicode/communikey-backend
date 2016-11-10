/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2016
 */
package de.communicode.communikey.domain.converter;

import de.communicode.communikey.domain.Key;
import de.communicode.communikey.domain.KeyDto;
import org.springframework.core.convert.converter.Converter;

import java.util.function.Function;

/**
 * A converter to convert {@link Key} entities.
 *
 * @author sgreb@communicode.de
 * @since 0.2.0
 */
public interface KeyConverter extends Converter<Key, KeyDto>, Function<Key, KeyDto> {
    default KeyDto apply(Key key) {
        return convert(key);
    }
}
