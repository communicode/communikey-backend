/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2016
 */
package de.communicode.communikey.repository;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import de.communicode.communikey.domain.Key;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * Unit tests for the {@link KeyRepository} class.
 *
 * @since 0.1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class KeyRepositoryTest {
    @Autowired
    private KeyRepository keyRepository;

    @Test
    public void shouldReturnCorrectKeyId() {
        Key key = new Key("yogurt");
        keyRepository.save(key);
        assertThat(keyRepository.findOneById(1L).getId(), is(key.getId()));
        assertNotNull(keyRepository.findOneById(1L).getCreationTimestamp());
    }

    @Test
    public void shouldContainTimestamp() {
        Key key = new Key("yogurt");
        keyRepository.save(key);
        assertNotNull(keyRepository.findOneById(1L).getCreationTimestamp());
    }

    @Test
    public void shouldReturnCorrectKeyValue() {
        Key key = new Key("yogurt");
        keyRepository.save(key);
        assertEquals(keyRepository.findOneById(1L).getValue(), key.getValue());
    }

    @Test
    public void shouldContainOnlyOneKey() {
        Key key = new Key("yogurt");
        keyRepository.save(key);
        List<Key> keyList = new ArrayList<>();
        for(Key p : keyRepository.findAll()) {
            keyList.add(p);
        }
        assertThat(keyList.size(), is(1));
    }
}
