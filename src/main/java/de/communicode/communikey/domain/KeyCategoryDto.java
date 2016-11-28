/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2016
 */
package de.communicode.communikey.domain;

/**
 * A data transfer object for a {@link KeyCategory} entity.
 *
 * @author sgreb@communicode.de
 * @since 0.2.0
 */
public class KeyCategoryDto {
    private long id;

    private long[] childs;

    private long creatorId;

    private String name;

    private long parentId;

    private long responsibleId;

    public long getId() {
        return id;
    }

    public long[] getChilds() {
        return childs;
    }

    public long getCreatorId() {
        return creatorId;
    }

    public String getName() {
        return name;
    }

    public long getParentId() {
        return parentId;
    }

    public long getResponsibleId() {
        return responsibleId;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setChilds(long[] childs) {
        this.childs = childs;
    }

    public void setCreatorId(long creatorId) {
        this.creatorId = creatorId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setParentId(long parentId) {
        this.parentId = parentId;
    }

    public void setResponsibleId(long responsibleId) {
        this.responsibleId = responsibleId;
    }
}
