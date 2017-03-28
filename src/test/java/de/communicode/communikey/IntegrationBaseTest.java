/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2017
 */
package de.communicode.communikey;

import static java.util.Objects.requireNonNull;

import com.google.common.base.Joiner;
import com.google.common.collect.Sets;
import de.communicode.communikey.config.CommunikeyProperties;
import de.communicode.communikey.config.SecurityConfig;
import de.communicode.communikey.domain.Authority;
import de.communicode.communikey.domain.User;
import de.communicode.communikey.repository.AuthorityRepository;
import de.communicode.communikey.repository.UserRepository;
import de.communicode.communikey.security.AuthoritiesConstants;
import de.communicode.communikey.security.SecurityUtils;
import org.junit.Before;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.util.OAuth2Utils;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * The base class for all communikey REST API integration tests.
 *
 * @author sgreb@communicode.de
 * @author fsanchez@communicode.de
 * @since 0.3.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = CommunikeyApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Category(CommunikeyIntegrationTest.class)
@ActiveProfiles("test")
public abstract class IntegrationBaseTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthorityRepository authorityRepository;
    @Autowired
    private CommunikeyProperties communikeyProperties;
    @Autowired
    private Environment environment;
    @Autowired
    private TestRestTemplate testRestTemplate;
    private String decodedUserPassword = "password";
    protected User user = new User();
    protected String userOAuth2AccessToken;
    protected String adminUserOAuth2AccessToken;

    @Before
    public void initialize() {
        user = userRepository.save(createUser());
        adminUserOAuth2AccessToken = generateUserAccessToken(
                communikeyProperties.getSecurity().getRoot().getLogin(),
                communikeyProperties.getSecurity().getRoot().getPassword());
        userOAuth2AccessToken = generateUserAccessToken(user.getLogin(), decodedUserPassword);
    }

    private String generateUserAccessToken(String login, String password) {
        StringBuilder requestUrlBuilder = new StringBuilder();
        Map<String, String> oAuth2TokenRequestParam = new HashMap<>();
        testRestTemplate = new TestRestTemplate(login, password);

        oAuth2TokenRequestParam.put(OAuth2Utils.RESPONSE_TYPE, "token");
        oAuth2TokenRequestParam.put(OAuth2Utils.CLIENT_ID, SecurityConfig.APP_ID);
        oAuth2TokenRequestParam.put(OAuth2Utils.SCOPE, "read");
        oAuth2TokenRequestParam.put(OAuth2Utils.REDIRECT_URI, communikeyProperties.getSecurity().getoAuth2().getRedirectUrl());

        ResponseEntity oAuth2AccessTokenResponse = testRestTemplate.getForEntity(
                requestUrlBuilder.append("http://localhost:").append(environment.getProperty("server.port"))
                        .append("/oauth/authorize?")
                        .append(Joiner.on("&").withKeyValueSeparator("=").join(oAuth2TokenRequestParam)).toString(),
                String.class
        );
        return extractOAuth2AccessToken(oAuth2AccessTokenResponse);
    }

    private User createUser() {
        user.setEmail("user@communicode.de");
        user.setLogin("user");
        user.setFirstName("first_name");
        user.setLastName("last_name");
        user.setPassword(passwordEncoder.encode(decodedUserPassword));
        user.setActivationKey(SecurityUtils.generateRandomActivationKey());
        user.setActivated(true);
        Set<Authority> authorities = Sets.newConcurrentHashSet();
        authorities.add(authorityRepository.findOne(AuthoritiesConstants.USER));
        user.setAuthorities(authorities);
        return user;
    }

    /**
     * Extracts the OAuth2 access token from the response entity location header.
     *
     * @param responseEntity the response entity to extract the access token from
     * @return the extracted OAuth2 access token
     * @throws NullPointerException if the response entity location header is null
     */
    private String extractOAuth2AccessToken(final ResponseEntity responseEntity) {
        final String redirectUrl = requireNonNull(responseEntity.getHeaders().getLocation().toString(), "redirectUrl must not be null!");
        String keyName = OAuth2AccessToken.ACCESS_TOKEN + "=";

        final int keyIdx = redirectUrl.indexOf(keyName);
        if (keyIdx != -1) {
            final int valueIdx = redirectUrl.indexOf("&", keyIdx + keyName.length());
            if (valueIdx != -1) {
                return redirectUrl.substring(keyIdx + keyName.length(), valueIdx);
            }
        }
        return null;
    }
}
