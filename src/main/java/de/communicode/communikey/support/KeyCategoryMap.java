/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2017
 */
package de.communicode.communikey.support;

import com.google.common.collect.MapMaker;
import com.google.common.collect.Sets;
import de.communicode.communikey.domain.KeyCategory;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;

/**
 * A reference map wrapper class for {@link KeyCategory} entities to track direct- and indirect children of each key category.
 * <p>
 *     Each map key is a ID of a key category entity mapped to a unordered value list of unique key category entity IDs.
 *     Every value can either be a direct- or indirect key category children.
 *     The map object can be accessed with the {@link #getMap()}  method.
 * </p>
 * <p>
 *     This class is a Singleton.
 *     The instance can be obtained via {@link #getInstance()}.
 *     The map object is baked by {@link MapMaker#makeMap()} to be thread-safe.
 * </p>
 *
 * @author sgreb@communicode.de
 * @author fsanchez@communicode.de
 * @since 0.2.0
 */
public class KeyCategoryMap {
    private static KeyCategoryMap singletonInstance = new KeyCategoryMap();
    private Map<Long, Set<Long>> map = new MapMaker().makeMap();

    public static KeyCategoryMap getInstance() {
        return singletonInstance;
    }

    private KeyCategoryMap() {}

    /**
     * Initializes the wrapped map with the specified collection of key category entity IDs.
     *
     * @param keyCategories the collection of key category entities to be added to the map
     */
    public void initialize(Set<KeyCategory> keyCategories) {
        keyCategories.forEach(this::addChildren);
    }

    /**
     * Gets the wrapped map object.
     *
     * @return the wrapped map object
     */
    public Map<Long, Set<Long>> getMap() {
        return map;
    }

    /**
     * Adds the specified parent key category entity ID mapped to its direct- and indirect key category children entity IDs.
     *
     * @param parent the key category entity to add all direct- and indirect children of
     */
    private void addChildren(KeyCategory parent) {
        map.put(parent.getId(), addChildrenRecursively(parent));
    }

    /**
     * Recursively adds all direct- and indirect key category children entity IDs of the specified parent key category entity.
     *
     * @param parent the key category entity to add all direct- and indirect children of
     */
    private Set<Long> addChildrenRecursively(KeyCategory parent) {
        if(parent.getChilds().isEmpty()) {
            return Sets.newConcurrentHashSet();
        } else {
            Set<Long> leafChildren = Sets.newConcurrentHashSet();
            for (KeyCategory child : parent.getChilds()) {
                leafChildren.add(child.getId());
                leafChildren.addAll(addChildrenRecursively(child));
            }
            return leafChildren;
        }
    }

    @Override
    public String toString() {
        return Arrays.toString(map.entrySet().toArray());
    }
}
