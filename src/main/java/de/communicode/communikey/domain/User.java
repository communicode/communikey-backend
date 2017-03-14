/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2017
 */
package de.communicode.communikey.domain;

import static de.communicode.communikey.config.SecurityConfig.EMAIL_REGEX;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import de.communicode.communikey.service.view.AuthoritiesRestView;
import org.hibernate.annotations.BatchSize;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a user.
 *
 * @author sgreb@communicode.de
 * @since 0.2.0
 */
@Entity
@Table(name = "users")
public class User extends AbstractEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(AuthoritiesRestView.Admin.class)
    private Long id;

    @NotBlank
    @Size(max = 100)
    @Column(length = 100, unique = true, nullable = false)
    @JsonView(AuthoritiesRestView.Admin.class)
    private String login;

    @NotBlank
    @Pattern(regexp = EMAIL_REGEX, message = "not a well-formed email address")
    @Size(max = 115)
    @Column(length = 115, unique = true, nullable = false)
    @JsonView(AuthoritiesRestView.Admin.class)
    private String email;

    @NotNull
    @Size(min = 60, max = 60)
    @Column(name = "password_hash", length = 60)
    @JsonIgnore
    private String password;

    @Size(max = 50)
    @Column(name = "first_name", length = 50)
    private String firstName;

    @Size(max = 50)
    @Column(name = "last_name", length = 50)
    private String lastName;

    @NotNull
    @Column(nullable = false)
    @JsonView(AuthoritiesRestView.Admin.class)
    private boolean activated = false;

    @Size(max = 20)
    @Column(name = "activation_key", length = 20)
    @JsonView(AuthoritiesRestView.Admin.class)
    private String activationKey;

    @Size(max = 20)
    @Column(name = "reset_key", length = 20)
    @JsonView(AuthoritiesRestView.Admin.class)
    private String resetKey;

    @Column(name = "reset_date")
    @JsonView(AuthoritiesRestView.Admin.class)
    private ZonedDateTime resetDate = null;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "users_authorities",
        joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name = "authority_name", referencedColumnName = "name")})
    @BatchSize(size = 20)
    @JsonView(AuthoritiesRestView.Admin.class)
    private Set<Authority> authorities = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "users")
    @JsonIgnoreProperties(value = {"users", "createdBy", "createdDate", "lastModifiedBy", "lastModifiedDate"})
    @JsonView(AuthoritiesRestView.Admin.class)
    private Set<UserGroup> groups = new HashSet<>();

    @OneToMany(mappedBy = "creator")
    @JsonIgnore
    private Set<Key> keys = new HashSet<>();

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "creator")
    @JsonIgnoreProperties(value = {"createdBy", "createdDate", "lastModifiedBy", "lastModifiedDate", "parent", "children", "creator", "responsible"})
    @JsonView(AuthoritiesRestView.Admin.class)
    private Set<KeyCategory> keyCategories = new HashSet<>();

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "responsible")
    @JsonIgnoreProperties(value = {"children", "responsible", "keys", "parent", "groups", "creator", "createdBy", "createdDate", "lastModifiedBy",
        "lastModifiedDate"})
    @JsonView(AuthoritiesRestView.Admin.class)
    private Set<KeyCategory> responsibleKeyCategories = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public String getActivationKey() {
        return activationKey;
    }

    public void setActivationKey(String activationKey) {
        this.activationKey = activationKey;
    }

    public String getResetKey() {
        return resetKey;
    }

    public void setResetKey(String resetKey) {
        this.resetKey = resetKey;
    }

    public ZonedDateTime getResetDate() {
        return resetDate;
    }

    public void setResetDate(ZonedDateTime resetDate) {
        this.resetDate = resetDate;
    }

    public Set<Authority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<Authority> authorities) {
        this.authorities = authorities;
    }

    public Set<UserGroup> getGroups() {
        return groups;
    }

    public void setGroups(Set<UserGroup> groups) {
        this.groups = groups;
    }

    public Set<Key> getKeys() {
        return keys;
    }

    public void setKeys(Set<Key> keys) {
        this.keys = keys;
    }

    public Set<KeyCategory> getKeyCategories() {
        return keyCategories;
    }

    public void setKeyCategories(Set<KeyCategory> keyCategories) {
        this.keyCategories = keyCategories;
    }

    public Set<KeyCategory> getResponsibleKeyCategories() {
        return responsibleKeyCategories;
    }

    public void setResponsibleKeyCategories(Set<KeyCategory> responsibleKeyCategories) {
        this.responsibleKeyCategories = responsibleKeyCategories;
    }
}
