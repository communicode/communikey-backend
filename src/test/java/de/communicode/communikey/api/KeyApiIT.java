/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2017
 */
package de.communicode.communikey.api;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

import de.communicode.communikey.IntegrationBaseTest;
import de.communicode.communikey.controller.RequestMappings;
import de.communicode.communikey.domain.Key;
import io.restassured.http.ContentType;
import org.junit.Test;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

/**
 * Integration tests for the {@link Key} REST API.
 *
 * @author sgreb@communicode.de
 * @since 0.4.0
 */
public class KeyApiIT extends IntegrationBaseTest {

    private Map<String, String> keyPayload = new HashMap<>();

    @Test
    public void testCreateKeyAsAdminWithValidPayload() {
        initializeTestKeyPayload();
        given()
                .auth().oauth2(adminUserOAuth2AccessToken)
                .contentType(ContentType.JSON)
                .body(keyPayload)
        .when()
                .post(RequestMappings.KEYS)
        .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("category", equalTo(null));
    }

    @Test
    public void testCreateKeyAsUserWithValidPayload() {
        initializeTestKeyPayload();
        given()
                .auth().oauth2(userOAuth2AccessToken)
                .contentType(ContentType.JSON)
                .body(keyPayload)
        .when()
                .post(RequestMappings.KEYS)
        .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    public void testCreateKeyAsAdminWithInvalidPayload() {
        keyPayload.put("unknownAttribute", "invalidValue");
        given()
                .auth().oauth2(adminUserOAuth2AccessToken)
                .contentType(ContentType.JSON)
                .body(keyPayload)
        .when()
                .post(RequestMappings.KEYS)
        .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void testDeleteKeyAsAdmin() {
        initializeTestKeyPayload();
        Long createdKeyId = given()
                .auth().oauth2(adminUserOAuth2AccessToken)
                .contentType(ContentType.JSON)
                .body(keyPayload)
        .when()
                .post(RequestMappings.KEYS)
        .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract().jsonPath().getLong("id");

        given()
                .auth().oauth2(adminUserOAuth2AccessToken)
                .pathParam("keyId", createdKeyId)
        .when()
                .delete(RequestMappings.KEYS + RequestMappings.KEYS_ID)
        .then()
                .statusCode(HttpStatus.NO_CONTENT.value());

        given()
                .auth().oauth2(adminUserOAuth2AccessToken)
                .pathParam("keyId", createdKeyId)
        .when()
                .get(RequestMappings.KEYS + RequestMappings.KEYS_ID)
        .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void testDeleteKeyAsUser() {
        initializeTestKeyPayload();
        Long createdKeyId = given()
                .auth().oauth2(adminUserOAuth2AccessToken)
                .contentType(ContentType.JSON)
                .body(keyPayload)
        .when()
                .post(RequestMappings.KEYS)
        .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract().jsonPath().getLong("id");

        given()
                .auth().oauth2(userOAuth2AccessToken)
                .pathParam("keyId", createdKeyId)
        .when()
                .delete(RequestMappings.KEYS + RequestMappings.KEYS_ID)
        .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    public void testDeleteAllKeysAsAdmin() {
        initializeTestKeyPayload();
        given()
                .auth().oauth2(adminUserOAuth2AccessToken)
                .contentType(ContentType.JSON)
                .body(keyPayload)
        .when()
                .post(RequestMappings.KEYS)
        .then()
                .statusCode(HttpStatus.CREATED.value());

        given()
                .auth().oauth2(adminUserOAuth2AccessToken)
        .when()
                .delete(RequestMappings.KEYS)
        .then()
                .statusCode(HttpStatus.NO_CONTENT.value());

        given()
                .auth().oauth2(adminUserOAuth2AccessToken)
        .when()
                .get(RequestMappings.KEYS)
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("size()", equalTo(0));
    }

    @Test
    public void testDeleteAllKeysAsUser() {
        initializeTestKeyPayload();
        given()
                .auth().oauth2(adminUserOAuth2AccessToken)
                .contentType(ContentType.JSON)
                .body(keyPayload)
        .when()
                .post(RequestMappings.KEYS)
        .then()
                .statusCode(HttpStatus.CREATED.value());

        given()
                .auth().oauth2(userOAuth2AccessToken)
        .when()
                .delete(RequestMappings.KEYS)
        .then()
                .statusCode(HttpStatus.FORBIDDEN.value());

        given()
                .auth().oauth2(adminUserOAuth2AccessToken)
        .when()
                .get(RequestMappings.KEYS)
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("size()", equalTo(1));
    }

    @Test
    public void testGetKeyAsAdmin() {
        initializeTestKeyPayload();
        Long createdKeyId = given()
                .auth().oauth2(adminUserOAuth2AccessToken)
                .contentType(ContentType.JSON)
                .body(keyPayload)
        .when()
                .post(RequestMappings.KEYS)
        .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract().jsonPath().getLong("id");

        given()
                .auth().oauth2(adminUserOAuth2AccessToken)
                .pathParam("keyId", createdKeyId)
        .when()
                .get(RequestMappings.KEYS + RequestMappings.KEYS_ID)
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("id", equalTo(createdKeyId.intValue()));
    }

    @Test
    public void testGetKeyAsUser() {
        initializeTestKeyPayload();
        Long createdKeyId = given()
                .auth().oauth2(adminUserOAuth2AccessToken)
                .contentType(ContentType.JSON)
                .body(keyPayload)
        .when()
                .post(RequestMappings.KEYS)
        .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract().jsonPath().getLong("id");

        given()
                .auth().oauth2(userOAuth2AccessToken)
                .pathParam("keyId", createdKeyId)
        .when()
                .get(RequestMappings.KEYS + RequestMappings.KEYS_ID)
        .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    public void testGetAllKeysAsAdmin() {
        initializeTestKeyPayload();
        given()
                .auth().oauth2(adminUserOAuth2AccessToken)
                .contentType(ContentType.JSON)
                .body(keyPayload)
        .when()
                .post(RequestMappings.KEYS)
        .then()
                .statusCode(HttpStatus.CREATED.value());

        given()
                .auth().oauth2(adminUserOAuth2AccessToken)
        .when()
                .get(RequestMappings.KEYS)
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("size()", equalTo(1));
    }

    @Test
    public void testGetAllKeysAsUser() {
        initializeTestKeyPayload();
        given()
                .auth().oauth2(adminUserOAuth2AccessToken)
                .contentType(ContentType.JSON)
                .body(keyPayload)
        .when()
                .post(RequestMappings.KEYS)
        .then()
                .statusCode(HttpStatus.CREATED.value());

        given()
                .auth().oauth2(userOAuth2AccessToken)
        .when()
                .get(RequestMappings.KEYS)
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("size()", equalTo(0));
    }

    @Test
    public void testUpdateKeyAsAdmin() {
        initializeTestKeyPayload();
        Long createdKeyId = given()
                .auth().oauth2(adminUserOAuth2AccessToken)
                .contentType(ContentType.JSON)
                .body(keyPayload)
        .when()
                .post(RequestMappings.KEYS)
        .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract().jsonPath().getLong("id");

        keyPayload.replace("name", "newName");
        keyPayload.replace("login", "newLogin");
        keyPayload.replace("password", "newPassword");

        given()
                .auth().oauth2(adminUserOAuth2AccessToken)
                .contentType(ContentType.JSON)
                .pathParam("keyId", createdKeyId)
                .body(keyPayload)
        .when()
                .put(RequestMappings.KEYS + RequestMappings.KEYS_ID)
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("name", equalTo(keyPayload.get("name")))
                .body("login", equalTo(keyPayload.get("login")))
                .body("password", equalTo(keyPayload.get("password")));
    }

    @Test
    public void testUpdateKeyAsUser() {
        initializeTestKeyPayload();
        Long createdKeyId = given()
                .auth().oauth2(adminUserOAuth2AccessToken)
                .contentType(ContentType.JSON)
                .body(keyPayload)
        .when()
                .post(RequestMappings.KEYS)
        .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract().jsonPath().getLong("id");

        keyPayload.replace("name", "newName");
        keyPayload.replace("login", "login");
        keyPayload.replace("password", "newPassword");

        given()
                .auth().oauth2(userOAuth2AccessToken)
                .contentType(ContentType.JSON)
                .pathParam("keyId", createdKeyId)
                .body(keyPayload)
        .when()
                .put(RequestMappings.KEYS + RequestMappings.KEYS_ID)
        .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    /**
     * Initializes the test key payload.
     */
    private void initializeTestKeyPayload() {
        keyPayload.put("name", "key");
        keyPayload.put("login", "login");
        keyPayload.put("password", "password");
    }
}
