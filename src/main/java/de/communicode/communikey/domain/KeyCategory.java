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
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.google.common.collect.Sets;
import de.communicode.communikey.service.view.AuthoritiesRestView;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.NotBlank;

import java.io.Serializable;
import java.util.Set;

/**
 * Represents a key category.
 *
 * @author sgreb@communicode.de
 * @author dvonderbey@communicode.de
 * @see <a href="https://en.wikipedia.org/wiki/Tree_(data_structure)#Terminology_used_in_trees">Wikipedia - Terminology used in trees</a>
 * @since 0.2.0
 */
@Entity
@Table(name = "key_categories")
public class KeyCategory extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = 1;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @Column
    @JsonProperty("id")
    private String hashid;

    @Column(nullable = false)
    @NotBlank
    private String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "category")
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    @JsonView(AuthoritiesRestView.Admin.class)
    private final Set<Key> keys = Sets.newConcurrentHashSet();

    @ManyToOne
    @JoinColumn
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    private KeyCategory parent;

    @NotNull
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parent")
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    private final Set<KeyCategory> children = Sets.newConcurrentHashSet();

    @NotNull
    private int treeLevel = 0;

    @ManyToOne
    @JoinColumn(name = "creator_user_id", nullable = false)
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    private User creator;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "key_categories_user_groups",
        joinColumns = {@JoinColumn(name = "key_category_id", referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name = "user_group_id", referencedColumnName = "id")})
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    @JsonView(AuthoritiesRestView.Admin.class)
    private final Set<UserGroup> groups = Sets.newConcurrentHashSet();

    @ManyToOne
    @JoinColumn(name = "responsible_user_id")
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    private User responsible;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the Hashid
     * @since 0.13.0
     */
    public String getHashid() {
        return hashid;
    }

    /**
     * @param hashid the Hashid
     * @since 0.13.0
     */
    public void setHashid(String hashid) {
        this.hashid = hashid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean addKey(Key key) {
        return keys.add(key);
    }

    public boolean addKeys(Set<Key> keys) {
        return this.keys.addAll(keys);
    }

    public boolean removeKey(Key key) {
        return keys.remove(key);
    }

    public boolean removeKeys(Set<Key> keys) {
        return this.keys.removeAll(keys);
    }

    public void removeAllKeys() {
        keys.clear();
    }

    public Set<Key> getKeys() {
        return Sets.newConcurrentHashSet(keys);
    }

    public KeyCategory getParent() {
        return parent;
    }

    public void setParent(KeyCategory parent) {
        this.parent = parent;
    }

    public boolean addChild(KeyCategory keyCategory) {
        return children.add(keyCategory);
    }

    public boolean addChildrens(Set<KeyCategory> keyCategories) {
        return children.addAll(keyCategories);
    }

    public boolean removeChild(KeyCategory keyCategory) {
        return children.remove(keyCategory);
    }

    public boolean removeChildren(Set<KeyCategory> keyCategories) {
        return children.removeAll(keyCategories);
    }

    public void removeAllChildren() {
        children.clear();
    }

    public Set<KeyCategory> getChildren() {
        return Sets.newConcurrentHashSet(children);
    }

    public int getTreeLevel() {
        return treeLevel;
    }

    public void setTreeLevel(int treeLevel) {
        this.treeLevel = treeLevel;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public boolean addGroup(UserGroup userGroup) {
        return groups.add(userGroup);
    }

    public boolean addGroups(Set<UserGroup> userGroups) {
        return groups.addAll(userGroups);
    }

    public boolean removeGroup(UserGroup userGroup) {
        return groups.remove(userGroup);
    }

    public boolean removeGroups(Set<UserGroup> userGroups) {
        return groups.removeAll(userGroups);
    }

    public void removeAllGroups() {
        groups.clear();
    }

    public Set<UserGroup> getGroups() {
        return Sets.newConcurrentHashSet(groups);
    }

    public User getResponsible() {
        return responsible;
    }

    public void setResponsible(User responsible) {
        this.responsible = responsible;
    }
}
