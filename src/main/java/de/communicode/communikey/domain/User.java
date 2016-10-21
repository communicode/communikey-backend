/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2016
*/
package de.communicode.communikey.domain;

import static de.communicode.communikey.CommunikeyConstants.TABLE_USERS;
import static de.communicode.communikey.CommunikeyConstants.TABLE_USERS_COLUMN_ENABLED;
import static de.communicode.communikey.CommunikeyConstants.TABLE_USERS_COLUMN_USER_ID;

import de.communicode.communikey.type.UserRoleType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Represents a user entity.
 *
 * @author sgreb@communicode.de
 * @since 0.2.0
 */
@Entity
@Table(name = TABLE_USERS)
public class User {
    @Id
    @Column(name = TABLE_USERS_COLUMN_USER_ID, nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = TABLE_USERS_COLUMN_ENABLED, nullable = false)
    private boolean isEnabled;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String role;

    private User() {}

    /**
     * Constructs a new user entity object with an auto-generated ID.
     *
     * @param username the name of the user
     * @param password the password of the user
     * @param role     the {@link UserRoleType} of the user
     */
    public User(String username, String password, UserRoleType role) {
        isEnabled = true;
        this.password = password;
        this.username = username;
        this.role = role.name();
    }

    public long getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public String getRole() {
        return role;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setRole(UserRoleType role) {
        this.role = role.name();
    }
}
