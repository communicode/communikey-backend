/*
  * Copyright (C) 2016 communicode AG
 */
package de.communicode.communikey.service;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

import de.communicode.communikey.CommunikeyApplication;
import de.communicode.communikey.domain.Password;
import de.communicode.communikey.repository.PasswordRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = CommunikeyApplication.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class PasswordServiceTest {
    @Autowired
    PasswordService passwordService;


    @Test
    public void shouldReturnAllPasswords() {
        List<Password> passwordList = new ArrayList<>();
        Password password1 = new Password("yogurt");
        Password password2 = new Password("coconut");
        passwordService.savePassword(password1);
        passwordService.savePassword(password2);
        for (Password p : passwordService.getAllPasswords()) {
            passwordList.add(p);
        }
        assertThat(passwordList.size(), is(2));
    }

    @Test
    public void shouldReturnPasswordById() {
        Password password = new Password("yogurt");
        passwordService.savePassword(password);
        assertThat(passwordService.getPasswordById(1L).getId(), is(password.getId()));
    }

    @Test
    public void shouldDeletePassword() {
        Password password = new Password("yogurt");
        passwordService.savePassword(password);
        passwordService.deletePassword(passwordService.getPasswordById(1L));
        assertNull(passwordService.getPasswordById(1L));
    }

    @Test
    public void shouldModifyPasswordValue() {
        Password password = new Password("yogurt");
        passwordService.savePassword(password);
        assertThat(passwordService.getPasswordById(1L).getValue(), is("yogurt"));
        passwordService.modifyPasswordValue(passwordService.getPasswordById(1L), "coconut");
        assertThat(passwordService.getPasswordById(1L).getValue(), is("coconut"));
    }

    @Test
    public void shouldSavePassword() {
        Password password = new Password("yogurt");
        passwordService.savePassword(password);
        passwordService.savePassword(password);
        assertNotNull(passwordService.getPasswordById(1L));
    }
}
