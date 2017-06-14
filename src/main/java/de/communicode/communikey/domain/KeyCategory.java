/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2017
 */
package de.communicode.communikey.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.google.common.collect.Sets;
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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.Set;

/**
 * Represents a key category.
 *
 * @author sgreb@communicode.de
 * @see <a href="https://en.wikipedia.org/wiki/Tree_(data_structure)#Terminology_used_in_trees">Wikipedia - Terminology used in trees</a>
 * @since 0.2.0
 */
@Entity
@Table(name = "key_categories")
public class KeyCategory extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = 1;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    @NotBlank
    private String name;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "category")
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    private final Set<Key> keys = Sets.newConcurrentHashSet();

    @ManyToOne
    @JoinColumn
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    private KeyCategory parent;

    @NotNull
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "parent")
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    private final Set<KeyCategory> children = Sets.newConcurrentHashSet();

    @ManyToOne
    @JoinColumn(name = "creator_user_id", nullable = false)
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    private User creator;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "key_categories_user_groups",
        joinColumns = {@JoinColumn(name = "key_category_id", referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name = "user_group_id", referencedColumnName = "id")})
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
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