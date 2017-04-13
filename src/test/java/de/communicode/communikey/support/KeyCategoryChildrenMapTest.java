/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2017
 */
package de.communicode.communikey.support;

import static org.hamcrest.Matchers.empty;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import de.communicode.communikey.CommunikeyApplication;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = CommunikeyApplication.class)
@ActiveProfiles("test")
public class KeyCategoryChildrenMapTest {

    KeyCategoryChildrenMap keyCategoryChildrenMap;

    @Before
    public void setUp() throws Exception {
        keyCategoryChildrenMap = KeyCategoryChildrenMap.getInstance();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void dummyTest() {
        assertTrue(true);
    }

}
