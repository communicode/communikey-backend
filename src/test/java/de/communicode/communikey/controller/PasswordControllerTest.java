/*
  * Copyright (C) 2016 communicode AG
 */
package de.communicode.communikey.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

import de.communicode.communikey.CommunikeyApplication;
import de.communicode.communikey.domain.Password;
import de.communicode.communikey.repository.PasswordRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * Unit tests for the {@link PasswordController} class.
 *
 * @since 0.1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = CommunikeyApplication.class)
@WebAppConfiguration
public class PasswordControllerTest {
    private MockMvc mockMvc;
    private PasswordController passwordController;
    @Autowired
    private PasswordRepository passwordRepository;
    @Autowired
    private WebApplicationContext wac;

    @Before
    public void setup() {
        passwordController = new PasswordController();
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void passwordsEndpoint() throws Exception {
        mockMvc.perform(get("/passwords"))
            .andExpect(status().isOk())
            .andExpect(view().name("passwords"));
    }

    @Test
    public void passwordsNewEndpoints() throws Exception {
        mockMvc.perform(get("/passwords/new"))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("password"))
            .andExpect(model().attributeExists("newPasswordForm"))
            .andExpect(model().size(2))
            .andExpect(view().name("/passwords-new"));

        mockMvc.perform(post("/passwords/new"))
            .andExpect(view().name("redirect:/passwords"))
            .andExpect(status().is3xxRedirection());
    }

    @Test
    public void passwordsEditEndpoints() throws Exception {
        Password password = new Password("yogurt");
        passwordRepository.save(password);

        mockMvc.perform(get("/passwords/1/edit"))
            .andExpect(status().isOk())
            .andExpect(view().name("/passwords-edit"))
            .andExpect(model().attributeExists("password"))
            .andExpect(model().attributeExists("editPasswordForm"))
            .andExpect(model().size(2));

        mockMvc.perform(post("/passwords/1/edit"))
            .andExpect(view().name("redirect:/passwords"))
            .andExpect(status().is3xxRedirection());
    }

    @Test
    public void passwordsDeleteRedirectionEndpoint() throws Exception {
        Password password = new Password("yogurt");
        passwordRepository.save(password);

        mockMvc.perform(get("/passwords/1/delete"))
            .andExpect(view().name("redirect:/passwords"))
            .andExpect(status().is3xxRedirection());
    }
}
