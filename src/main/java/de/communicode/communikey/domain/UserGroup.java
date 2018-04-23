/*
 * This file is part of communikey.
 * Copyright (C) 2016-2018  communicode AG <communicode.de>
 *
 * communikey is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package de.communicode.communikey.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.google.common.collect.Sets;
import de.communicode.communikey.service.view.AuthoritiesRestView;

import javax.validation.constraints.NotBlank;
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
    @Column(unique = true, nullable = false)
    private String name;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "user_groups_users",
        joinColumns = @JoinColumn(name = "user_group_id", referencedColumnName = "id"),
        inverseJoinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")})
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    @JsonView(AuthoritiesRestView.Admin.class)
    private final Set<User> users = Sets.newConcurrentHashSet();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "groups")
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
