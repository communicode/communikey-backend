/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2017
 */
package de.communicode.communikey.api;

import static de.communicode.communikey.controller.PathVariables.KEY_ID;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItems;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import de.communicode.communikey.IntegrationBaseTest;
import de.communicode.communikey.controller.RequestMappings;
import de.communicode.communikey.domain.Key;
import de.communicode.communikey.domain.UserGroup;
import de.communicode.communikey.domain.KeyCategory;
import io.restassured.http.ContentType;
import org.junit.Test;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Integration tests for the {@link Key} REST API.
 *
 * @author sgreb@communicode.de
 * @since 0.4.0
 */
public class KeyApiIT extends IntegrationBaseTest {

    private Map<String, Object> keyPayload = new HashMap<>();
    private Key key = new Key();
    private UserGroup userGroup = new UserGroup();
    private KeyCategory keyCategory = new KeyCategory();

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
        String createdKeyHashid = given()
                .auth().oauth2(adminUserOAuth2AccessToken)
                .contentType(ContentType.JSON)
                .body(keyPayload)
        .when()
                .post(RequestMappings.KEYS)
        .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract().jsonPath().getString("id");

        given()
                .auth().oauth2(adminUserOAuth2AccessToken)
                .pathParam(KEY_ID, createdKeyHashid)
        .when()
                .delete(RequestMappings.KEYS + RequestMappings.KEY_HASHID)
        .then()
                .statusCode(HttpStatus.NO_CONTENT.value());

        given()
                .auth().oauth2(adminUserOAuth2AccessToken)
                .pathParam(KEY_ID, createdKeyHashid)
        .when()
                .get(RequestMappings.KEYS + RequestMappings.KEY_HASHID)
        .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void testDeleteKeyAsUser() {
        initializeTestKeyPayload();
        String createdKeyHashid = given()
                .auth().oauth2(adminUserOAuth2AccessToken)
                .contentType(ContentType.JSON)
                .body(keyPayload)
        .when()
                .post(RequestMappings.KEYS)
        .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract().jsonPath().getString("id");

        given()
                .auth().oauth2(userOAuth2AccessToken)
                .pathParam(KEY_ID, createdKeyHashid)
        .when()
                .delete(RequestMappings.KEYS + RequestMappings.KEY_HASHID)
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
        String createdKeyHashid = given()
                .auth().oauth2(adminUserOAuth2AccessToken)
                .contentType(ContentType.JSON)
                .body(keyPayload)
        .when()
                .post(RequestMappings.KEYS)
        .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract().jsonPath().getString("id");

        given()
                .auth().oauth2(adminUserOAuth2AccessToken)
                .pathParam(KEY_ID, createdKeyHashid)
        .when()
                .get(RequestMappings.KEYS + RequestMappings.KEY_HASHID)
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("id", equalTo(createdKeyHashid));
    }

    @Test
    public void testGetKeyAsUser() {
        initializeTestKeyPayload();
        String createdKeyHashid = given()
                .auth().oauth2(adminUserOAuth2AccessToken)
                .contentType(ContentType.JSON)
                .body(keyPayload)
        .when()
                .post(RequestMappings.KEYS)
        .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract().jsonPath().getString("id");

        given()
                .auth().oauth2(userOAuth2AccessToken)
                .pathParam(KEY_ID, createdKeyHashid)
        .when()
                .get(RequestMappings.KEYS + RequestMappings.KEY_HASHID)
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
        String createdKeyHashid = given()
                .auth().oauth2(adminUserOAuth2AccessToken)
                .contentType(ContentType.JSON)
                .body(keyPayload)
        .when()
                .post(RequestMappings.KEYS)
        .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract().jsonPath().getString("id");

        keyPayload.replace("name", "newName");
        keyPayload.replace("login", "newLogin");

        given()
                .auth().oauth2(adminUserOAuth2AccessToken)
                .contentType(ContentType.JSON)
                .pathParam(KEY_ID, createdKeyHashid)
                .body(keyPayload)
        .when()
                .put(RequestMappings.KEYS + RequestMappings.KEY_HASHID)
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("name", equalTo(keyPayload.get("name")))
                .body("login", equalTo(keyPayload.get("login")));
    }

