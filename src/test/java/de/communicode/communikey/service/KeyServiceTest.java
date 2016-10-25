/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2016
 */
package de.communicode.communikey.service;

import static org.hamcrest.CoreMatchers.is;
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

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class KeyServiceTest {
    @Autowired
    KeyService keyService;

    @Test
    public void shouldReturnAllKeys() {
        List<Key> keyList = new ArrayList<>();
        Key key1 = new Key("yogurt");
        Key key2 = new Key("coconut");
        keyService.saveKey(key1);
        keyService.saveKey(key2);
        for (Key p : keyService.getAllKeys()) {
            keyList.add(p);
        }
        assertThat(keyList.size(), is(2));
    }

    @Test
    public void shouldReturnKeyById() {
        Key key = new Key("yogurt");
        keyService.saveKey(key);
        assertThat(keyService.getKeyById(1L).getId(), is(key.getId()));
    }

    @Test(expected = NullPointerException.class)
    public void shouldDeleteKey() {
        Key key = new Key("yogurt");
        keyService.saveKey(key);
        keyService.deleteKey(keyService.getKeyById(1L));
        keyService.getKeyById(1L);
    }

    @Test
    public void shouldModifyKeyValue() {
        Key key = new Key("yogurt");
        keyService.saveKey(key);
        assertThat(keyService.getKeyById(1L).getValue(), is("yogurt"));
        keyService.modifyKeyValue(keyService.getKeyById(1L), "coconut");
        assertThat(keyService.getKeyById(1L).getValue(), is("coconut"));
    }

    @Test
    public void shouldSaveKey() {
        Key key = new Key("yogurt");
        keyService.saveKey(key);
        keyService.saveKey(key);
        assertNotNull(keyService.getKeyById(1L));
    }
}
