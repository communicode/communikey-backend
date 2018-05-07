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

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;

import de.communicode.communikey.IntegrationBaseTest;
import de.communicode.communikey.controller.RequestMappings;
import de.communicode.communikey.domain.Authority;
import de.communicode.communikey.security.AuthoritiesConstants;
import org.junit.Test;
import org.springframework.http.HttpStatus;

/**
 * Integration tests for the {@link Authority} REST API.
 *
 * @author sgreb@communicode.de
 * @since 0.4.0
 */
public class AuthorityApiIt extends IntegrationBaseTest {

    @Test
    public void testGetAuthorityAsAdmin() {
        given()
            .auth().oauth2(adminUserOAuth2AccessToken)
            .pathParam("authorityName", AuthoritiesConstants.ADMIN)
        .when()
            .get(RequestMappings.AUTHORITIES + RequestMappings.AUTHORITIES_NAME)
        .then()
            .statusCode(HttpStatus.OK.value())
            .assertThat().extract().body().as(Authority.class).getName().equals(AuthoritiesConstants.ADMIN);
    }

    @Test
    public void testGetAuthorityAsUser() {
        given()
            .auth().oauth2(userOAuth2AccessToken)
            .pathParam("authorityName", AuthoritiesConstants.ADMIN)
        .when()
            .get(RequestMappings.AUTHORITIES + RequestMappings.AUTHORITIES_NAME)
        .then()
            .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    public void testGetAllAuthoritiesAsAdmin() {
        given()
            .auth().oauth2(adminUserOAuth2AccessToken)
        .when()
            .get(RequestMappings.AUTHORITIES)
        .then()
            .statusCode(HttpStatus.OK.value())
            .body("size()", not(equalTo(0)));
    }

    @Test
    public void testGetAllAuthoritiesAsUser() {
        given()
            .auth().oauth2(userOAuth2AccessToken)
        .when()
            .get(RequestMappings.AUTHORITIES)
        .then()
            .statusCode(HttpStatus.FORBIDDEN.value());
    }
}
