/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2017
 */
package de.communicode.communikey.service.payload;

import static de.communicode.communikey.config.SecurityConfig.EMAIL_REGEX;

import de.communicode.communikey.domain.User;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Pattern;

/**
 * A payload object for a {@link User}.
 *
 * @author sgreb@communicode.de
 * @since 0.2.0
 */
public class UserPayload {

    private Long id;

    private String login;

    @NotBlank
    @Pattern(regexp = EMAIL_REGEX, message = "not a well-formed email address")
    private String email;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    public UserPayload() {}

    public UserPayload(User user) {
        this.id = user.getId();
        this.login = user.getLogin();
        this.email = user.getEmail();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getEmail() {
        return email.trim();
    }

    public String getFirstName() {
        return firstName.trim();
    }

    public String getLastName() {
        return lastName.trim();
    }

    @Override
    public String toString() {
        return "UserPayload{" +
            "id=" + id +
            ", login='" + login + '\'' +
            ", email='" + email + '\'' +
            ", firstName='" + firstName + '\'' +
            ", lastName='" + lastName + '\'' +
            '}';
    }
}
