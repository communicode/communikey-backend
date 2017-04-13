/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2017
 */
package de.communicode.communikey.api;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import de.communicode.communikey.IntegrationBaseTest;
import de.communicode.communikey.controller.RequestMappings;
import de.communicode.communikey.domain.KeyCategory;
import de.communicode.communikey.domain.UserGroup;
import io.restassured.http.ContentType;
import org.junit.Test;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

/**
 * Integration tests for the {@link KeyCategory} REST API.
 *
 * @author sgreb@communicode.de
 * @since 0.4.0
 */
public class KeyCategoryApiIT extends IntegrationBaseTest {

    private Map<String, String> keyCategoryPayload = new HashMap<>();
    private KeyCategory keyCategory = new KeyCategory();

    @Test
    public void testAddChildAsAdmin() {
        initializeTestKeyCategoryPayload();
        Long createdParentKeyCategoryId = given()
                .auth().oauth2(adminUserOAuth2AccessToken)
                .contentType(ContentType.JSON)
                .body(keyCategoryPayload)
        .when()
                .post(RequestMappings.KEY_CATEGORIES)
        .then()
                .extract().body().as(KeyCategory.class).getId();

        keyCategoryPayload.replace("name", fairy.textProducer().word(1));
        Long createdChildKeyCategoryId = given()
                .auth().oauth2(adminUserOAuth2AccessToken)
                .contentType(ContentType.JSON)
                .body(keyCategoryPayload)
        .when()
                .post(RequestMappings.KEY_CATEGORIES)
                .then()
                .extract().body().as(KeyCategory.class).getId();

        given()
                .auth().oauth2(adminUserOAuth2AccessToken)
                .pathParam("keyCategoryId", createdParentKeyCategoryId)
                .param("childKeyCategoryId", createdChildKeyCategoryId)
        .when()
                .get(RequestMappings.KEY_CATEGORIES + RequestMappings.KEY_CATEGORY_CHILDREN)
        .then()
                .statusCode(HttpStatus.OK.value())
                .root("children")
                .body("size()", equalTo(1));
    }

    @Test
    public void testAddChildAsUser() {
        initializeTestKeyCategoryPayload();
        Long createdParentKeyCategoryId = given()
                .auth().oauth2(adminUserOAuth2AccessToken)
                .contentType(ContentType.JSON)
                .body(keyCategoryPayload)
        .when()
                .post(RequestMappings.KEY_CATEGORIES)
        .then()
                .extract().body().as(KeyCategory.class).getId();

        keyCategoryPayload.replace("name", fairy.textProducer().word(1));
        Long createdChildKeyCategoryId = given()
                .auth().oauth2(adminUserOAuth2AccessToken)
                .contentType(ContentType.JSON)
                .body(keyCategoryPayload)
        .when()
                .post(RequestMappings.KEY_CATEGORIES)
        .then()
                .extract().body().as(KeyCategory.class).getId();

        given()
                .auth().oauth2(userOAuth2AccessToken)
                .pathParam("keyCategoryId", createdParentKeyCategoryId)
                .param("childKeyCategoryId", createdChildKeyCategoryId)
        .when()
                .get(RequestMappings.KEY_CATEGORIES + RequestMappings.KEY_CATEGORY_CHILDREN)
        .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    public void testCreateAsAdmin() {
        initializeTestKeyCategoryPayload();
        given()
                .auth().oauth2(adminUserOAuth2AccessToken)
                .contentType(ContentType.JSON)
                .body(keyCategoryPayload)
        .when()
                .post(RequestMappings.KEY_CATEGORIES)
        .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("name", equalTo(keyCategoryPayload.get("name")));
    }

    @Test
    public void testCreateAsUser() {
        initializeTestKeyCategoryPayload();
        given()
                .auth().oauth2(userOAuth2AccessToken)
                .contentType(ContentType.JSON)
                .body(keyCategoryPayload)
        .when()
                .post(RequestMappings.KEY_CATEGORIES)
        .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    public void testAddGroupAsAdmin() {
        initializeTestKeyCategoryPayload();

        Long createdKeyCategoryId = given()
                .auth().oauth2(adminUserOAuth2AccessToken)
                .contentType(ContentType.JSON)
                .body(keyCategoryPayload)
        .when()
                .post(RequestMappings.KEY_CATEGORIES)
        .then()
                .extract().body().as(KeyCategory.class).getId();

        Map<String, String> userGroupPayload = new HashMap<>();
        userGroupPayload.put("name", fairy.textProducer().word(1));
        String createdUserGroupName = given()
                .auth().oauth2(adminUserOAuth2AccessToken)
                .contentType(ContentType.JSON)
                .body(userGroupPayload)
        .when()
                .post(RequestMappings.USER_GROUPS)
        .then()
                .extract().body().as(UserGroup.class).getName();

        given()
                .auth().oauth2(adminUserOAuth2AccessToken)
                .pathParam("keyCategoryId", createdKeyCategoryId)
                .param("userGroupName", createdUserGroupName)
        .when()
                .get(RequestMappings.KEY_CATEGORIES + RequestMappings.KEY_CATEGORY_GROUPS)
        .then()
                .statusCode(HttpStatus.OK.value())
                .root("groups")
                .body("size()", equalTo(1));

        given()
                .auth().oauth2(adminUserOAuth2AccessToken)
                .pathParam("userGroupName", createdUserGroupName)
        .when()
                .get(RequestMappings.USER_GROUPS + RequestMappings.USER_GROUPS_NAME)
        .then()
                .root("categories")
                .body("size()", equalTo(1));
    }

    /**
     * Initializes the test key category payload.
     */
    private void initializeTestKeyCategoryPayload() {
        keyCategoryPayload.put("name", fairy.textProducer().word(1));
    }

    /**
     * Initializes the test key category.
     */
    private void initializeTestKeyCategory() {
        keyCategory.setName(fairy.textProducer().word(1));
    }
}
