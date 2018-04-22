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
                .param(RequestParameter.API_ME)
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
                .param(RequestParameter.API_ME)
        .when()
                .get(RequestMappings.API)
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("privileged", equalTo(false));
    }
}
