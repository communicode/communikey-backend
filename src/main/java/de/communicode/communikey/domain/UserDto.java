/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2016
 */
package de.communicode.communikey.domain;

import java.util.Set;

/**
 * A data transfer object for a {@link User} entity.
 *
 * @author sgreb@communicode.de
 * @since 0.2.0
 */
public class UserDto {

    private long id;

    private Set<Key> keys;

    private Set<KeyCategory> keyCategories;

    private Set<KeyCategory> responsibleKeyCategories;

    private Set<UserGroup> groups;

    private boolean isEnabled;

    private String username;

    private String role;

    public long getId() {
        return id;
    }

    public Set<Key> getKeys() {
        return keys;
    }

    public Set<KeyCategory> getKeyCategories() {
        return keyCategories;
    }

    public Set<KeyCategory> getResponsibleKeyCategories() {
        return responsibleKeyCategories;
    }

    public Set<UserGroup> getGroups() {
        return groups;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public String getUsername() {
        return username;
    }

    public String getRole() {
        return role;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setKeys(Set<Key> keys) {
        this.keys = keys;
    }

    public void setKeyCategories(Set<KeyCategory> keyCategories) {
        this.keyCategories = keyCategories;
    }

    public void setResponsibleKeyCategories(Set<KeyCategory> responsibleKeyCategories) {
        this.responsibleKeyCategories = responsibleKeyCategories;
    }

    public void setGroups(Set<UserGroup> groups) {
        this.groups = groups;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
