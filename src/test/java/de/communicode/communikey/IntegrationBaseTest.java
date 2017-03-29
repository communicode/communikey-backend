/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2017
 */
package de.communicode.communikey;

import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;

import com.google.common.base.Joiner;
import com.google.common.collect.Sets;
import de.communicode.communikey.config.CommunikeyProperties;
import de.communicode.communikey.config.SecurityConfig;
import de.communicode.communikey.domain.Authority;
import de.communicode.communikey.domain.User;
import de.communicode.communikey.exception.UserNotFoundException;
import de.communicode.communikey.repository.AuthorityRepository;
import de.communicode.communikey.repository.KeyCategoryRepository;
import de.communicode.communikey.repository.UserRepository;
import de.communicode.communikey.security.AuthoritiesConstants;
import de.communicode.communikey.security.SecurityUtils;
import de.communicode.communikey.service.AuthorityService;
import de.communicode.communikey.service.KeyCategoryService;
import de.communicode.communikey.service.KeyService;
import de.communicode.communikey.service.UserGroupService;
import de.communicode.communikey.service.UserService;
import org.junit.After;
import org.junit.Before;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.util.OAuth2Utils;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
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
    protected UserRepository userRepository;
    @Autowired
    protected UserService userService;
    @Autowired
    protected KeyCategoryService keyCategoryService;
    @Autowired
    protected KeyService keyService;
    @Autowired
    protected UserGroupService userGroupService;
    @Autowired
    protected PasswordEncoder passwordEncoder;
    @Autowired
    protected AuthorityService authorityService;
    @Autowired
    protected JdbcTokenStore jdbcTokenStore;
    @Autowired
    protected CommunikeyProperties communikeyProperties;
    @Autowired
    protected TestRestTemplate testRestTemplate;
    protected String decodedUserPassword = "password";
    protected String userLogin = "user";
    private String userEmail = userLogin + "@communicode.de";
    protected User user = new User();
    protected String userOAuth2AccessToken;
    protected String adminUserOAuth2AccessToken;
    @LocalServerPort
    protected int definedServerPort;

    @Before
    public void setup() {
        user = userRepository.save(createUser());
        adminUserOAuth2AccessToken = generateUserAccessToken(
                communikeyProperties.getSecurity().getRoot().getLogin(),
                communikeyProperties.getSecurity().getRoot().getPassword());
        userOAuth2AccessToken = generateUserAccessToken(userLogin, decodedUserPassword);
    }

    @After
    public void cleanUp() {
        userRepository.findAll().stream()
                .filter(testUser -> !testUser.getLogin().equals(communikeyProperties.getSecurity().getRoot().getLogin()))
                .forEach(nonRootUser -> {
                    jdbcTokenStore.findTokensByUserName(nonRootUser.getLogin()).forEach(accessToken -> jdbcTokenStore.removeAccessToken(accessToken));
                    userService.delete(nonRootUser.getLogin());
                });
        keyCategoryService.deleteAll();
        keyService.deleteAll();
        userGroupService.deleteAll();
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
                requestUrlBuilder.append("http://localhost:").append(definedServerPort)
                        .append("/oauth/authorize?")
                        .append(Joiner.on("&").withKeyValueSeparator("=").join(oAuth2TokenRequestParam)).toString(),
                String.class
        );
        return extractOAuth2AccessToken(oAuth2AccessTokenResponse);
    }

    private User createUser() {
        user.setEmail(userEmail);
        user.setLogin(userLogin);
        user.setFirstName("first_name");
        user.setLastName("last_name");
        user.setPassword(passwordEncoder.encode(decodedUserPassword));
        user.setActivationKey(SecurityUtils.generateRandomActivationKey());
        user.setActivated(true);
        Set<Authority> authorities = Sets.newConcurrentHashSet();
        authorities.add(authorityService.get(AuthoritiesConstants.USER));
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
