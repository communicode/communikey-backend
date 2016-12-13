/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2016
 */
package de.communicode.communikey.domain;

import static de.communicode.communikey.config.DataSourceConfig.USER_GROUPS;
import static de.communicode.communikey.config.DataSourceConfig.USER_GROUP_NAME;
import static de.communicode.communikey.config.DataSourceConfig.USER_GROUP_ID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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
@Table(name = USER_GROUPS)
public class UserGroup {
    @Id
    @Column(name = USER_GROUP_ID, nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = USER_GROUP_NAME, nullable = false)
    private String name;

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "groups")
    @JsonIgnoreProperties(value = {"groups", "roles", "credentialsNonExpired", "accountNonExpired", "accountNonLocked", "enabled"})
    private Set<User> users;

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "groups")
    @JsonIgnoreProperties(value = {"groups", "creator", "responsible", "parent"})
    private Set<KeyCategory> categories;

    private UserGroup() {}

    /**
     * Constructs a new user group entity with the specified attributes and an auto-generated ID with no {@link User} assigned to.
     *
     * @param name the name of the user group
     */
    public UserGroup(String name) {
        this.name = name;
    }

    public Set<KeyCategory> getCategories() {
        return categories;
    }

    public long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setCategories(Set<KeyCategory> categories) {
        this.categories = categories;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }
}
