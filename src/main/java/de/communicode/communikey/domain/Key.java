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
import javax.persistence.GenerationType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import java.io.Serializable;

/**
 * Represents a key.
 *
 * @author sgreb@communicode.de
 * @since 0.1.0
 */
@Entity
@Table(name = "\"keys\"")
public class Key extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = 1;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 100)
    @Column(length = 100, nullable = false)
    private String name;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "key_category_id")
    @JsonIgnoreProperties(value = {"createdBy", "createdDate", "lastModifiedBy", "lastModifiedDate", "parent", "children", "creator", "responsible", "keys",
            "groups"})
    private KeyCategory category;

    @ManyToOne
    @JoinColumn(name = "creator_user_id", nullable = false)
    @JsonIgnoreProperties(value = {"createdBy", "createdDate", "lastModifiedBy", "lastModifiedDate", "activated", "activationKey", "resetKey", "resetDate",
            "authorities", "groups", "keyCategories", "responsibleKeyCategories", "keys"})
    private User creator;

    @NotNull
    @Column(nullable = false)
    private String password;

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

    public KeyCategory getCategory() {
        return category;
    }

    public void setCategory(KeyCategory category) {
        this.category = category;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Key{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", category=" + category +
            ", creator=" + creator +
            '}';
    }
}