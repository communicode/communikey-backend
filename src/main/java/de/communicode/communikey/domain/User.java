/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2017
 */
package de.communicode.communikey.domain;

import static de.communicode.communikey.config.SecurityConfig.EMAIL_REGEX;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.google.common.collect.Sets;
import de.communicode.communikey.service.view.AuthoritiesRestView;
import org.hibernate.validator.constraints.NotBlank;

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
import javax.persistence.Column;
import javax.persistence.Lob;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
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

    private static final long serialVersionUID = 1;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(AuthoritiesRestView.Admin.class)
    private Long id;

    @NotBlank
    @Column(unique = true, nullable = false)
    @JsonView(AuthoritiesRestView.Admin.class)
    private String login;

    @NotBlank
    @Pattern(regexp = EMAIL_REGEX, message = "not a well-formed email address")
    @Column(unique = true, nullable = false)
    @JsonView(AuthoritiesRestView.Admin.class)
    private String email;

    @NotNull
    @Column(name = "password_hash")
    @JsonIgnore
    private String password;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @NotNull
    @Column(nullable = false)
    @JsonView(AuthoritiesRestView.Admin.class)
    private boolean activated = false;

    @Column(name = "activation_token")
    @JsonView(AuthoritiesRestView.Admin.class)
    private String activationToken;

    @Column(name = "reset_token")
    @JsonView(AuthoritiesRestView.Admin.class)
    private String resetToken;

    @Column(name = "reset_date")
    @JsonView(AuthoritiesRestView.Admin.class)
    private ZonedDateTime resetDate = null;

    @Lob
    @Column(name = "public_key")
    private String publicKey;

    @Column(name = "publickey_reset_token")
    @JsonView(AuthoritiesRestView.Admin.class)
    private String publicKeyResetToken;

    @Column(name = "publickey_reset_date")
    @JsonView(AuthoritiesRestView.Admin.class)
    private ZonedDateTime publicKeyResetDate = null;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "users_authorities",
        joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name = "authority_name", referencedColumnName = "name")})
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "name")
    @JsonIdentityReference(alwaysAsId = true)
    @JsonView(AuthoritiesRestView.Admin.class)
    private final Set<Authority> authorities = Sets.newConcurrentHashSet();

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "users")
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    @JsonView(AuthoritiesRestView.Admin.class)
    private final Set<UserGroup> groups = Sets.newConcurrentHashSet();

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "creator")
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    private final Set<Key> keys = Sets.newConcurrentHashSet();

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "owner")
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    private final Set<UserEncryptedPassword> encryptedPasswords = Sets.newConcurrentHashSet();

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "creator")
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    @JsonView(AuthoritiesRestView.Admin.class)
    private final Set<KeyCategory> keyCategories = Sets.newConcurrentHashSet();

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "responsible")
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    @JsonView(AuthoritiesRestView.Admin.class)
    private final Set<KeyCategory> responsibleKeyCategories = Sets.newConcurrentHashSet();

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

    public String getActivationToken() {
        return activationToken;
    }

    public void setActivationToken(String activationToken) {
        this.activationToken = activationToken;
    }

    public String getResetToken() {
        return resetToken;
    }

    public void setResetToken(String resetToken) {
        this.resetToken = resetToken;
    }

    public String getPublicKeyResetToken() {
        return publicKeyResetToken;
    }

    public void setPublicKeyResetToken(String publicKeyResetToken) {
        this.publicKeyResetToken = publicKeyResetToken;
    }

    public ZonedDateTime getPublicKeyResetDate() {
        return publicKeyResetDate;
    }

    public void setPublicKeyResetDate(ZonedDateTime publicKeyResetDate) {
        this.publicKeyResetDate = publicKeyResetDate;
    }

    public ZonedDateTime getResetDate() {
        return resetDate;
    }

    public void setResetDate(ZonedDateTime resetDate) {
        this.resetDate = resetDate;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
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
        return Sets.newConcurrentHashSet(authorities);
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
        return Sets.newConcurrentHashSet(groups);
    }

    public Set<UserEncryptedPassword> getEncryptedPasswords() {
        return Sets.newConcurrentHashSet(this.encryptedPasswords);
    }

    public boolean addUserEncryptedPassword(UserEncryptedPassword userEncryptedPassword) {
        return this.encryptedPasswords.add(userEncryptedPassword);
    }

    public boolean addUserEncryptedPasswords(Set<UserEncryptedPassword> userEncryptedPasswords) {
        return this.encryptedPasswords.addAll(userEncryptedPasswords);
    }

    public boolean removeUserEncryptedPassword(UserEncryptedPassword userEncryptedPassword) {
        return this.encryptedPasswords.remove(userEncryptedPassword);
    }

    public boolean removeUserEncryptedPasswords(Set<UserEncryptedPassword> userEncryptedPasswords) {
        return this.encryptedPasswords.removeAll(userEncryptedPasswords);
    }

    public void removeAllUserEncryptedPasswords() {
        this.encryptedPasswords.clear();
    }

    public boolean addCreatedKey(Key key) {
        return keys.add(key);
    }

    public boolean addCreatedKeys(Set<Key> keys) {
        return this.keys.addAll(keys);
    }

    public boolean removeCreatedKey(Key key) {
        return keys.remove(key);
    }

    public boolean removeCreatedKeys(Set<Key> keys) {
        return this.keys.removeAll(keys);
    }

    public void removeAllCreatedKeys() {
        keys.clear();
    }

    public Set<Key> getKeys() {
        return Sets.newConcurrentHashSet(keys);
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
        return Sets.newConcurrentHashSet(responsibleKeyCategories);
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
        return Sets.newConcurrentHashSet(responsibleKeyCategories);
    }

    public SubscriberInfo getSubscriberInfo() {
        return new SubscriberInfo(login, publicKey);
    }

    public static class SubscriberInfo {
        private String user;
        private String publicKey;

        SubscriberInfo(String user, String publicKey) {
            this.user = user;
            this.publicKey = publicKey;
        }

        public String getUser() {
            return user;
        }

        public void setUser(String user) {
            this.user = user;
        }

        public String getPublicKey() {
            return publicKey;
        }

        public void setPublicKey(String publicKey) {
            this.publicKey = publicKey;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + user.hashCode();
            result = prime * result + publicKey.hashCode();
            return result;
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof SubscriberInfo))
                return false;
            SubscriberInfo n = (SubscriberInfo) o;
            return n.user.equals(user) && n.publicKey.equals(publicKey);
        }

        @Override
        public String toString() {
            return "SubscriberInfo{" +
                   "user=" + this.user + '\'' +
                   ", publicKey=" + this.publicKey +
                   "}";
        }
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
                ", activationToken='" + activationToken + '\'' +
                ", resetToken='" + resetToken + '\'' +
                ", resetDate=" + resetDate +
                ", authorities=" + authorities +
                ", groups=" + groups +
                ", keys=" + keys +
                ", keyCategories=" + keyCategories +
                ", responsibleKeyCategories=" + responsibleKeyCategories +
                "}";
    }
}
