/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2016
 */
package de.communicode.communikey.domain;

/**
 * A data transfer object for a {@link User} entity.
 *
 * @author sgreb@communicode.de
 * @since 0.2.0
 */
public class UserDto {

    private long id;

    private long[] keys;

    private long[] keyCategories;

    private long[] responsibleKeyCategories;

    private long[] groups;

    private boolean isEnabled;

    private String username;

    private String role;

    public long getId() {
        return id;
    }

    public long[] getKeys() {
        return keys;
    }

    public long[] getKeyCategories() {
        return keyCategories;
    }

    public long[] getResponsibleKeyCategories() {
        return responsibleKeyCategories;
    }

    public long[] getGroups() {
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

    public void setKeys(long[] keys) {
        this.keys = keys;
    }

    public void setKeyCategories(long[] keyCategories) {
        this.keyCategories = keyCategories;
    }

    public void setResponsibleKeyCategories(long[] responsibleKeyCategories) {
        this.responsibleKeyCategories = responsibleKeyCategories;
    }

    public void setGroups(long[] groups) {
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
