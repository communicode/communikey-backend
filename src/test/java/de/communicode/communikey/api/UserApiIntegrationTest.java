/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2017
 */
package de.communicode.communikey.api;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.CoreMatchers.containsString;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.communicode.communikey.IntegrationBaseTest;
import de.communicode.communikey.controller.RequestMappings;
import de.communicode.communikey.domain.User;
import de.communicode.communikey.service.payload.UserPayload;
import io.restassured.http.ContentType;
import org.junit.Test;
import org.springframework.http.HttpStatus;


public class UserApiIntegrationTest extends IntegrationBaseTest {

    @Test
    public void testCreateUserAsAdminWithValidPayload() throws JsonProcessingException {
        User newUser = new User();
        newUser.setLogin("newuser");
        newUser.setEmail(newUser.getLogin() + "@communicode.de");
        newUser.setFirstName("firstname");
        newUser.setLastName("lastname");
        newUser.setActivated(true);
        newUser.setPassword(passwordEncoder.encode(decodedUserPassword));

        given()
                .auth().oauth2(adminUserOAuth2AccessToken)
                .contentType(ContentType.JSON)
                .body(new UserPayload(newUser))
        .when()
                .post(RequestMappings.USERS + RequestMappings.USERS_REGISTER)
        .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("login", equalTo(newUser.getLogin()))
                .body("email", equalTo(newUser.getEmail()))
                .body("firstName", equalTo(newUser.getFirstName()))
                .body("lastName", equalTo(newUser.getLastName()))
                .body("activated", equalTo(newUser.isActivated()));
    }

    @Test
    public void testCreateUserAsAdminWithInvalidPayload() throws JsonProcessingException {
        User newUser = new User();
        newUser.setLogin("   new__userwith123$invalid//characters-||##5   ");
        newUser.setEmail(newUser.getLogin() + "@invalid.de");
        newUser.setFirstName("firstname");
        newUser.setLastName("lastname");
        newUser.setActivated(true);
        newUser.setPassword(passwordEncoder.encode(decodedUserPassword));

        given()
                .auth().oauth2(adminUserOAuth2AccessToken)
                .contentType(ContentType.JSON)
                .body(new UserPayload(newUser))
        .when()
                .post(RequestMappings.USERS + RequestMappings.USERS_REGISTER)
        .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body(containsString("email not a well-formed email address"));
    }

    @Test
    public void testActivateUserAsAdmin() throws JsonProcessingException {
        User newUser = new User();
        newUser.setLogin("newuser");
        newUser.setEmail(newUser.getLogin() + "@communicode.de");
        newUser.setFirstName("firstname");
        newUser.setLastName("lastname");
        newUser.setActivated(false);
        newUser.setPassword(passwordEncoder.encode(decodedUserPassword));

        given()
                .auth().oauth2(adminUserOAuth2AccessToken)
                .contentType(ContentType.JSON)
                .body(new UserPayload(newUser))
        .when()
                .post(RequestMappings.USERS + RequestMappings.USERS_REGISTER)
        .then()
                .statusCode(HttpStatus.CREATED.value())
                .body(containsString("email not a well-formed email address"));

        given()
                .auth().oauth2(adminUserOAuth2AccessToken)
                .contentType(ContentType.JSON)
                .queryParam("activation_key", newUser.getActivationKey())
        .when()
                .post(RequestMappings.USERS + RequestMappings.USERS_ACTIVATE)
        .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
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
}
