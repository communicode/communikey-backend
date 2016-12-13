/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2016
 */
package de.communicode.communikey.domain;

import static de.communicode.communikey.config.DataSourceConfig.CATEGORIES;
import static de.communicode.communikey.config.DataSourceConfig.CREATOR_USER_ID;
import static de.communicode.communikey.config.DataSourceConfig.KEY_CATEGORY_ID;
import static de.communicode.communikey.config.DataSourceConfig.RESPONSIBLE_USER_ID;
import static de.communicode.communikey.config.DataSourceConfig.USER_GROUPS_KEY_CATEGORIES;
import static de.communicode.communikey.config.DataSourceConfig.USER_GROUP_ID;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.CascadeType;
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

import java.util.Optional;
import java.util.Set;

/**
 * Represents a key category entity.
 *
 * @author sgreb@communicode.de
 * @since 0.2.0
 */
@Entity
@Table(name = CATEGORIES)
public class KeyCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = KEY_CATEGORY_ID, nullable = false)
    private long id;

    @OneToMany(mappedBy = "parent")
    @JsonBackReference
    private Set<KeyCategory> childs;

    @ManyToOne
    @JoinColumn(name = CREATOR_USER_ID, nullable = false)
    @JsonManagedReference
    @JsonIgnoreProperties(value = {"roles", "groups", "credentialsNonExpired", "accountNonExpired", "accountNonLocked", "enabled"})
    private User creator;

    @ManyToOne
    @JoinColumn(name = RESPONSIBLE_USER_ID, nullable = false)
    @JsonManagedReference
    @JsonIgnoreProperties(value = {"roles", "groups", "credentialsNonExpired", "accountNonExpired", "accountNonLocked", "enabled"})
    private User responsible;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "category")
    @JsonBackReference
    private Set<Key> keys;

    private String name;

    @ManyToOne
    @JoinColumn
    @JsonManagedReference
    private KeyCategory parent;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = USER_GROUPS_KEY_CATEGORIES,
        joinColumns = @JoinColumn(name = KEY_CATEGORY_ID, nullable = false, updatable = false),
        inverseJoinColumns = @JoinColumn(name = USER_GROUP_ID, nullable = false))
    @JsonIgnoreProperties(value = {"categories", "users"})
    private Set<UserGroup> groups;

    private KeyCategory() {}

    /**
     * Constructs a new key category entity with the given value and an auto-generated ID.
     *
     * @param name the name of the key category
     * @param creator the user who created this key category
     */
    public KeyCategory(String name, User creator) {
        this.name = name;
        this.creator = creator;
    }

    public Set<KeyCategory> getChilds() {
        return childs;
    }

    public User getCreator() {
        return creator;
    }

    public long getId() {
        return id;
    }

    public Set<Key> getKeys() {
        return keys;
    }

    public String getName() {
        return name;
    }

    public Optional<KeyCategory> getParent() {
        return Optional.ofNullable(parent);
    }

    public User getResponsible() {
        return responsible;
    }

    public Set<UserGroup> getGroups() {
        return groups;
    }

    public void setChilds(Set<KeyCategory> childs) {
        this.childs = childs;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setKeys(Set<Key> keys) {
        this.keys = keys;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setParent(KeyCategory parent) {
        this.parent = parent;
    }

    public void setResponsible(User responsible) {
        this.responsible = responsible;
    }

    public void setGroups(Set<UserGroup> groups) {
        this.groups = groups;
    }
}