    @Test
    public void testUpdateKeyAsUser() {
        initializeTestKeyPayload();
        String createdKeyHashid = given()
                .auth().oauth2(adminUserOAuth2AccessToken)
                .contentType(ContentType.JSON)
                .body(keyPayload)
        .when()
                .post(RequestMappings.KEYS)
        .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract().jsonPath().getString("id");

        keyPayload.replace("name", "newName");
        keyPayload.replace("login", "login");
        keyPayload.replace("password", "newPassword");

        given()
                .auth().oauth2(userOAuth2AccessToken)
                .contentType(ContentType.JSON)
                .pathParam(KEY_ID, createdKeyHashid)
                .body(keyPayload)
        .when()
                .put(RequestMappings.KEYS + RequestMappings.KEY_HASHID)
        .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    public void testSubscriberListOfKeyWithTwoSubscribers() {
        initializeSubscriberTestData();
        given()
                .auth().oauth2(adminUserOAuth2AccessToken)
                .contentType(ContentType.JSON)
                .pathParam(KEY_ID, key.getHashid())
        .when()
                .get(RequestMappings.KEYS + RequestMappings.KEY_SUBSCRIBERS)
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("size()", equalTo(2))
                .body("publicKey", hasItems(user.getPublicKey()))
                .body("user", hasItems(user.getLogin()));
    }

    @Test
    public void testPutEncryptedPasswordsForSubscribers() {
        initializeSubscriberTestData();
        given()
                .auth().oauth2(adminUserOAuth2AccessToken)
                .contentType(ContentType.JSON)
                .pathParam(KEY_ID, key.getHashid())
                .body(encryptedPasswordPayload())
        .when()
                .put(RequestMappings.KEYS + RequestMappings.KEY_HASHID)
        .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    public void testEncryptedPasswordDeletionAfterLosingAccessToUserGroup() {
        initializeSubscriberTestData();
        given()
                .auth().oauth2(adminUserOAuth2AccessToken)
                .contentType(ContentType.JSON)
                .pathParam(KEY_ID, key.getHashid())
                .body(encryptedPasswordPayload())
        .when()
                .put(RequestMappings.KEYS + RequestMappings.KEY_HASHID)
        .then()
                .statusCode(HttpStatus.OK.value());

        given()
                .auth().oauth2(userOAuth2AccessToken)
                .contentType(ContentType.JSON)
                .pathParam(KEY_ID, key.getHashid())
        .when()
                .get(RequestMappings.KEYS + RequestMappings.KEY_ENCRYPTED_PASSWORD)
        .then()
                .statusCode(HttpStatus.OK.value());

        given()
                .auth().oauth2(adminUserOAuth2AccessToken)
                .pathParam("userGroupId", userGroup.getId())
                .param("login", user.getLogin())
        .when()
                .delete(RequestMappings.USER_GROUPS + RequestMappings.USER_GROUPS_USERS)
        .then()
                .statusCode(HttpStatus.OK.value())
                .root("users")
                .body("size()", equalTo(0));

        given()
                .auth().oauth2(userOAuth2AccessToken)
                .contentType(ContentType.JSON)
                .pathParam(KEY_ID, key.getHashid())
        .when()
                .get(RequestMappings.KEYS + RequestMappings.KEY_ENCRYPTED_PASSWORD)
        .then()
                .statusCode(HttpStatus.NOT_FOUND.value());

    }

    private void initializeSubscriberTestData() {
        initializeTestKey();
        key.setCreator(user);
        keyRepository.save(key);
        key.setHashid(hashIds.encode(key.getId()));
        keyRepository.save(key);
        initializeTestUserGroup();
        userGroup.addUser(user);
        userGroupRepository.save(userGroup);
        initializeTestKeyCategory();
        keyCategory.addKey(key);
        keyCategory.addGroup(userGroup);
        keyCategory.setCreator(user);
        keyCategoryRepository.save(keyCategory);
        key.setCategory(keyCategory);
        keyRepository.save(key);
    }

    /**
     * Initializes the test key.
     */
    private void initializeTestKey() {
        key.setName(fairy.textProducer().word(1));
        key.setLogin(fairy.textProducer().word(1));
        key.setNotes(fairy.textProducer().word(5));
    }

    /**
     * Returns a payload for password encryption testing
     */
    private ImmutableMap<String, Object> encryptedPasswordPayload() {
        return ImmutableMap.<String, Object>builder()
            .put("name", "newname")
            .put("login", "newlogin")
            .put("encryptedPasswords", ImmutableSet.<Map>builder()
                .add(ImmutableMap.<String, String>builder()
                    .put("login", "root")
                    .put("encryptedPassword", "VGhpcyBpcyBhIGJhc2U2NCBlbmNyeXB0ZWQgcGFzc3dvcmQgc3RyaW5n")
                    .build())
                .add(ImmutableMap.<String, String>builder()
                    .put("login", "user")
                    .put("encryptedPassword", "VGhpcyBpcyBhIGJhc2U2NCBlbmNyeXB0ZWQgcGFzc3dvcmQgc3RyaW5n")
                    .build())
                .build())
            .build();
    }

    /**
     * Initializes the test userGroup.
     */
    private void initializeTestUserGroup() {
        userGroup.setName(fairy.textProducer().word(1));
    }

    /**
     * Initializes the test category.
     */
    private void initializeTestKeyCategory() {
        keyCategory.setName(fairy.textProducer().word(1));
    }

    /**
     * Initializes the test key payload.
     */
    private void initializeTestKeyPayload() {
        keyPayload.put("name", "key");
        keyPayload.put("login", "login");
        keyPayload.put("notes", "notes");
        Map<String, String> encryptedPassword = new HashMap<>();
        encryptedPassword.put("login", "root");
        encryptedPassword.put("encryptedPassword", "user encrypted password content");
        Set<Map<String, String>> encryptedPasswords = new HashSet<>();
        encryptedPasswords.add(encryptedPassword);
        keyPayload.put("encryptedPasswords", encryptedPasswords);
    }
}
