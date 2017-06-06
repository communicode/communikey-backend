/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2017
 */
package de.communicode.communikey.api;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.CoreMatchers.containsString;

import de.communicode.communikey.IntegrationBaseTest;
import de.communicode.communikey.controller.RequestMappings;
import de.communicode.communikey.domain.User;
import de.communicode.communikey.security.AuthoritiesConstants;
import de.communicode.communikey.service.payload.UserCreationPayload;
import de.communicode.communikey.service.payload.UserPayload;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Test;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Integration tests for the {@link User} REST API.
 *
 * @author sgreb@communicode.de
 * @since 0.4.0
 */
public class UserApiIT extends IntegrationBaseTest {

    private String testUserLogin = "test";
    private String testUserEmail = testUserLogin + "@communicode.de";
    private User testUser = new User();

    @Test
    public void testActivateUserAsAdmin() {
        initializeTestUser(false);
        Response response = given()
                .auth().oauth2(adminUserOAuth2AccessToken)
                .contentType(ContentType.JSON)
                .body(new UserCreationPayload(testUser))
        .when()
                .post(RequestMappings.USERS + RequestMappings.USERS_REGISTER)
        .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract().response();

        given()
                .auth().oauth2(adminUserOAuth2AccessToken)
                .queryParam("activation_key", response.path("activationKey").toString())
        .when()
                .get(RequestMappings.USERS + RequestMappings.USERS_ACTIVATE)
        .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    public void testCreateUserAsAdminWithValidPayload() {
        initializeTestUser(true);
        given()
                .auth().oauth2(adminUserOAuth2AccessToken)
                .contentType(ContentType.JSON)
                .body(new UserCreationPayload(testUser))
        .when()
                .post(RequestMappings.USERS + RequestMappings.USERS_REGISTER)
        .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("login", equalTo(testUser.getLogin()))
                .body("email", equalTo(testUser.getEmail()))
                .body("firstName", equalTo(testUser.getFirstName()))
                .body("lastName", equalTo(testUser.getLastName()))
                .body("activated", equalTo(testUser.isActivated()));
    }

    @Test
    public void testCreateUserAsAdminWithInvalidPayload() {
        initializeTestUser(true);
        testUser.setLogin("   new__userwith123$invalid//characters-||##5   ");
        testUser.setEmail(testUser.getLogin() + "@invalid.de");

        given()
                .auth().oauth2(adminUserOAuth2AccessToken)
                .contentType(ContentType.JSON)
                .body(new UserCreationPayload(testUser))
        .when()
                .post(RequestMappings.USERS + RequestMappings.USERS_REGISTER)
        .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body(containsString("email not a well-formed email address"));
    }

    @Test
    public void testDeactivateUserAsAdmin() {
        initializeTestUser(true);
        given()
                .auth().oauth2(adminUserOAuth2AccessToken)
                .contentType(ContentType.JSON)
                .body(new UserCreationPayload(testUser))
        .when()
                .post(RequestMappings.USERS + RequestMappings.USERS_REGISTER)
        .then()
                .statusCode(HttpStatus.CREATED.value());

        given()
                .auth().oauth2(adminUserOAuth2AccessToken)
                .queryParam("login", testUser.getLogin())
        .when()
                .get(RequestMappings.USERS + RequestMappings.USERS_DEACTIVATE)
        .then()
                .statusCode(HttpStatus.NO_CONTENT.value());

        given()
                .auth().oauth2(adminUserOAuth2AccessToken)
                .pathParam("login", testUser.getLogin())
        .when()
                .get(RequestMappings.USERS + RequestMappings.USERS_LOGIN)
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("activated", equalTo(false));
    }

    @Test
    public void testDeleteUserAsAdmin() {
        initializeTestUser(true);
        given()
                .auth().oauth2(adminUserOAuth2AccessToken)
                .contentType(ContentType.JSON)
                .body(new UserCreationPayload(testUser))
        .when()
                .post(RequestMappings.USERS + RequestMappings.USERS_REGISTER)
        .then()
                .statusCode(HttpStatus.CREATED.value());

        given()
                .auth().oauth2(adminUserOAuth2AccessToken)
                .pathParam("login", testUser.getLogin())
        .when()
                .delete(RequestMappings.USERS + RequestMappings.USERS_LOGIN)
        .then()
                .statusCode(HttpStatus.NO_CONTENT.value());

        given()
                .auth().oauth2(adminUserOAuth2AccessToken)
                .pathParam("login", testUser.getLogin())
        .when()
                .delete(RequestMappings.USERS + RequestMappings.USERS_LOGIN)
        .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void testGetOneUserAsAdmin() {
        initializeTestUser(true);
        given()
                .auth().oauth2(adminUserOAuth2AccessToken)
                .contentType(ContentType.JSON)
                .body(new UserCreationPayload(testUser))
        .when()
                .post(RequestMappings.USERS + RequestMappings.USERS_REGISTER)
        .then()
                .statusCode(HttpStatus.CREATED.value());

        given()
                .auth().oauth2(adminUserOAuth2AccessToken)
                .pathParam("login", testUser.getLogin())
        .when()
                .get(RequestMappings.USERS + RequestMappings.USERS_LOGIN)
        .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    public void testGetAllUsersAsAdmin() {
        given()
                .auth().oauth2(adminUserOAuth2AccessToken)
        .when()
                .get(RequestMappings.USERS)
        .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    public void testGetAllUsersAsUserShouldBeAccessDenied() {
        given()
                .auth().oauth2(userOAuth2AccessToken)
        .when()
                .get(RequestMappings.USERS)
        .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    public void testGetPasswordResetKeyAsUser() {
        initializeTestUser(true);
        given()
                .auth().oauth2(adminUserOAuth2AccessToken)
                .contentType(ContentType.JSON)
                .body(new UserCreationPayload(testUser))
        .when()
                .post(RequestMappings.USERS + RequestMappings.USERS_REGISTER)
        .then()
                .statusCode(HttpStatus.CREATED.value());

        given()
                .auth().oauth2(userOAuth2AccessToken)
                .queryParam("email", testUser.getEmail())
        .when()
                .get(RequestMappings.USERS + RequestMappings.USERS_PASSWORD_RESET)
        .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    public void testPasswordResetAsUser() {
        initializeTestUser(true);
        Map<String, String> userPasswordResetPayload = new HashMap<>();
        userPasswordResetPayload.put("password", "newPassword");

        given()
                .auth().oauth2(adminUserOAuth2AccessToken)
                .contentType(ContentType.JSON)
                .body(new UserCreationPayload(testUser))
        .when()
                .post(RequestMappings.USERS + RequestMappings.USERS_REGISTER)
        .then()
                .statusCode(HttpStatus.CREATED.value());

        Response resetKeyResponse = given()
                .auth().oauth2(userOAuth2AccessToken)
                .queryParam("email", testUser.getEmail())
        .when()
                .get(RequestMappings.USERS + RequestMappings.USERS_PASSWORD_RESET)
        .then()
                .statusCode(HttpStatus.OK.value())
                .extract().response();

        userPasswordResetPayload.put("resetKey", resetKeyResponse.path("resetKey"));
        given()
                .auth().oauth2(userOAuth2AccessToken)
                .contentType(ContentType.JSON)
                .body(userPasswordResetPayload)
        .when()
                .post(RequestMappings.USERS + RequestMappings.USERS_PASSWORD_RESET)
        .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    public void testUpdateAsAdmin() {
        initializeTestUser(true);
        given()
                .auth().oauth2(adminUserOAuth2AccessToken)
                .contentType(ContentType.JSON)
                .body(new UserCreationPayload(testUser))
        .when()
                .post(RequestMappings.USERS + RequestMappings.USERS_REGISTER)
        .then()
                .statusCode(HttpStatus.CREATED.value());

        testUser.setEmail("newName@communicode.de");
        given()
                .auth().oauth2(adminUserOAuth2AccessToken)
                .contentType(ContentType.JSON)
                .body(new UserPayload(testUser))
                .pathParam("login", testUser.getLogin())
        .when()
                .put(RequestMappings.USERS + RequestMappings.USERS_LOGIN)
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("login", equalTo("newName".toLowerCase()))
                .body("email", equalTo(testUser.getEmail()))
                .body("activated", equalTo(false));
    }

    @Test
    public void testUpdateAuthoritiesAsAdmin() {
        initializeTestUser(true);
        Set<String> authorities = new HashSet<>();
        authorities.add(AuthoritiesConstants.ADMIN);
        authorities.add(AuthoritiesConstants.USER);

        given()
                .auth().oauth2(adminUserOAuth2AccessToken)
                .contentType(ContentType.JSON)
                .body(new UserCreationPayload(testUser))
        .when()
                .post(RequestMappings.USERS + RequestMappings.USERS_REGISTER)
        .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("authorities.name", hasItem(containsString(AuthoritiesConstants.USER)))
                .body("authorities.name", hasItem(not(containsString(AuthoritiesConstants.ADMIN))))
                .root("authorities")
                .body("size()", equalTo(1));
        String invalidatedTestUserOAuth2AccessToken = generateOAuth2AccessToken(testUser.getLogin(), testUser.getPassword());

        given()
                .auth().oauth2(adminUserOAuth2AccessToken)
                .contentType(ContentType.JSON)
                .body(authorities)
                .pathParam("login", testUser.getLogin())
        .when()
                .put(RequestMappings.USERS + RequestMappings.USER_AUTHORITIES)
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("email", equalTo(testUser.getEmail()))
                .body("activated", equalTo(true))
                .body("authorities.name", hasItem(containsString(AuthoritiesConstants.USER)))
                .body("authorities.name", hasItem(containsString(AuthoritiesConstants.ADMIN)))
                .root("authorities")
                .body("size()", equalTo(2));

        given()
                .auth().oauth2(invalidatedTestUserOAuth2AccessToken)
        .when()
                .get(RequestMappings.KEY_CATEGORIES)
        .then()
                .statusCode(HttpStatus.UNAUTHORIZED.value())
                .body(containsString("Invalid access token"));
    }

    /**
     * Initializes the test user entity.
     *
     * @param activated the activation status of the test user
     */
    private void initializeTestUser(boolean activated) {
        testUser.setLogin(testUserLogin);
        testUser.setEmail(testUserEmail);
        testUser.setFirstName(fairy.person().getFirstName());
        testUser.setLastName(fairy.person().getLastName());
        testUser.setActivated(activated);
        testUser.setPassword(passwordEncoder.encode(decodedUserPassword));
    }
}
