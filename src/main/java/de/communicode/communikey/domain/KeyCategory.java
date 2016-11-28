/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2016
 */
package de.communicode.communikey.domain;

import static de.communicode.communikey.config.CommunikeyConstants.TABLE_CATEGORIES;
import static de.communicode.communikey.config.CommunikeyConstants.TABLE_CATEGORIES_COLUMN_KEY_CATEGORY_ID;
import static de.communicode.communikey.config.CommunikeyConstants.TABLE_CATEGORIES_CREATOR_USER_ID;
import static de.communicode.communikey.config.CommunikeyConstants.TABLE_CATEGORIES_RESPONSIBLE_USER_ID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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
@Table(name = TABLE_CATEGORIES)
public class KeyCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = TABLE_CATEGORIES_COLUMN_KEY_CATEGORY_ID, nullable = false)
    private long id;

    @OneToMany(mappedBy = "parent")
    private Set<KeyCategory> childs;

    @ManyToOne
    @JoinColumn(name = TABLE_CATEGORIES_CREATOR_USER_ID, nullable = false)
    private User creator;

    @ManyToOne
    @JoinColumn(name = TABLE_CATEGORIES_RESPONSIBLE_USER_ID, nullable = false)
    private User responsible;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "category")
    private Set<Key> keys;

    private String name;

    @ManyToOne
    @JoinColumn
    private KeyCategory parent;

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
}