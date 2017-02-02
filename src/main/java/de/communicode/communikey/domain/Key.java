/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2017
 */
package de.communicode.communikey.domain;

import static de.communicode.communikey.config.DataSourceConfig.KEYS;
import static de.communicode.communikey.config.DataSourceConfig.CREATOR_USER_ID;
import static de.communicode.communikey.config.DataSourceConfig.KEY_CATEGORY_ID;
import static de.communicode.communikey.config.DataSourceConfig.KEY_ID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GenerationType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Represents a key entity.
 *
 * @author sgreb@communicode.de
 * @since 0.1.0
 */
@Entity
@Table(name = KEYS)
public class Key implements Serializable {
    @Id
    @Column(name = KEY_ID, nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = CREATOR_USER_ID, nullable = false)
    @JsonIgnoreProperties(value = {"roles", "groups", "credentialsNonExpired", "accountNonExpired", "accountNonLocked", "enabled", "keys", "keyCategories",
        "responsibleKeyCategories"})
    @CreatedBy
    private User creator;

    @ManyToOne
    @JoinColumn(name = KEY_CATEGORY_ID)
    @JsonIgnoreProperties(value = {"creator", "children", "responsible", "keys", "parent", "groups"})
    private KeyCategory category;

    private String name;

    @JsonProperty("created")
    @CreationTimestamp
    private Timestamp creationTimestamp;

    @JsonProperty("updated")
    @UpdateTimestamp
    private Timestamp updateTimestamp;

    private String value;

    private Key() {}

    /**
     * Constructs a new key entity with the given attributes, an auto-generated ID and creation timestamp.
     *
     * @param name the name of the key
     * @param value the value of the key
     * @param creator the user who created this key
     */
    public Key(String name, String value, User creator) {
        this.creator = creator;
        this.value = value;
        this.name = name.trim();
    }

    public KeyCategory getCategory() {
        return this.category;
    }

    public Timestamp getCreationTimestamp() {
        return creationTimestamp;
    }

    public User getCreator() {
        return creator;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Timestamp getUpdateTimestamp() {
        return updateTimestamp;
    }

    public String getValue() {
        return value;
    }

    public void setCategory(KeyCategory category) {
        this.category = category;
    }

    public void setCreationTimestamp(Timestamp creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUpdateTimestamp(Timestamp updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }

    public void setValue(String value) {
        this.value = value;
    }
}