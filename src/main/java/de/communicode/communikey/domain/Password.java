/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2016
 */
package de.communicode.communikey.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GenerationType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import java.sql.Timestamp;
import java.util.Calendar;

/**
 * Represents a password entity.
 *
 * @author sgreb@communicode.de
 * @since 0.1.0
 */
@Entity
@Table(name = "credential_passwords")
public class Password {
    @Id
    @Column(name = "CREDENTIAL_PASSWORD_ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private Timestamp creationTimestamp;
    private String value;

    private Password() {}

    public Password(String value) {
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

    public String getValue() {
        return value;
    }

    public void setCreationTimestamp(Timestamp creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }

    public void setValue(String value) {
        this.value = value;
    }
}