/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2017
 */
package de.communicode.communikey.api;

import static de.communicode.communikey.CommunikeyApplication.COMMUNIKEY_REST_API_VERSION;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

import de.communicode.communikey.IntegrationBaseTest;
import de.communicode.communikey.controller.RequestMappings;
import de.communicode.communikey.controller.RequestParameter;
import org.junit.Test;
import org.springframework.http.HttpStatus;

/**
 * Integration tests for the REST API root endpoints.
 *
 * @author sgreb@communicode.de
 * @since 0.4.0
 */
public class ApiRootIT extends IntegrationBaseTest {

    @Test
    public void testGetVersionAsAdmin() {
        given()
                .auth().oauth2(adminUserOAuth2AccessToken)
                .param(RequestParameter.API_VERSION)
        .when()
                .get(RequestMappings.API)
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("version", equalTo(COMMUNIKEY_REST_API_VERSION));
    }

    @Test
    public void testGetVersionAsUser() {
        given()
                .auth().oauth2(userOAuth2AccessToken)
                .param(RequestParameter.API_VERSION)
        .when()
                .get(RequestMappings.API)
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("version", equalTo(COMMUNIKEY_REST_API_VERSION));
    }

    @Test
    public void testIsPrivilegedAsAdmin() {
        given()
                .auth().oauth2(adminUserOAuth2AccessToken)
                .param(RequestParameter.API_PRIVILEGED)
        .when()
                .get(RequestMappings.API)
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("privileged", equalTo(true));
    }

    @Test
    public void testIsPrivilegedAsUser() {
        given()
                .auth().oauth2(userOAuth2AccessToken)
                .param(RequestParameter.API_PRIVILEGED)
        .when()
                .get(RequestMappings.API)
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("privileged", equalTo(false));
    }
}
