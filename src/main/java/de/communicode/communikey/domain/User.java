/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2016
 */
package de.communicode.communikey.domain;

import static de.communicode.communikey.config.DataSourceConfig.ROLE_ID;
import static de.communicode.communikey.config.DataSourceConfig.USERS_GROUPS;
import static de.communicode.communikey.config.DataSourceConfig.USERS;
import static de.communicode.communikey.config.DataSourceConfig.USERS_ROLES;
import static de.communicode.communikey.config.DataSourceConfig.USER_ENABLED;
import static de.communicode.communikey.config.DataSourceConfig.USER_ID;
import static de.communicode.communikey.config.DataSourceConfig.USER_GROUP_ID;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

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
@Table(name = USERS)
public class User {
    @Id
    @Column(name = USER_ID, nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "creator")
    @JsonBackReference
    private Set<Key> keys;

    @OneToMany(mappedBy = "creator")
    @JsonBackReference
    private Set<KeyCategory> keyCategories;

    @OneToMany(mappedBy = "responsible")
    @JsonBackReference
    private Set<KeyCategory> responsibleKeyCategories;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
        name = USERS_GROUPS,
        joinColumns = @JoinColumn(name = USER_ID, nullable = false, updatable = false),
        inverseJoinColumns = @JoinColumn(name = USER_GROUP_ID, nullable = false))
    @JsonBackReference
    private Set<UserGroup> groups;

    @Column(name = USER_ENABLED, nullable = false)
    private boolean isEnabled;

    @JsonIgnore
    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String username;

    @ManyToMany
    @JoinTable(
        name = USERS_ROLES,
        joinColumns = @JoinColumn(name = USER_ID, referencedColumnName = USER_ID),
        inverseJoinColumns = @JoinColumn(name = ROLE_ID, referencedColumnName = "id"))
    @JsonBackReference
    private Set<Role> roles;

    private User() {}

    /**
     * Constructs a new user entity with the specified username and password.
     *
     * @param username the name of the user
     * @param password the password of the user
     */
    public User(String username, String password) {
        this.username = username;
        this.password = password;
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

    public Set<Role> getRoles() {
        return roles;
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

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
