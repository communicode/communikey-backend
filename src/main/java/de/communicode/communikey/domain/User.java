/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2016
 */
package de.communicode.communikey.domain;

import static de.communicode.communikey.config.DataSourceConfig.ROLE_ID;
import static de.communicode.communikey.config.DataSourceConfig.USERS_GROUPS;
import static de.communicode.communikey.config.DataSourceConfig.USERS;
import static de.communicode.communikey.config.DataSourceConfig.USERS_ROLES;
import static de.communicode.communikey.config.DataSourceConfig.USER_ID;
import static de.communicode.communikey.config.DataSourceConfig.USER_GROUP_ID;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

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
import javax.persistence.OneToMany;
import javax.persistence.Table;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Represents a user entity.
 *
 * @author sgreb@communicode.de
 * @since 0.2.0
 */
@Entity
@Table(name = USERS)
public class User implements UserDetails {
    @Id
    @Column(name = USER_ID, nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "creator")
    @JsonBackReference
    private Set<Key> keys;

    @OneToMany(mappedBy = "creator")
    @JsonBackReference
    private Set<KeyCategory> keyCategories;

    @OneToMany(mappedBy = "responsible")
    @JsonBackReference
    private Set<KeyCategory> responsibleKeyCategories;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
        name = USERS_GROUPS,
        joinColumns = @JoinColumn(name = USER_ID, nullable = false, updatable = false),
        inverseJoinColumns = @JoinColumn(name = USER_GROUP_ID, nullable = false))
    @JsonIgnoreProperties(value = {"users", "categories"})
    private Set<UserGroup> groups;

    @Column(nullable = false)
    private boolean isEnabled;

    @Column(nullable = false)
    private boolean isAccountNonExpired;

    @Column(nullable = false)
    private boolean isAccountNonLocked;

    @Column(nullable = false)
    private boolean isCredentialsNonExpired;

    @JsonIgnore
    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String email;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = USERS_ROLES,
        joinColumns = @JoinColumn(name = USER_ID, referencedColumnName = USER_ID),
        inverseJoinColumns = @JoinColumn(name = ROLE_ID, referencedColumnName = "id"))
    @JsonManagedReference
    @JsonIgnoreProperties("privileges")
    private Set<Role> roles;

    private User() {}

    /**
     * Constructs a new user entity with the specified email and password.
     *
     * @param email the email of the user
     * @param password the password of the user
     */
    public User(String email, String password) {
        this.email = email;
        this.password = password;
        isEnabled = false;
        isAccountNonExpired = true;
        isAccountNonLocked = true;
        isCredentialsNonExpired = true;
    }

    public String getEmail() {
        return email;
    }

    public Set<UserGroup> getGroups() {
        return new HashSet<>(groups);
    }

    public long getId() {
        return id;
    }

    public Set<KeyCategory> getKeyCategories() {
        return keyCategories;
    }

    public Set<Key> getKeys() {
        return keys;
    }

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getGrantedAuthorities(getPrivileges(roles));
    }

    public String getPassword() {
        return password;
    }

    private List<String> getPrivileges(Collection<Role> roles) {
        List<String> privileges = new ArrayList<>();
        List<Privilege> collection = new ArrayList<>();

        for (Role role : roles) {
            collection.addAll(role.getPrivileges());
        }
        for (Privilege privilege : collection) {
            privileges.add(privilege.getName());
        }
        return privileges;
    }

    private List<GrantedAuthority> getGrantedAuthorities(List<String> privileges) {
        List<GrantedAuthority> authorities = new ArrayList<>();

        for (String privilege : privileges) {
            authorities.add(new SimpleGrantedAuthority(privilege));
        }
        return authorities;
    }

    public Set<KeyCategory> getResponsibleKeyCategories() {
        return responsibleKeyCategories;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public void setGroups(Set<UserGroup> groups) {
        this.groups = new HashSet<>(groups);
    }

    @Override
    @JsonIgnore
    public String getUsername() {
        return email;
    }

    public User setAccountNonExpired(boolean accountNonExpired) {
        isAccountNonExpired = accountNonExpired;
        return this;
    }

    public User setAccountNonLocked(boolean accountNonLocked) {
        isAccountNonLocked = accountNonLocked;
        return this;
    }

    public User setCredentialsNonExpired(boolean credentialsNonExpired) {
        isCredentialsNonExpired = credentialsNonExpired;
        return this;
    }

    public void setKeyCategories(Set<KeyCategory> keyCategories) {
        this.keyCategories = keyCategories;
    }

    public void setKeys(Set<Key> keys) {
        this.keys = keys;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setResponsibleKeyCategories(Set<KeyCategory> responsibleKeyCategories) {
        this.responsibleKeyCategories = responsibleKeyCategories;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}
