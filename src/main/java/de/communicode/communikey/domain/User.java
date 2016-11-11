/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2016
 */
package de.communicode.communikey.domain;

import static de.communicode.communikey.config.CommunikeyConstants.TABLE_LINK_USERS_TO_USER_GROUPS;
import static de.communicode.communikey.config.CommunikeyConstants.TABLE_USERS;
import static de.communicode.communikey.config.CommunikeyConstants.TABLE_USERS_COLUMN_ENABLED;
import static de.communicode.communikey.config.CommunikeyConstants.TABLE_USERS_COLUMN_USER_ID;
import static de.communicode.communikey.config.CommunikeyConstants.TABLE_USER_GROUPS_COLUMN_USER_GROUP_ID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.communicode.communikey.exception.UserGroupNotFoundException;
import de.communicode.communikey.type.UserRoleType;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import java.util.HashSet;
import java.util.Set;

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

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "creator")
    private Set<Key> keys;

    @OneToMany(mappedBy = "creator")
    private Set<KeyCategory> keyCategories;

    @OneToMany(mappedBy = "responsible")
    private Set<KeyCategory> responsibleKeyCategories;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
        name = TABLE_LINK_USERS_TO_USER_GROUPS,
        joinColumns = {
            @JoinColumn(
                name = TABLE_USERS_COLUMN_USER_ID,
                nullable = false,
                updatable = false
            )
        },
        inverseJoinColumns = {
            @JoinColumn(
                name = TABLE_USER_GROUPS_COLUMN_USER_GROUP_ID,
                nullable = false
            )
        }
    )
    private Set<UserGroup> groups;

    @Column(name = TABLE_USERS_COLUMN_ENABLED, nullable = false)
    private boolean isEnabled;

    @Column(nullable = false)
    @JsonIgnore
    private String password;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String role;

    private User() {}

    /**
     * Constructs a new user entity object with the given attributes, an auto-generated ID, the default user role {@link UserRoleType#ROLE_USER} and no
     * assigned user groups.
     *
     * @param username the name of the user
     * @param password the password of the user
     */
    public User(String username, String password) {
        this(username, password, UserRoleType.ROLE_USER, new HashSet<>(0));
    }

    /**
     * Constructs a new user entity object with the given attributes, an auto-generated ID and no assigned user groups.
     *
     * @param username the name of the user
     * @param password the password of the user
     * @param role the {@link UserRoleType} of the user
     */
    public User(String username, String password, UserRoleType role) {
        this(username, password, role, new HashSet<>(0));
    }

    /**
     * Constructs a new user entity object with the given attributes and an auto-generated ID.
     *
     * @param username the name of the user
     * @param password the password of the user
     * @param role the {@link UserRoleType} of the user
     * @param groups a collection {@link UserGroup} to assign the user to
     */
    public User(String username, String password, UserRoleType role, Set<UserGroup> groups) {
        this.groups = new HashSet<>(groups);
        isEnabled = true;
        this.password = password;
        this.username = username;
        this.role = role.name();
    }

    public Set<UserGroup> getGroups() {
        return new HashSet<>(groups);
    }

    public long getId() {
        return id;
    }

    public Set<KeyCategory> getKeyCategories() {
        return keyCategories;
    }

    public Set<Key> getKeys() {
        return keys;
    }

    public String getPassword() {
        return password;
    }

    public Set<KeyCategory> getResponsibleKeyCategories() {
        return responsibleKeyCategories;
    }

    public String getRole() {
        return role;
    }

    public String getUsername() {
        return username;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public void setGroups(Set<UserGroup> groups) {
        this.groups = new HashSet<>(groups);
    }

    public void setKeyCategories(Set<KeyCategory> keyCategories) {
        this.keyCategories = keyCategories;
    }

    public void setKeys(Set<Key> keys) {
        this.keys = keys;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setResponsibleKeyCategories(Set<KeyCategory> responsibleKeyCategories) {
        this.responsibleKeyCategories = responsibleKeyCategories;
    }

    public void setRole(UserRoleType role) {
        this.role = role.name();
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
