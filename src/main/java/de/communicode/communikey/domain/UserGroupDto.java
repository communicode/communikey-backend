/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2016
 */
package de.communicode.communikey.domain;

/**
 * A data transfer object for a {@link UserGroup} entity.
 *
 * @author sgreb@communicode.de
 * @since 0.2.0
 */
public class UserGroupDto {

    private long id;

    private String name;

    private long[] users;

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public long[] getUsers() {
        return users;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUsers(long[] users) {
        this.users = users;
    }
}
