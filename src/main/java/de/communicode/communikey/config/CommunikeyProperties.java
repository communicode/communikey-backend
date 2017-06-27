/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2017
 */
package de.communicode.communikey.config;

import static de.communicode.communikey.config.SecurityConfig.APP_ID;
import static de.communicode.communikey.config.SecurityConfig.EMAIL_REGEX;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * communikey (default) configuration properties.
 *
 * <p>Values can be overridden in the {@code application.yml} file.
 *
 * @author sgreb@communicode.de
 * @since 0.2.0
 */
@ConfigurationProperties(prefix = APP_ID, ignoreUnknownFields = false)
public final class CommunikeyProperties {

    private final Security security = new Security();

    public Security getSecurity() {
        return this.security;
    }

    /**
     * Provides communikey specific security properties.
     */
    public static class Security {

        private final OAuth2 oAuth2 = new OAuth2();
        private final Root root = new Root();

        public OAuth2 getoAuth2() {
            return this.oAuth2;
        }

        public Root getRoot() {
            return this.root;
        }

        /**
         * Provides communikey specific OAuth2 security properties.
         */
        public static class OAuth2 {

            /**
             * The default validity of a communikey OAuth2 access token in seconds.
             */
            public final int COMMUNIKEY_SECURITY_OAUTH2_DEFAULT_ACCESS_TOKEN_VALIDITY = 604800;

            @NotNull
            private int accessTokenValidity = COMMUNIKEY_SECURITY_OAUTH2_DEFAULT_ACCESS_TOKEN_VALIDITY;

            @NotNull
            private String secret = "secret";

            public int getAccessTokenValidity() {
                return this.accessTokenValidity;
            }

            public void setAccessTokenValidity(int accessTokenValidity) {
                this.accessTokenValidity = accessTokenValidity;
            }

            public String getSecret() {
                return this.secret;
            }

            public void setSecret(String secret) {
                this.secret = secret;
            }

            @Override
            public String toString() {
                return "OAuth2{" + "accessTokenValidity=" + this.accessTokenValidity + '}';
            }
        }

        /**
         * Provides communikey specific root user properties.
         */
        public static class Root {

            @NotBlank
            private String login = "root";

            @NotBlank
            @Pattern(regexp = EMAIL_REGEX, message = "not a well-formed email address")
            private String email = "cckey_root@communicode.de";

            @NotNull
            private String password;

            private String firstName = "communikey";

            private String lastName = "root";

            public String getLogin() {
                return this.login;
            }

            public void setLogin(String login) {
                this.login = login;
            }

            public String getEmail() {
                return this.email;
            }

            public void setEmail(String email) {
                this.email = email;
            }

            public String getPassword() {
                return this.password;
            }

            public void setPassword(String password) {
                this.password = password;
            }

            public String getFirstName() {
                return this.firstName;
            }

            public void setFirstName(String firstName) {
                this.firstName = firstName;
            }

            public String getLastName() {
                return this.lastName;
            }

            public void setLastName(String lastName) {
                this.lastName = lastName;
            }

            @Override
            public String toString() {
                return "Root{" +
                    "login='" + this.login + '\'' +
                    ", email='" + this.email + '\'' +
                    ", firstName='" + this.firstName + '\'' +
                    ", lastName='" + this.lastName + '\'' +
                    '}';
            }
        }

        @Override
        public String toString() {
            return "Security{" +
                "oAuth2=" + this.oAuth2 +
                ", root=" + this.root +
                '}';
        }
    }

    @Override public String toString() {
        return "CommunikeyProperties{" + "security=" + this.security + '}';
    }
}
