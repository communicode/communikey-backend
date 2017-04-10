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
import com.google.common.collect.Sets;
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
    private Set<Authority> authorities = Sets.newConcurrentHashSet();

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "users")
    @JsonIgnoreProperties(value = {"users", "createdBy", "createdDate", "lastModifiedBy", "lastModifiedDate", "categories"})
    @JsonView(AuthoritiesRestView.Admin.class)
    private Set<UserGroup> groups = Sets.newConcurrentHashSet();

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "creator")
    @JsonIgnoreProperties(value = {"createdBy", "createdDate", "lastModifiedBy", "lastModifiedDate", "category", "creator", "password"})
    private Set<Key> keys = Sets.newConcurrentHashSet();

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "creator")
    @JsonIgnoreProperties(value = {"createdBy", "createdDate", "lastModifiedBy", "lastModifiedDate", "parent", "children", "creator", "responsible", "groups",
            "keys"})
    @JsonView(AuthoritiesRestView.Admin.class)
    private Set<KeyCategory> keyCategories = Sets.newConcurrentHashSet();

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "responsible")
    @JsonIgnoreProperties(value = {"children", "responsible", "keys", "parent", "groups", "creator", "createdBy", "createdDate", "lastModifiedBy",
        "lastModifiedDate"})
    @JsonView(AuthoritiesRestView.Admin.class)
    private Set<KeyCategory> responsibleKeyCategories = Sets.newConcurrentHashSet();

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

    public boolean addAuthority(Authority authority) {
        return authorities.add(authority);
    }

    public boolean addAuthorities(Set<Authority> authorities) {
        return this.authorities.addAll(authorities);
    }

    public boolean removeAuthority(Authority authority) {
        return authorities.remove(authority);
    }

    public boolean removeAuthorities(Set<Authority> authorities) {
        return this.authorities.removeAll(authorities);
    }

    public void removeAllAuthorities() {
        authorities.clear();
    }

    public Set<Authority> getAuthorities() {
        return authorities;
    }

    public boolean addGroup(UserGroup userGroup) {
        return groups.add(userGroup);
    }

    public boolean addGroups(Set<UserGroup> userGroups) {
        return this.groups.addAll(userGroups);
    }

    public boolean removeGroup(UserGroup userGroups) {
        return groups.remove(userGroups);
    }

    public boolean removeGroups(Set<UserGroup> userGroups) {
        return this.groups.removeAll(userGroups);
    }

    public void removeAllGroups() {
        groups.clear();
    }

    public Set<UserGroup> getGroups() {
        return groups;
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
        return keys;
    }

    public boolean addKeyCategory(KeyCategory keyCategory) {
        return keyCategories.add(keyCategory);
    }

    public boolean addKeyCategories(Set<KeyCategory> keyCategories) {
        return this.keyCategories.addAll(keyCategories);
    }

    public boolean removeKeyCategory(KeyCategory keyCategory) {
        return keyCategories.remove(keyCategory);
    }

    public boolean removeKeyCategories(Set<KeyCategory> keyCategories) {
        return this.keyCategories.removeAll(keyCategories);
    }

    public void removeAllKeyCategories() {
        keyCategories.clear();
    }

    public Set<KeyCategory> getKeyCategories() {
        return responsibleKeyCategories;
    }

    public boolean addResponsibleKeyCategory(KeyCategory responsibleKeyCategory) {
        return responsibleKeyCategories.add(responsibleKeyCategory);
    }

    public boolean addResponsibleKeyCategories(Set<KeyCategory> responsibleKeyCategories) {
        return this.responsibleKeyCategories.addAll(responsibleKeyCategories);
    }

    public boolean removeResponsibleKeyCategory(KeyCategory responsibleKeyCategory) {
        return responsibleKeyCategories.remove(responsibleKeyCategory);
    }

    public boolean removeResponsibleKeyCategories(Set<KeyCategory> responsibleKeyCategories) {
        return this.responsibleKeyCategories.removeAll(responsibleKeyCategories);
    }

    public void removeAllResponsibleKeyCategories() {
        responsibleKeyCategories.clear();
    }

    public Set<KeyCategory> getResponsibleKeyCategories() {
        return responsibleKeyCategories;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", activated=" + activated +
                ", activationKey='" + activationKey + '\'' +
                ", resetKey='" + resetKey + '\'' +
                ", resetDate=" + resetDate +
                ", authorities=" + authorities +
                ", groups=" + groups +
                ", keys=" + keys +
                ", keyCategories=" + keyCategories +
                ", responsibleKeyCategories=" + responsibleKeyCategories +
                '}';
    }
}
