/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2017
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
public class AuthorityApiIT extends IntegrationBaseTest {

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
