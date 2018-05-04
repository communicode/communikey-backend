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

package de.communicode.communikey.api;

import static de.communicode.communikey.controller.PathVariables.TAG_ID;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.equalTo;

import de.communicode.communikey.IntegrationBaseTest;
import de.communicode.communikey.controller.RequestMappings;
import de.communicode.communikey.domain.Tag;
import io.restassured.http.ContentType;
import org.junit.Test;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

/**
 * Integration tests for the {@link Tag} REST API.
 *
 * @author dvonderbey@communicode.de
 * @since 0.18.0
 */
public class TagApiIt extends IntegrationBaseTest {

    private Map<String, Object> tagPayload = new HashMap<>();

    @Test
    public void testCreateAsAdmin() {
        initializeTestTagPayload();
        given()
            .auth().oauth2(adminUserOAuth2AccessToken)
            .contentType(ContentType.JSON)
            .body(tagPayload)
        .when()
            .post(RequestMappings.TAGS)
        .then()
            .statusCode(HttpStatus.CREATED.value())
            .body("name", equalTo(tagPayload.get("name")));
    }

    @Test
    public void testCreateAsUser() {
        initializeTestTagPayload();
        given()
            .auth().oauth2(userOAuth2AccessToken)
            .contentType(ContentType.JSON)
            .body(tagPayload)
        .when()
            .post(RequestMappings.TAGS)
        .then()
            .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    public void testDeleteAsAdmin() {
        initializeTestTagPayload();

        String createdTagHashid = given()
            .auth().oauth2(adminUserOAuth2AccessToken)
            .contentType(ContentType.JSON)
            .body(tagPayload)
            .when()
                .post(RequestMappings.TAGS)
            .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract().jsonPath().getString("id");

        given()
            .auth().oauth2(adminUserOAuth2AccessToken)
            .pathParam(TAG_ID, createdTagHashid)
        .when()
            .delete(RequestMappings.TAGS + RequestMappings.TAG_HASHID)
        .then()
            .statusCode(HttpStatus.NO_CONTENT.value());

        given()
            .auth().oauth2(adminUserOAuth2AccessToken)
            .pathParam(TAG_ID, createdTagHashid)
        .when()
            .get(RequestMappings.TAGS + RequestMappings.TAG_HASHID)
        .then()
            .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void testDeleteAsUser() {
        initializeTestTagPayload();

        String createdTagHashid = given()
            .auth().oauth2(adminUserOAuth2AccessToken)
            .contentType(ContentType.JSON)
            .body(tagPayload)
            .when()
                .post(RequestMappings.TAGS)
            .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract().jsonPath().getString("id");

        given()
            .auth().oauth2(userOAuth2AccessToken)
            .pathParam(TAG_ID, createdTagHashid)
        .when()
            .delete(RequestMappings.TAGS + RequestMappings.TAG_HASHID)
        .then()
            .statusCode(HttpStatus.FORBIDDEN.value());

        given()
            .auth().oauth2(adminUserOAuth2AccessToken)
            .pathParam(TAG_ID, createdTagHashid)
        .when()
            .get(RequestMappings.TAGS + RequestMappings.TAG_HASHID)
        .then()
            .statusCode(HttpStatus.OK.value())
            .body("id", equalTo(createdTagHashid));
    }

    @Test
    public void testDeleteAllAsAdmin() {
        initializeTestTagPayload();

        given()
            .auth().oauth2(adminUserOAuth2AccessToken)
            .contentType(ContentType.JSON)
            .body(tagPayload)
        .when()
            .post(RequestMappings.TAGS)
        .then()
            .statusCode(HttpStatus.CREATED.value());

        given()
            .auth().oauth2(adminUserOAuth2AccessToken)
        .when()
            .delete(RequestMappings.TAGS)
        .then()
            .statusCode(HttpStatus.NO_CONTENT.value());

        given()
            .auth().oauth2(adminUserOAuth2AccessToken)
        .when()
            .get(RequestMappings.TAGS)
        .then()
            .statusCode(HttpStatus.OK.value())
            .body("size()", equalTo(0));
    }

    @Test
    public void testDeleteAllAsUser() {
        initializeTestTagPayload();

        given()
            .auth().oauth2(adminUserOAuth2AccessToken)
            .contentType(ContentType.JSON)
            .body(tagPayload)
        .when()
            .post(RequestMappings.TAGS)
        .then()
            .statusCode(HttpStatus.CREATED.value());

        given()
            .auth().oauth2(userOAuth2AccessToken)
        .when()
            .delete(RequestMappings.TAGS)
        .then()
            .statusCode(HttpStatus.FORBIDDEN.value());

        given()
            .auth().oauth2(adminUserOAuth2AccessToken)
        .when()
            .get(RequestMappings.TAGS)
        .then()
            .statusCode(HttpStatus.OK.value())
            .body("size()", equalTo(1));
    }

    @Test
    public void testGetAsAdmin() {
        initializeTestTagPayload();

        String createdTagHashid = given()
            .auth().oauth2(adminUserOAuth2AccessToken)
            .contentType(ContentType.JSON)
            .body(tagPayload)
            .when()
                .post(RequestMappings.TAGS)
            .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract().jsonPath().getString("id");

        given()
            .auth().oauth2(adminUserOAuth2AccessToken)
            .pathParam(TAG_ID, createdTagHashid)
        .when()
            .get(RequestMappings.TAGS + RequestMappings.TAG_HASHID)
        .then()
            .statusCode(HttpStatus.OK.value())
            .body("id", equalTo(createdTagHashid));
    }

    @Test
    public void testGetAsUser() {
        initializeTestTagPayload();

        String createdTagHashid = given()
            .auth().oauth2(adminUserOAuth2AccessToken)
            .contentType(ContentType.JSON)
            .body(tagPayload)
            .when()
                .post(RequestMappings.TAGS)
            .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract().jsonPath().getString("id");

        given()
            .auth().oauth2(userOAuth2AccessToken)
            .pathParam(TAG_ID, createdTagHashid)
        .when()
            .get(RequestMappings.TAGS + RequestMappings.TAG_HASHID)
        .then()
            .statusCode(HttpStatus.OK.value())
            .body("id", equalTo(createdTagHashid));
    }

    @Test
    public void testGetAllAsAdmin() {
        initializeTestTagPayload();

        given()
            .auth().oauth2(adminUserOAuth2AccessToken)
            .contentType(ContentType.JSON)
            .body(tagPayload)
        .when()
            .post(RequestMappings.TAGS);

        given()
            .auth().oauth2(adminUserOAuth2AccessToken)
        .when()
            .get(RequestMappings.TAGS)
        .then()
            .statusCode(HttpStatus.OK.value())
            .body("size()", equalTo(1));
    }

    @Test
    public void testSetColorAsAdmin() {
        initializeTestTagPayload();

        String createdTagHashid = given()
            .auth().oauth2(adminUserOAuth2AccessToken)
            .contentType(ContentType.JSON)
            .body(tagPayload)
            .when()
                .post(RequestMappings.TAGS)
            .then()
                .extract().jsonPath().getString("id");

        tagPayload.put("color", "#332fb5");

        given()
            .auth().oauth2(adminUserOAuth2AccessToken)
            .contentType(ContentType.JSON)
            .pathParam(TAG_ID, createdTagHashid)
            .body(tagPayload)
        .when()
            .put(RequestMappings.TAGS + RequestMappings.TAG_HASHID)
        .then()
            .statusCode(HttpStatus.OK.value());

        given()
            .auth().oauth2(adminUserOAuth2AccessToken)
            .pathParam(TAG_ID, createdTagHashid)
        .when()
            .get(RequestMappings.TAGS + RequestMappings.TAG_HASHID)
        .then()
            .statusCode(HttpStatus.OK.value())
            .body("color", equalTo(tagPayload.get("color")));
    }

    @Test
    public void testSetColorAsUser() {
        initializeTestTagPayload();

        String createdTagHashid = given()
            .auth().oauth2(adminUserOAuth2AccessToken)
            .contentType(ContentType.JSON)
            .body(tagPayload)
            .when()
                .post(RequestMappings.TAGS)
            .then()
                .extract().jsonPath().getString("id");

        tagPayload.put("color", "#332fb5");

        given()
            .auth().oauth2(userOAuth2AccessToken)
            .contentType(ContentType.JSON)
            .pathParam(TAG_ID, createdTagHashid)
            .body(tagPayload)
        .when()
            .put(RequestMappings.TAGS + RequestMappings.TAG_HASHID)
        .then()
            .statusCode(HttpStatus.FORBIDDEN.value());

        given()
            .auth().oauth2(userOAuth2AccessToken)
            .pathParam(TAG_ID, createdTagHashid)
        .when()
            .get(RequestMappings.TAGS + RequestMappings.TAG_HASHID)
        .then()
            .statusCode(HttpStatus.OK.value())
            .body("color", not(equalTo(tagPayload.get("color"))));
    }

    /**
     * Initializes the test tag payload.
     */
    private void initializeTestTagPayload() {
        tagPayload.put("name", fairy.textProducer().word(3));
        tagPayload.put("color", "#2ad127");
    }
}
