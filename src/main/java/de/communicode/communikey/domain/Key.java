/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2016
 */
package de.communicode.communikey.domain;

import static de.communicode.communikey.config.CommunikeyConstants.TABLE_KEYS;
import static de.communicode.communikey.config.CommunikeyConstants.TABLE_KEYS_COLUMN_KEY_ID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GenerationType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import java.sql.Timestamp;
import java.util.Calendar;

/**
 * Represents a key entity.
 *
 * @author sgreb@communicode.de
 * @since 0.1.0
 */
@Entity
@Table(name = TABLE_KEYS)
public class Key {
    @Id
    @Column(name = TABLE_KEYS_COLUMN_KEY_ID)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne
    @JoinColumn(name = "creator_user_id")
    private User creator;

    private Timestamp creationTimestamp;
    private String value;

    private Key() {}

    public Key(String value) {
        this.value = value;
        long time = Calendar.getInstance().getTimeInMillis();
        this.creationTimestamp = new Timestamp(time);
    }

    public long getId() {
        return id;
    }

    public Timestamp getCreationTimestamp() {
        return creationTimestamp;
    }

    /**
     * @since 0.2.0
     * @return the creator of this key
     */
    public User getCreator() {
        return creator;
    }

    public long getId() {
        return id;
    }

    public String getValue() {
        return value;
    }

    public void setCreationTimestamp(Timestamp creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }

    /**
     * @since 0.2.0
     * @param creator the creator of this key
     */
    public void setCreator(User creator) {
        this.creator = creator;
    }

    public void setValue(String value) {
        this.value = value;
    }
}