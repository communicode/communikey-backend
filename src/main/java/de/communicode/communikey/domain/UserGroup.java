/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2017
 */
package de.communicode.communikey.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.google.common.collect.Sets;
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

    private static final long serialVersionUID = 1;

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
        name = "user_groups_users",
        joinColumns = @JoinColumn(name = "user_group_id", referencedColumnName = "id"),
        inverseJoinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")})
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    @JsonView(AuthoritiesRestView.Admin.class)
    private final Set<User> users = Sets.newConcurrentHashSet();

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "groups")
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    @JsonView(AuthoritiesRestView.Admin.class)
    private final Set<KeyCategory> categories = Sets.newConcurrentHashSet();

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

    public boolean addUser(User user) {
        return users.add(user);
    }

    public boolean addUsers(Set<User> user) {
        return users.addAll(user);
    }

    public boolean removeUser(User user) {
        return users.remove(user);
    }

    public boolean removeUsers(Set<User> users) {
        return this.users.removeAll(users);
    }

    public void removeAllUser() {
        users.clear();
    }

    public Set<User> getUsers() {
        return Sets.newConcurrentHashSet(users);
    }

    public boolean addCategory(KeyCategory keyCategory) {
        return categories.add(keyCategory);
    }

    public boolean addCategories(Set<KeyCategory> keyCategories) {
        return categories.addAll(keyCategories);
    }

    public boolean removeCategory(KeyCategory keyCategory) {
        return categories.remove(keyCategory);
    }

    public boolean removeCategories(Set<KeyCategory> keyCategories) {
        return categories.removeAll(keyCategories);
    }

    public void removeAllCategories() {
        categories.clear();
    }

    public Set<KeyCategory> getCategories() {
        return Sets.newConcurrentHashSet(categories);
    }

    @Override
    public String toString() {
        return "UserGroup{" +"id=" + id +
                ", name='" + name + '\'' +
                ", users=" + users +
                ", categories=" + categories +
                '}';
    }
}
