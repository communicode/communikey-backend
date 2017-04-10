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
import javax.validation.constraints.Size;

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
        return security;
    }

    public static class Security {

        private final OAuth2 oAuth2 = new OAuth2();
        private final Root root = new Root();

        public OAuth2 getoAuth2() {
            return oAuth2;
        }

        public Root getRoot() {
            return root;
        }

        public static class OAuth2 {

            @NotNull
            private int accessTokenValidity = 604800;
            @NotNull
            private String secret = "secret";

            public int getAccessTokenValidity() {
                return accessTokenValidity;
            }

            public void setAccessTokenValidity(int accessTokenValidity) {
                this.accessTokenValidity = accessTokenValidity;
            }

            public String getSecret() {
                return secret;
            }

            public void setSecret(String secret) {
                this.secret = secret;
            }

            @Override
            public String toString() {
                return "OAuth2{" + "accessTokenValidity=" + accessTokenValidity + '}';
            }
        }

        public static class Root {

            @NotBlank
            @Size(max = 100)
            private String login = "root";
            @NotBlank
            @Pattern(regexp = EMAIL_REGEX, message = "not a well-formed email address")
            @Size(max = 100)
            private String email = "cckey_root@communicode.de";
            @NotNull
            private String password;
            @Size(max = 50)
            private String firstName = "communikey";
            @Size(max = 50)
            private String lastName;

            public String getRootLogin() {
                return login;
            }

            public void setRootLogin(String login) {
                this.login = login;
            }

            public String getRootEmail() {
                return email;
            }

            public void setRootEmail(String email) {
                this.email = email;
            }

            public String getRootPassword() {
                return password;
            }

            public void setRootPassword(String password) {
                this.password = password;
            }

            public String getRootFirstName() {
                return firstName;
            }

            public void setRootFirstName(String firstName) {
                this.firstName = firstName;
            }

            public String getRootLastName() {
                return lastName;
            }

            public void setRootLastName(String lastName) {
                this.lastName = lastName;
            }

            @Override
            public String toString() {
                return "Root{" +
                    "login='" + login + '\'' +
                    ", email='" + email + '\'' +
                    ", firstName='" + firstName + '\'' +
                    ", lastName='" + lastName + '\'' +
                    '}';
            }
        }

        @Override
        public String toString() {
            return "Security{" +
                "oAuth2=" + oAuth2 +
                ", root=" + root +
                '}';
        }
    }

    @Override public String toString() {
        return "CommunikeyProperties{" + "security=" + security + '}';
    }
}
