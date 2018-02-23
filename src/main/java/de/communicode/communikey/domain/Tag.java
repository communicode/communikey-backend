/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2017
 */
package de.communicode.communikey.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.google.common.collect.Sets;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Set;

/**
 * Represents a key.
 *
 * @author dvonderbey@communicode.de
 * @since 0.18.0
 */
@Entity
@Table(name = "\"tags\"")
public class Tag extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = 1;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @Column
    @JsonProperty("id")
    private String hashid;

    @NotBlank
    @Column(nullable = false)
    private String name;

    @NotBlank
    @Column(nullable = false)
    private String color;

    @ManyToOne
    @JoinColumn(name = "creator_user_id", nullable = false)
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    private User creator;

    @ManyToMany
    @JoinColumn(name = "key_id")
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    private Set<Key> keys = Sets.newConcurrentHashSet();

    @ManyToMany
    @JoinColumn(name = "key_categories_id")
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    private Set<KeyCategory> keyCategories = Sets.newConcurrentHashSet();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the Hashid
     */
    public String getHashid() {
        return hashid;
    }

    /**
     * @param hashid the Hashid
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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public boolean addKeyCategory(KeyCategory keyCategory) {
        return this.keyCategories.add(keyCategory);
    }

    public boolean addKeyCategories(Set<KeyCategory> keyCategories) {
        return this.keyCategories.addAll(keyCategories);
    }

    public boolean removeKeyCategory(KeyCategory key) {
        return this.keyCategories.remove(key);
    }

    public boolean removeKeyCategories(Set<KeyCategory> keyCategories) {
        return this.keyCategories.removeAll(keyCategories);
    }

    public void removeAllKeyCategories() {
        this.keyCategories.clear();
    }

    public Set<KeyCategory> getKeyCategories() {
        return Sets.newConcurrentHashSet(this.keyCategories);
    }

    public boolean addKey(Key key) {
        return this.keys.add(key);
    }

    public boolean addKeys(Set<Key> keys) {
        return this.keys.addAll(keys);
    }

    public boolean removeKey(Key key) {
        return this.keys.remove(key);
    }

    public boolean removeKeys(Set<Key> keys) {
        return this.keys.removeAll(keys);
    }

    public void removeAllKeys() {
        this.keys.clear();
    }

    public Set<Key> getKeys() {
        return Sets.newConcurrentHashSet(this.keys);
    }

    @Override
    public String toString() {
        return "Tag{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", color='" + color + '\'' +
            ", creator=" + creator +
            '}';
    }
}
