/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2016
 */
package de.communicode.communikey.domain;

import java.sql.Timestamp;

/**
 * A data transfer object for a {@link Key} entity.
 *
 * @author sgreb@communicode.de
 * @since 0.2.0
 */
public class KeyDto {
    private long id;

    private long categoryId;

    private long creatorId;

    private Timestamp creationTimestamp;

    private String value;

    public long getCategoryId() {
        return categoryId;
    }

    public Timestamp getCreationTimestamp() {
        return creationTimestamp;
    }

    public long getCreatorId() {
        return creatorId;
    }

    public long getId() {
        return id;
    }

    public String getValue() {
        return value;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    public void setCreationTimestamp(Timestamp creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }

    public void setCreatorId(long creatorId) {
        this.creatorId = creatorId;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
