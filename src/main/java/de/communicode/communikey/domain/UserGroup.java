/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2016
*/
package de.communicode.communikey.domain;

import static de.communicode.communikey.config.CommunikeyConstants.TABLE_USER_GROUPS;
import static de.communicode.communikey.config.CommunikeyConstants.TABLE_USER_GROUPS_COLUMN_NAME;
import static de.communicode.communikey.config.CommunikeyConstants.TABLE_USER_GROUPS_COLUMN_USER_GROUP_ID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import java.util.Set;

/**
 * Represents a user group entity.
 *
 * @author sgreb@communicode.de
 * @since 0.2.0
 */
@Entity
@Table(name = TABLE_USER_GROUPS)
public class UserGroup {
    @Id
    @Column(name = TABLE_USER_GROUPS_COLUMN_USER_GROUP_ID, nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = TABLE_USER_GROUPS_COLUMN_NAME, nullable = false)
    private String name;

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "groups")
    private Set<User> users;

    private UserGroup() {}

    /**
     * Constructs a new user group entity object with an auto-generated ID.
     *
     * @param name the name of the user group
     */
    public UserGroup(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
