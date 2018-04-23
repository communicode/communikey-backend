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
package de.communicode.communikey.service.payload;

import static de.communicode.communikey.config.SecurityConfig.EMAIL_REGEX;

import de.communicode.communikey.domain.User;
import javax.validation.constraints.NotBlank;

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
