/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2016
*/
package de.communicode.communikey.controller;

import static de.communicode.communikey.CommunikeyConstants.ENDPOINT_LOGIN;
import static de.communicode.communikey.CommunikeyConstants.ENDPOINT_LOGOUT;
import static de.communicode.communikey.CommunikeyConstants.ENDPOINT_PASSWORDS;
import static de.communicode.communikey.CommunikeyConstants.REQUEST_PARAM_LOGIN_LOGOUT;
import static de.communicode.communikey.CommunikeyConstants.withParameters;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.logout;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * Unit tests for the {@link SessionController}.
 *
 * @since 0.2.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SessionControllerTest {
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext wac;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
            .apply(springSecurity())
            .defaultRequest(get(ENDPOINT_LOGIN).with(user("user").password("pass").roles("USER")))
            .build();
    }

    @Test
    @Ignore
    public void loginWithCSRFProtection() throws Exception {
        mockMvc.perform(formLogin(ENDPOINT_LOGIN))
            .andExpect(status().isFound())
            .andExpect(redirectedUrl(ENDPOINT_PASSWORDS));
    }

    @Test
    public void logoutWithCSRFProtection() throws Exception {
        mockMvc.perform(logout(ENDPOINT_LOGOUT))
            .andExpect(status().isFound())
            .andExpect(redirectedUrl(withParameters(ENDPOINT_LOGIN, REQUEST_PARAM_LOGIN_LOGOUT)));
    }
}
