/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2016
 */
package de.communicode.communikey.controller;

import static de.communicode.communikey.config.CommunikeyConstants.ENDPOINT_KEYS;
import static de.communicode.communikey.config.CommunikeyConstants.ENDPOINT_ROOT;
import static de.communicode.communikey.config.CommunikeyConstants.REQUEST_KEY_NEW;
import static de.communicode.communikey.config.CommunikeyConstants.TEMPLATE_KEY_EDIT;
import static de.communicode.communikey.config.CommunikeyConstants.TEMPLATE_KEY_NEW;
import static de.communicode.communikey.util.CommunikeyConstantsUtil.asRedirect;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

import de.communicode.communikey.domain.Key;
import de.communicode.communikey.form.EditKeyForm;
import de.communicode.communikey.repository.KeyRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * Unit tests for the {@link KeyController} class.
 *
 * @since 0.1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class KeyControllerTest {
    private MockMvc mockMvc;
    @Autowired
    private KeyRepository keyRepository;
    @Autowired
    private WebApplicationContext wac;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
            .apply(springSecurity())
            .defaultRequest(get(ENDPOINT_ROOT).with(user("user").password("pass").roles("USER")))
            .build();
    }

    @Test
    public void keysEndpoint() throws Exception {
        mockMvc.perform(get(ENDPOINT_KEYS))
            .andExpect(status().isOk())
            .andExpect(view().name(ENDPOINT_KEYS));
    }

    @Test
    public void keysNewEndpoints() throws Exception {
        mockMvc.perform(get(REQUEST_KEY_NEW)
            .with(csrf())
        )
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("key"))
            .andExpect(model().attributeExists("newKeyForm"))
            .andExpect(model().size(2))
            .andExpect(view().name(TEMPLATE_KEY_NEW));

        mockMvc.perform(post(REQUEST_KEY_NEW)
            .with(csrf())
        )
            .andExpect(view().name(asRedirect(ENDPOINT_KEYS)))
            .andExpect(status().is3xxRedirection());
    }

    @Test
    public void keysEditEndpoints() throws Exception {
        Key key = new Key("yogurt");
        keyRepository.save(key);

        mockMvc.perform(get("/keys/1/edit")
            .with(csrf())
        )
            .andExpect(status().isOk())
            .andExpect(view().name(TEMPLATE_KEY_EDIT))
            .andExpect(model().attributeExists("key"))
            .andExpect(model().attributeExists("editKeyForm"))
            .andExpect(model().size(2));

        EditKeyForm editKeyForm = new EditKeyForm();
        editKeyForm.setValue("newKey");

        mockMvc.perform(post("/keys/1/edit").with(csrf())
            .flashAttr("editKeyForm", editKeyForm)
        )
            .andExpect(status().isFound())
            .andExpect(view().name(asRedirect(ENDPOINT_KEYS)))
            .andExpect(redirectedUrl(ENDPOINT_KEYS));
    }

    @Test
    public void keysDeleteRedirectionEndpoint() throws Exception {
        Key key = new Key("yogurt");
        keyRepository.save(key);

        mockMvc.perform(get("/keys/1/delete"))
            .andExpect(view().name(asRedirect(ENDPOINT_KEYS)))
            .andExpect(status().is3xxRedirection());
    }
}
