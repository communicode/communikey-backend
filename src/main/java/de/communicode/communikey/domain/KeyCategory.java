/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2017
 */
package de.communicode.communikey.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.validator.constraints.NotBlank;

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
import javax.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.HashSet;
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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(length = 100, nullable = false)
    private String name;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "category")
    @JsonIgnoreProperties(value = {"createdBy", "createdDate", "lastModifiedBy", "lastModifiedDate", "creator", "category"})
    private Set<Key> keys = new HashSet<>();

    @ManyToOne
    @JoinColumn
    @JsonIgnoreProperties(value = {"groups", "children", "keys"})
    private KeyCategory parent = null;

    @NotNull
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE, mappedBy = "parent")
    @JsonIgnoreProperties(value = {"groups", "parent", "keys"})
    private Set<KeyCategory> children = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "creator_user_id", nullable = false)
    @JsonIgnoreProperties(value = {
        "createdBy", "createdDate", "lastModifiedBy", "lastModifiedDate", "activated", "activationKey", "resetKey", "resetDate",
        "authorities", "groups", "keyCategories", "responsibleKeyCategories"})
    private User creator;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "key_categories_user_groups",
        joinColumns = {@JoinColumn(name = "key_category_id", referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name = "user_group_id", referencedColumnName = "id")})
    @JsonIgnoreProperties(value = {"categories", "users"})
    private Set<UserGroup> groups = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "responsible_user_id")
    @JsonIgnoreProperties(value = {
        "createdBy", "createdDate", "lastModifiedBy", "lastModifiedDate", "activated", "activationKey", "resetKey", "resetDate",
        "authorities", "groups", "keyCategories", "responsibleKeyCategories"})
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

    public Set<Key> getKeys() {
        return keys;
    }

    public void setKeys(Set<Key> keys) {
        this.keys = keys;
    }

    public KeyCategory getParent() {
        return parent;
    }

    public void setParent(KeyCategory parent) {
        this.parent = parent;
    }

    public Set<KeyCategory> getChildren() {
        return children;
    }

    public void setChildren(Set<KeyCategory> children) {
        this.children = children;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public Set<UserGroup> getGroups() {
        return groups;
    }

    public void setGroups(Set<UserGroup> groups) {
        this.groups = groups;
    }

    public User getResponsible() {
        return responsible;
    }

    public void setResponsible(User responsible) {
        this.responsible = responsible;
    }
}