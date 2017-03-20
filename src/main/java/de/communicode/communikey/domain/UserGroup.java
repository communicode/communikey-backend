/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2017
 */
package de.communicode.communikey.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import de.communicode.communikey.service.view.AuthoritiesRestView;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a user group.
 *
 * @author sgreb@communicode.de
 * @since 0.2.0
 */
@Entity
@Table(name = "user_groups")
public class UserGroup extends AbstractEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(AuthoritiesRestView.Admin.class)
    private Long id;

    @NotBlank
    @Size(max = 100)
    @Column(length = 100, unique = true, nullable = false)
    private String name;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "groups_users",
        joinColumns = @JoinColumn(name = "user_group_id", referencedColumnName = "id"),
        inverseJoinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")})
    @JsonIgnoreProperties(value = {"createdBy", "createdDate", "lastModifiedBy", "lastModifiedDate", "activated", "activationKey", "resetKey", "resetDate",
            "authorities", "groups", "keyCategories", "responsibleKeyCategories"})
    @JsonView(AuthoritiesRestView.Admin.class)
    private Set<User> users = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "groups")
    @JsonIgnoreProperties(value = {"children", "responsible", "keys", "parent", "groups", "creator", "createdBy", "createdDate", "lastModifiedBy",
            "lastModifiedDate"})
    @JsonView(AuthoritiesRestView.Admin.class)
    private Set<KeyCategory> categories = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public Set<KeyCategory> getCategories() {
        return categories;
    }

    public void setCategories(Set<KeyCategory> categories) {
        this.categories = categories;
    }
}
