/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2017
 */
package de.communicode.communikey.api;

import static de.communicode.communikey.controller.PathVariables.KEY_ID;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

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
    private Map<String, String> userGroupPayload = new HashMap<>();
    private Map<String, String> keyPayload = new HashMap<>();

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
                .extract().jsonPath().getLong("id");

        keyCategoryPayload.replace("name", fairy.textProducer().word(1));
        Long createdChildKeyCategoryId = given()
                .auth().oauth2(adminUserOAuth2AccessToken)
                .contentType(ContentType.JSON)
                .body(keyCategoryPayload)
        .when()
                .post(RequestMappings.KEY_CATEGORIES)
                .then()
                .extract().jsonPath().getLong("id");

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
                .extract().jsonPath().getLong("id");

        keyCategoryPayload.replace("name", fairy.textProducer().word(1));
        Long createdChildKeyCategoryId = given()
                .auth().oauth2(adminUserOAuth2AccessToken)
                .contentType(ContentType.JSON)
                .body(keyCategoryPayload)
        .when()
                .post(RequestMappings.KEY_CATEGORIES)
        .then()
                .extract().jsonPath().getLong("id");

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
    public void testAddGroupAsAdmin() {
        initializeTestKeyCategoryPayload();
        initializeTestUserGroupPayload();

        Long createdKeyCategoryId = given()
                .auth().oauth2(adminUserOAuth2AccessToken)
                .contentType(ContentType.JSON)
                .body(keyCategoryPayload)
        .when()
                .post(RequestMappings.KEY_CATEGORIES)
        .then()
                .extract().jsonPath().getLong("id");

        Long createdUserGroupId = given()
                .auth().oauth2(adminUserOAuth2AccessToken)
                .contentType(ContentType.JSON)
                .body(userGroupPayload)
        .when()
                .post(RequestMappings.USER_GROUPS)
        .then()
                .extract().body().as(UserGroup.class).getId();

        given()
                .auth().oauth2(adminUserOAuth2AccessToken)
                .pathParam("keyCategoryId", createdKeyCategoryId)
                .param("userGroupId", createdUserGroupId)
        .when()
                .get(RequestMappings.KEY_CATEGORIES + RequestMappings.KEY_CATEGORY_GROUPS)
        .then()
                .statusCode(HttpStatus.OK.value())
                .root("groups")
                .body("size()", equalTo(1));

        given()
                .auth().oauth2(adminUserOAuth2AccessToken)
                .pathParam("userGroupId", createdUserGroupId)
        .when()
                .get(RequestMappings.USER_GROUPS + RequestMappings.USER_GROUPS_ID)
        .then()
                .root("categories")
                .body("size()", equalTo(1));
    }

    @Test
    public void testAddGroupAsUser() {
        initializeTestKeyCategoryPayload();
        initializeTestUserGroupPayload();

        Long createdKeyCategoryId = given()
                .auth().oauth2(adminUserOAuth2AccessToken)
                .contentType(ContentType.JSON)
                .body(keyCategoryPayload)
        .when()
                .post(RequestMappings.KEY_CATEGORIES)
        .then()
                .extract().jsonPath().getLong("id");

        Long createdUserGroupId = given()
                .auth().oauth2(adminUserOAuth2AccessToken)
                .contentType(ContentType.JSON)
                .body(userGroupPayload)
        .when()
                .post(RequestMappings.USER_GROUPS)
        .then()
                .extract().body().as(UserGroup.class).getId();

        given()
                .auth().oauth2(userOAuth2AccessToken)
                .pathParam("keyCategoryId", createdKeyCategoryId)
                .param("userGroupId", createdUserGroupId)
        .when()
                .get(RequestMappings.KEY_CATEGORIES + RequestMappings.KEY_CATEGORY_GROUPS)
        .then()
                .statusCode(HttpStatus.FORBIDDEN.value());

        given()
                .auth().oauth2(adminUserOAuth2AccessToken)
                .pathParam("userGroupId", createdUserGroupId)
        .when()
                .get(RequestMappings.USER_GROUPS + RequestMappings.USER_GROUPS_ID)
        .then()
                .root("categories")
                .body("size()", equalTo(0));
    }

    @Test
    public void testAddKeyAsAdmin() {
        initializeTestKeyCategoryPayload();
        initializeTestKeyPayload();

        Long createdKeyCategoryId = given()
                .auth().oauth2(adminUserOAuth2AccessToken)
                .contentType(ContentType.JSON)
                .body(keyCategoryPayload)
        .when()
                .post(RequestMappings.KEY_CATEGORIES)
        .then()
                .extract().jsonPath().getLong("id");

        String createdKeyHashid = given()
                .auth().oauth2(adminUserOAuth2AccessToken)
                .contentType(ContentType.JSON)
                .body(keyPayload)
        .when()
                .post(RequestMappings.KEYS)
        .then()
                .extract().jsonPath().getString("id");

        given()
                .auth().oauth2(adminUserOAuth2AccessToken)
                .pathParam("keyCategoryId", createdKeyCategoryId)
                .param(KEY_ID, createdKeyHashid)
        .when()
                .get(RequestMappings.KEY_CATEGORIES + RequestMappings.KEY_CATEGORY_KEYS)
        .then()
                .statusCode(HttpStatus.OK.value())
                .root("keys")
                .body("size()", equalTo(1));

        given()
                .auth().oauth2(adminUserOAuth2AccessToken)
                .pathParam(KEY_ID, createdKeyHashid)
        .when()
                .get(RequestMappings.KEYS + RequestMappings.KEY_HASHID)
        .then()
                .body("category", notNullValue())
                .body("category", equalTo(createdKeyCategoryId.intValue()));
    }

    @Test
    public void testAddKeyAsUser() {
        initializeTestKeyCategoryPayload();

        Long createdKeyCategoryId = given()
                .auth().oauth2(adminUserOAuth2AccessToken)
                .contentType(ContentType.JSON)
                .body(keyCategoryPayload)
        .when()
                .post(RequestMappings.KEY_CATEGORIES)
        .then()
                .extract().jsonPath().getLong("id");

        Map<String, String> keyPayload = new HashMap<>();
        keyPayload.put("name", fairy.textProducer().word(1));
        keyPayload.put("login", fairy.person().getEmail());
        keyPayload.put("password", fairy.person().getPassword());
        String createdKeyHashid = given()
                .auth().oauth2(adminUserOAuth2AccessToken)
                .contentType(ContentType.JSON)
                .body(keyPayload)
        .when()
                .post(RequestMappings.KEYS)
        .then()
                .extract().jsonPath().getString("id");

        given()
                .auth().oauth2(userOAuth2AccessToken)
                .pathParam("keyCategoryId", createdKeyCategoryId)
                .param(KEY_ID, createdKeyHashid)
        .when()
                .get(RequestMappings.KEY_CATEGORIES + RequestMappings.KEY_CATEGORY_KEYS)
        .then()
                .statusCode(HttpStatus.FORBIDDEN.value());

        given()
                .auth().oauth2(adminUserOAuth2AccessToken)
                .pathParam("keyCategoryId", createdKeyCategoryId)
        .when()
                .get(RequestMappings.KEY_CATEGORIES + RequestMappings.KEY_CATEGORIES_ID)
        .then()
                .root("keys")
                .body("size()", equalTo(0));

        given()
                .auth().oauth2(adminUserOAuth2AccessToken)
                .pathParam(KEY_ID, createdKeyHashid)
        .when()
                .get(RequestMappings.KEYS + RequestMappings.KEY_HASHID)
        .then()
                .body("category", nullValue());
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
    public void testDeleteAsAdmin() {
        initializeTestKeyCategoryPayload();

        Long createdKeyCategoryId = given()
                .auth().oauth2(adminUserOAuth2AccessToken)
                .contentType(ContentType.JSON)
                .body(keyCategoryPayload)
        .when()
                .post(RequestMappings.KEY_CATEGORIES)
        .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract().jsonPath().getLong("id");

        given()
                .auth().oauth2(adminUserOAuth2AccessToken)
                .pathParam("keyCategoryId", createdKeyCategoryId)
        .when()
                .delete(RequestMappings.KEY_CATEGORIES + RequestMappings.KEY_CATEGORIES_ID)
        .then()
                .statusCode(HttpStatus.NO_CONTENT.value());

        given()
                .auth().oauth2(adminUserOAuth2AccessToken)
                .pathParam("keyCategoryId", createdKeyCategoryId)
        .when()
                .get(RequestMappings.KEY_CATEGORIES + RequestMappings.KEY_CATEGORIES_ID)
        .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void testDeleteAsUser() {
        initializeTestKeyCategoryPayload();

        Long createdKeyCategoryId = given()
                .auth().oauth2(adminUserOAuth2AccessToken)
                .contentType(ContentType.JSON)
                .body(keyCategoryPayload)
        .when()
                .post(RequestMappings.KEY_CATEGORIES)
        .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract().jsonPath().getLong("id");

        given()
                .auth().oauth2(userOAuth2AccessToken)
                .pathParam("keyCategoryId", createdKeyCategoryId)
        .when()
                .delete(RequestMappings.KEY_CATEGORIES + RequestMappings.KEY_CATEGORIES_ID)
        .then()
                .statusCode(HttpStatus.FORBIDDEN.value());

        given()
                .auth().oauth2(adminUserOAuth2AccessToken)
                .pathParam("keyCategoryId", createdKeyCategoryId)
        .when()
                .get(RequestMappings.KEY_CATEGORIES + RequestMappings.KEY_CATEGORIES_ID)
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("id", equalTo(createdKeyCategoryId.intValue()));
    }

    @Test
    public void testDeleteAllAsAdmin() {
        initializeTestKeyCategoryPayload();

        given()
                .auth().oauth2(adminUserOAuth2AccessToken)
                .contentType(ContentType.JSON)
                .body(keyCategoryPayload)
        .when()
                .post(RequestMappings.KEY_CATEGORIES)
        .then()
                .statusCode(HttpStatus.CREATED.value());

        given()
                .auth().oauth2(adminUserOAuth2AccessToken)
        .when()
                .delete(RequestMappings.KEY_CATEGORIES)
        .then()
                .statusCode(HttpStatus.NO_CONTENT.value());

        given()
                .auth().oauth2(adminUserOAuth2AccessToken)
        .when()
                .get(RequestMappings.KEY_CATEGORIES)
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("size()", equalTo(0));
    }

    @Test
    public void testDeleteAllAsUser() {
        initializeTestKeyCategoryPayload();

        given()
                .auth().oauth2(adminUserOAuth2AccessToken)
                .contentType(ContentType.JSON)
                .body(keyCategoryPayload)
        .when()
                .post(RequestMappings.KEY_CATEGORIES)
        .then()
                .statusCode(HttpStatus.CREATED.value());

        given()
                .auth().oauth2(userOAuth2AccessToken)
        .when()
                .delete(RequestMappings.KEY_CATEGORIES)
        .then()
                .statusCode(HttpStatus.FORBIDDEN.value());

        given()
                .auth().oauth2(adminUserOAuth2AccessToken)
        .when()
                .get(RequestMappings.KEY_CATEGORIES)
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("size()", equalTo(1));
    }

    @Test
    public void testGetAsAdmin() {
        initializeTestKeyCategoryPayload();

        Long createdKeyCategoryId = given()
                .auth().oauth2(adminUserOAuth2AccessToken)
                .contentType(ContentType.JSON)
                .body(keyCategoryPayload)
        .when()
                .post(RequestMappings.KEY_CATEGORIES)
        .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract().jsonPath().getLong("id");

        given()
                .auth().oauth2(adminUserOAuth2AccessToken)
                .pathParam("keyCategoryId", createdKeyCategoryId)
        .when()
                .get(RequestMappings.KEY_CATEGORIES + RequestMappings.KEY_CATEGORIES_ID)
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("id", equalTo(createdKeyCategoryId.intValue()));
    }

    @Test
    public void testGetAsUser() {
        initializeTestKeyCategoryPayload();

        Long createdKeyCategoryId = given()
                .auth().oauth2(adminUserOAuth2AccessToken)
                .contentType(ContentType.JSON)
                .body(keyCategoryPayload)
        .when()
                .post(RequestMappings.KEY_CATEGORIES)
        .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract().jsonPath().getLong("id");

        given()
                .auth().oauth2(userOAuth2AccessToken)
                .pathParam("keyCategoryId", createdKeyCategoryId)
        .when()
                .get(RequestMappings.KEY_CATEGORIES + RequestMappings.KEY_CATEGORIES_ID)
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("id", equalTo(createdKeyCategoryId.intValue()));
    }

    @Test
    public void testGetAllAsAdmin() {
        initializeTestKeyCategoryPayload();

        given()
                .auth().oauth2(adminUserOAuth2AccessToken)
                .contentType(ContentType.JSON)
                .body(keyCategoryPayload)
        .when()
                .post(RequestMappings.KEY_CATEGORIES);

        given()
                .auth().oauth2(adminUserOAuth2AccessToken)
        .when()
                .get(RequestMappings.KEY_CATEGORIES)
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("size()", equalTo(1));
    }

    @Test
    public void testGetAllAsUserWithAssignedGroup() {
        initializeTestKeyCategoryPayload();
        initializeTestUserGroupPayload();

        Long createdKeyCategoryId = given()
                .auth().oauth2(adminUserOAuth2AccessToken)
                .contentType(ContentType.JSON)
                .body(keyCategoryPayload)
        .when()
                .post(RequestMappings.KEY_CATEGORIES)
        .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract().jsonPath().getLong("id");

        Long createdUserGroupId = given()
                .auth().oauth2(adminUserOAuth2AccessToken)
                .contentType(ContentType.JSON)
                .body(userGroupPayload)
        .when()
                .post(RequestMappings.USER_GROUPS)
        .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract().body().as(UserGroup.class).getId();

        given()
                .auth().oauth2(adminUserOAuth2AccessToken)
                .pathParam("keyCategoryId", createdKeyCategoryId)
                .param("userGroupId", createdUserGroupId)
        .when()
                .get(RequestMappings.KEY_CATEGORIES + RequestMappings.KEY_CATEGORY_GROUPS);

        given()
                .auth().oauth2(adminUserOAuth2AccessToken)
                .pathParam("userGroupId", createdUserGroupId)
                .param("login", userLogin)
        .when()
                .get(RequestMappings.USER_GROUPS + RequestMappings.USER_GROUPS_USERS);

        given()
                .auth().oauth2(userOAuth2AccessToken)
        .when()
                .get(RequestMappings.KEY_CATEGORIES)
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("size()", equalTo(1));
    }

    @Test
    public void testGetAllAsUserWithoutAssignedGroup() {
        initializeTestKeyCategoryPayload();
        initializeTestUserGroupPayload();

        Long createdKeyCategoryId = given()
                .auth().oauth2(adminUserOAuth2AccessToken)
                .contentType(ContentType.JSON)
                .body(keyCategoryPayload)
        .when()
                .post(RequestMappings.KEY_CATEGORIES)
        .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract().jsonPath().getLong("id");

        Long createdUserGroupId = given()
                .auth().oauth2(adminUserOAuth2AccessToken)
                .contentType(ContentType.JSON)
                .body(userGroupPayload)
        .when()
                .post(RequestMappings.USER_GROUPS)
        .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract().body().as(UserGroup.class).getId();

        given()
                .auth().oauth2(adminUserOAuth2AccessToken)
                .pathParam("keyCategoryId", createdKeyCategoryId)
                .param("userGroupId", createdUserGroupId)
        .when()
                .get(RequestMappings.KEY_CATEGORIES + RequestMappings.KEY_CATEGORY_GROUPS);

        given()
                .auth().oauth2(userOAuth2AccessToken)
        .when()
                .get(RequestMappings.KEY_CATEGORIES)
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("size()", equalTo(1));
    }

    @Test
    public void testRemoveGroupAsAdmin() {
        initializeTestKeyCategoryPayload();
        initializeTestUserGroupPayload();

        Long createdKeyCategoryId = given()
                .auth().oauth2(adminUserOAuth2AccessToken)
                .contentType(ContentType.JSON)
                .body(keyCategoryPayload)
        .when()
                .post(RequestMappings.KEY_CATEGORIES)
        .then()
                .extract().jsonPath().getLong("id");

        Long createdUserGroupId = given()
                .auth().oauth2(adminUserOAuth2AccessToken)
                .contentType(ContentType.JSON)
                .body(userGroupPayload)
        .when()
                .post(RequestMappings.USER_GROUPS)
        .then()
                .extract().body().as(UserGroup.class).getId();

        given()
                .auth().oauth2(adminUserOAuth2AccessToken)
                .pathParam("keyCategoryId", createdKeyCategoryId)
                .param("userGroupId", createdUserGroupId)
        .when()
                .get(RequestMappings.KEY_CATEGORIES + RequestMappings.KEY_CATEGORY_GROUPS)
        .then()
                .statusCode(HttpStatus.OK.value());

        given()
                .auth().oauth2(adminUserOAuth2AccessToken)
                .pathParam("keyCategoryId", createdKeyCategoryId)
                .param("userGroupId", createdUserGroupId)
        .when()
                .delete(RequestMappings.KEY_CATEGORIES + RequestMappings.KEY_CATEGORY_GROUPS)
        .then()
                .statusCode(HttpStatus.OK.value())
                .root("groups")
                .body("size()", equalTo(0));
    }

    @Test
    public void testRemoveGroupAsUser() {
        initializeTestKeyCategoryPayload();
        initializeTestUserGroupPayload();

        Long createdKeyCategoryId = given()
                .auth().oauth2(adminUserOAuth2AccessToken)
                .contentType(ContentType.JSON)
                .body(keyCategoryPayload)
        .when()
                .post(RequestMappings.KEY_CATEGORIES)
        .then()
                .extract().jsonPath().getLong("id");

        Long createdUserGroupId = given()
                .auth().oauth2(adminUserOAuth2AccessToken)
                .contentType(ContentType.JSON)
                .body(userGroupPayload)
        .when()
                .post(RequestMappings.USER_GROUPS)
        .then()
                .extract().body().as(UserGroup.class).getId();

        given()
                .auth().oauth2(adminUserOAuth2AccessToken)
                .pathParam("keyCategoryId", createdKeyCategoryId)
                .param("userGroupId", createdUserGroupId)
        .when()
                .get(RequestMappings.KEY_CATEGORIES + RequestMappings.KEY_CATEGORY_GROUPS)
        .then()
                .statusCode(HttpStatus.OK.value());

        given()
                .auth().oauth2(userOAuth2AccessToken)
                .pathParam("keyCategoryId", createdKeyCategoryId)
                .param("userGroupId", createdUserGroupId)
        .when()
                .delete(RequestMappings.KEY_CATEGORIES + RequestMappings.KEY_CATEGORY_GROUPS)
        .then()
                .statusCode(HttpStatus.FORBIDDEN.value());

        given()
                .auth().oauth2(adminUserOAuth2AccessToken)
                .pathParam("keyCategoryId", createdKeyCategoryId)
        .when()
                .get(RequestMappings.KEY_CATEGORIES + RequestMappings.KEY_CATEGORIES_ID)
        .then()
                .statusCode(HttpStatus.OK.value())
                .root("groups")
                .body("size()", equalTo(1));
    }

    @Test
    public void testRemoveKeyAsAdmin() {
        initializeTestKeyCategoryPayload();
        initializeTestKeyPayload();

        Long createdKeyCategoryId = given()
                .auth().oauth2(adminUserOAuth2AccessToken)
                .contentType(ContentType.JSON)
                .body(keyCategoryPayload)
        .when()
                .post(RequestMappings.KEY_CATEGORIES)
        .then()
                .extract().jsonPath().getLong("id");

        String createdKeyHashid = given()
                .auth().oauth2(adminUserOAuth2AccessToken)
                .contentType(ContentType.JSON)
                .body(keyPayload)
        .when()
                .post(RequestMappings.KEYS)
        .then()
                .extract().jsonPath().getString("id");

        given()
                .auth().oauth2(adminUserOAuth2AccessToken)
                .pathParam("keyCategoryId", createdKeyCategoryId)
                .param(KEY_ID, createdKeyHashid)
        .when()
                .get(RequestMappings.KEY_CATEGORIES + RequestMappings.KEY_CATEGORY_KEYS)
        .then()
                .statusCode(HttpStatus.OK.value());

        given()
                .auth().oauth2(adminUserOAuth2AccessToken)
                .pathParam("keyCategoryId", createdKeyCategoryId)
                .param(KEY_ID, createdKeyHashid)
        .when()
                .delete(RequestMappings.KEY_CATEGORIES + RequestMappings.KEY_CATEGORY_KEYS)
        .then()
                .statusCode(HttpStatus.OK.value())
                .root("keys")
                .body("size()", equalTo(0));
    }

    @Test
    public void testRemoveKeyAsUser() {
        initializeTestKeyCategoryPayload();
        initializeTestKeyPayload();

        Long createdKeyCategoryId = given()
                .auth().oauth2(adminUserOAuth2AccessToken)
                .contentType(ContentType.JSON)
                .body(keyCategoryPayload)
        .when()
                .post(RequestMappings.KEY_CATEGORIES)
        .then()
                .extract().jsonPath().getLong("id");

        String createdKeyHashid = given()
                .auth().oauth2(adminUserOAuth2AccessToken)
                .contentType(ContentType.JSON)
                .body(keyPayload)
        .when()
                .post(RequestMappings.KEYS)
        .then()
                .extract().jsonPath().getString("id");

        given()
                .auth().oauth2(adminUserOAuth2AccessToken)
                .pathParam("keyCategoryId", createdKeyCategoryId)
                .param(KEY_ID, createdKeyHashid)
        .when()
                .get(RequestMappings.KEY_CATEGORIES + RequestMappings.KEY_CATEGORY_KEYS)
        .then()
                .statusCode(HttpStatus.OK.value());

        given()
                .auth().oauth2(userOAuth2AccessToken)
                .pathParam("keyCategoryId", createdKeyCategoryId)
                .param(KEY_ID, createdKeyHashid)
        .when()
                .delete(RequestMappings.KEY_CATEGORIES + RequestMappings.KEY_CATEGORY_KEYS)
        .then()
                .statusCode(HttpStatus.FORBIDDEN.value());

        given()
                .auth().oauth2(adminUserOAuth2AccessToken)
                .pathParam("keyCategoryId", createdKeyCategoryId)
        .when()
                .get(RequestMappings.KEY_CATEGORIES + RequestMappings.KEY_CATEGORIES_ID)
        .then()
                .statusCode(HttpStatus.OK.value())
                .root("keys")
                .body("size()", equalTo(1));
    }

    @Test
    public void testSetResponsibleUserAsAdmin() {
        initializeTestKeyCategoryPayload();

        Long createdKeyCategoryId = given()
                .auth().oauth2(adminUserOAuth2AccessToken)
                .contentType(ContentType.JSON)
                .body(keyCategoryPayload)
        .when()
                .post(RequestMappings.KEY_CATEGORIES)
        .then()
                .extract().jsonPath().getLong("id");

        Long responsibleUserId = given()
                .auth().oauth2(adminUserOAuth2AccessToken)
                .pathParam("keyCategoryId", createdKeyCategoryId)
                .param("userLogin", userLogin)
        .when()
                .put(RequestMappings.KEY_CATEGORIES + RequestMappings.KEY_CATEGORY_RESPONSIBLE)
        .then()
                .statusCode(HttpStatus.OK.value())
                .extract().jsonPath().getLong("responsible");

        given()
                .auth().oauth2(adminUserOAuth2AccessToken)
                .pathParam("keyCategoryId", createdKeyCategoryId)
        .when()
                .get(RequestMappings.KEY_CATEGORIES + RequestMappings.KEY_CATEGORIES_ID)
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("responsible", equalTo(responsibleUserId.intValue()));
    }

    @Test
    public void testSetResponsibleUserAsUser() {
        initializeTestKeyCategoryPayload();

        Long createdKeyCategoryId = given()
                .auth().oauth2(adminUserOAuth2AccessToken)
                .contentType(ContentType.JSON)
                .body(keyCategoryPayload)
        .when()
                .post(RequestMappings.KEY_CATEGORIES)
        .then()
                .extract().jsonPath().getLong("id");

        given()
                .auth().oauth2(userOAuth2AccessToken)
                .pathParam("keyCategoryId", createdKeyCategoryId)
                .param("userLogin", userLogin)
        .when()
                .put(RequestMappings.KEY_CATEGORIES + RequestMappings.KEY_CATEGORY_RESPONSIBLE)
        .then()
                .statusCode(HttpStatus.FORBIDDEN.value());

        given()
                .auth().oauth2(adminUserOAuth2AccessToken)
                .pathParam("keyCategoryId", createdKeyCategoryId)
        .when()
                .get(RequestMappings.KEY_CATEGORIES + RequestMappings.KEY_CATEGORIES_ID)
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("responsible", not(equalTo(createdKeyCategoryId.intValue())));
    }

    /**
     * Initializes the test key category payload.
     */
    private void initializeTestKeyCategoryPayload() {
        keyCategoryPayload.put("name", fairy.textProducer().word(1));
    }

    /**
     * Initializes the test user group payload.
     */
    private void initializeTestUserGroupPayload() {
        userGroupPayload.put("name", fairy.textProducer().word(1));
    }

    /**
     * Initializes the test key payload.
     */
    private void initializeTestKeyPayload() {
        keyPayload.put("name", fairy.textProducer().word(1));
        keyPayload.put("login", fairy.person().getEmail());
        keyPayload.put("password", fairy.person().getPassword());
    }
}
