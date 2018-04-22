/*
 * This file is part of communikey.
 * Copyright (C) 2016-2018  communicode AG <communicode.de>
 *
 * communikey is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package de.communicode.communikey;

import static io.restassured.RestAssured.given;
import static java.util.Objects.requireNonNull;

import com.google.common.collect.Sets;
import de.communicode.communikey.config.CommunikeyProperties;
import de.communicode.communikey.domain.Authority;
import de.communicode.communikey.domain.User;
import de.communicode.communikey.repository.KeyCategoryRepository;
import de.communicode.communikey.repository.TagRepository;
import de.communicode.communikey.repository.UserGroupRepository;
import de.communicode.communikey.repository.UserRepository;
import de.communicode.communikey.security.AuthoritiesConstants;
import de.communicode.communikey.security.SecurityUtils;
import de.communicode.communikey.service.AuthorityService;
import de.communicode.communikey.service.KeyCategoryService;
import de.communicode.communikey.service.KeyService;
import de.communicode.communikey.repository.KeyRepository;
import de.communicode.communikey.service.TagService;
import de.communicode.communikey.service.UserGroupService;
import de.communicode.communikey.service.UserService;
import org.hashids.Hashids;
import io.codearte.jfairy.Fairy;
import io.restassured.http.ContentType;
import org.junit.After;
import org.junit.Before;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
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
    protected KeyCategoryRepository keyCategoryRepository;
    @Autowired
    protected KeyService keyService;
    @Autowired
    protected KeyRepository keyRepository;
    @Autowired
    protected UserGroupService userGroupService;
    @Autowired
    protected UserGroupRepository userGroupRepository;
    @Autowired
    protected TagService tagService;
    @Autowired
    protected TagRepository tagRepository;
    @Autowired
    protected PasswordEncoder passwordEncoder;
    @Autowired
    protected AuthorityService authorityService;
    @Autowired
    protected CommunikeyProperties communikeyProperties;
    @Autowired
    protected TestRestTemplate testRestTemplate;
    @Autowired
    protected Hashids hashIds;

    protected Fairy fairy;

    protected User user = new User();
    protected String decodedUserPassword = "password";
    protected String userLogin = "user";
    protected String userEmail = userLogin + "@communicode.de";
    protected String userPublicKey = "-----BEGIN PUBLIC KEY-----" +
                                     "VGhpcyBpcyBhIGJhc2U2NCBlbm" +
                                     "NyeXB0ZWQgcHVibGljIGtleQ==" +
                                     "-----END PUBLIC KEY-----";

    protected String userOAuth2AccessToken;
    protected String adminUserOAuth2AccessToken;

    @LocalServerPort
    protected int definedServerPort;

    @Before
    public void setup() {
        fairy = Fairy.create();
        user = userRepository.save(createUser());
        adminUserOAuth2AccessToken = generateOAuth2AccessToken(
                communikeyProperties.getSecurity().getRoot().getLogin(),
                communikeyProperties.getSecurity().getRoot().getPassword());
        userOAuth2AccessToken = generateOAuth2AccessToken(userLogin, decodedUserPassword);
    }

    @After
    public void cleanUp() {
        userGroupService.deleteAll();
        keyService.deleteAll();
        keyCategoryService.deleteAll();
        userRepository.findAll().stream()
                .filter(testUser -> !testUser.getLogin().equals(communikeyProperties.getSecurity().getRoot().getLogin()))
                .forEach(nonRootUser -> userService.delete(nonRootUser.getLogin()));
        tagService.deleteAll();
    }

    /**
     * Generates a OAuth2 access token for the user with the specified login- and password.
     *
     * @param login the login of the user to generate the OAuth2 access token for
     * @param password the password of the user
     * @return the redirect URL which contains the access token as parameter
     * @see <a href="https://tools.ietf.org/html/rfc6749#section-1.3.2"></a>RFC6749 - The OAuth 2.0 Authorization Framework</a>
     * @see <a href="http://oauthlib.readthedocs.io/en/latest/oauth2/grants/implicit.html"></a>oauthlib - Read The Docs</a>
     */
    protected String generateOAuth2AccessToken(String login, String password) {
        Map<String, String> oAuth2TokenRequestParam = new HashMap<>();
        oAuth2TokenRequestParam.put("login", login);
        oAuth2TokenRequestParam.put("password", password);

        String url = "http://localhost:" + definedServerPort + "/api?authorize";

        return given()
                .contentType(ContentType.JSON)
                .body(oAuth2TokenRequestParam)
        .when()
                .post(url)
        .then()
                .extract().response().getBody().jsonPath().get(OAuth2AccessToken.ACCESS_TOKEN);
    }

    private User createUser() {
        user.setEmail(userEmail);
        user.setLogin(userLogin);
        user.setFirstName(fairy.person().getFirstName());
        user.setLastName(fairy.person().getLastName());
        user.setPassword(passwordEncoder.encode(decodedUserPassword));
        user.setActivationToken(SecurityUtils.generateRandomActivationToken());
        user.setPublicKey(userPublicKey);
        user.setPublicKeyResetToken(null);
        user.setActivated(true);
        Set<Authority> authorities = Sets.newConcurrentHashSet();
        authorities.add(authorityService.get(AuthoritiesConstants.USER));
        user.addAuthorities(authorities);
        return user;
    }

    /**
     * Extracts the OAuth2 access token from a response entity location header redirect URL.
     *
     * @param redirectUrl the URL to extract the access token from
     * @return the extracted OAuth2 access token
     * @throws NullPointerException if the response entity location header is null
     */
    private String extractOAuth2AccessTokenFromRedirectUrl(final String redirectUrl) {
        requireNonNull(redirectUrl, "redirectUrl must not be null!");
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
