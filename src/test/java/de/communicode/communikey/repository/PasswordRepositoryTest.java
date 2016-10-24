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

import de.communicode.communikey.domain.Password;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * Unit tests for the {@link PasswordRepository} class.
 *
 * @since 0.1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class PasswordRepositoryTest {
    @Autowired
    private PasswordRepository passwordRepository;

    @Test
    public void shouldReturnCorrectPasswordId() {
        Password password = new Password("yogurt");
        passwordRepository.save(password);
        assertThat(passwordRepository.findOneById(1L).getId(), is(password.getId()));
        assertNotNull(passwordRepository.findOneById(1L).getCreationTimestamp());
    }

    @Test
    public void shouldContainTimestamp() {
        Password password = new Password("yogurt");
        passwordRepository.save(password);
        assertNotNull(passwordRepository.findOneById(1L).getCreationTimestamp());
    }

    @Test
    public void shouldReturnCorrectPasswordValue() {
        Password password = new Password("yogurt");
        passwordRepository.save(password);
        assertEquals(passwordRepository.findOneById(1L).getValue(), password.getValue());
    }

    @Test
    public void shouldContainOnlyOnePassword() {
        Password password = new Password("yogurt");
        passwordRepository.save(password);
        List<Password>  passwordList = new ArrayList<>();
        for(Password p : passwordRepository.findAll()) {
            passwordList.add(p);
        }
        assertThat(passwordList.size(), is(1));
    }
}
