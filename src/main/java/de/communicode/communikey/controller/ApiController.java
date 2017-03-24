/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2017
 */
package de.communicode.communikey.controller;

import static de.communicode.communikey.CommunikeyApplication.COMMUNIKEY_REST_API_VERSION;
import static de.communicode.communikey.controller.RequestMappings.API;

import com.google.common.collect.ImmutableMap;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * The REST API controller to provide information about the communikey API.
 * <p>
 * Mapped to the "{@value RequestMappings#API}" endpoint.
 *
 * @author sgreb@communicode.de
 * @since 0.3.0
 */
@RestController
@RequestMapping(API)
public class ApiController {

    /**
     * Gets the version of the communikey REST API.
     * <p>
     * This endpoint is mapped to "{@value RequestMappings#KEY_CATEGORIES}{@value RequestMappings#KEY_CATEGORIES_ID}".
     *
     * @param version the request parameter to get the version
     * @return the version of the communikey REST API as response entity
     */
    @GetMapping
    ResponseEntity<Map<String, String>> getRestApiVersion(@RequestParam String version) {
        return new ResponseEntity<>(ImmutableMap.of("version", COMMUNIKEY_REST_API_VERSION), HttpStatus.OK);
    }
}
