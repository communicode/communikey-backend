/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2016
 */
package de.communicode.communikey.domain;

import static de.communicode.communikey.config.DataSourceConfig.PRIVILEGE_ID;
import static de.communicode.communikey.config.DataSourceConfig.ROLES_PRIVILEGES;
import static de.communicode.communikey.config.DataSourceConfig.ROLE_ID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import java.util.Set;

/**
 * Represents a role entity.
 *
 * @author sgreb@communicode.de
 * @since 0.2.0
 */
@Entity
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToMany(mappedBy = "roles")
    private Set<User> users;

    @ManyToMany
    @JoinTable(
        name = ROLES_PRIVILEGES,
        joinColumns = @JoinColumn(name = ROLE_ID, referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = PRIVILEGE_ID, referencedColumnName = "id")
    )
    private Set<Privilege> privileges;

    private String name;

    private Role() {}

    /**
     * Constructs a new role entity with the specified name.
     *
     * @param name the name of the role
     */
    public Role(final String name) {
        super();
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Set<Privilege> getPrivileges() {
        return privileges;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setId(final long id) {
        this.id = id;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public void setPrivileges(final Set<Privilege> privileges) {
        this.privileges = privileges;
    }

    public void setUsers(final Set<User> users) {
        this.users = users;
    }
}
