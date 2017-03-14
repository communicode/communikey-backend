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
 *     Each map key is a ID of a key category mapped to a unordered value list of unique key category IDs.
 *     Every value can either be a direct- or indirect key category children.
 *     The map object can be accessed with the {@link #getMap()} method.
 * <p>
 *     This class is a Singleton.
 *     The instance can be obtained via {@link #getInstance()}.
 *     The map object is baked by {@link MapMaker#makeMap()} to be thread-safe.
 *
 * @author sgreb@communicode.de
 * @author fsanchez@communicode.de
 * @since 0.2.0
 */
public class KeyCategoryChildrenMap {
    private static KeyCategoryChildrenMap singletonInstance = new KeyCategoryChildrenMap();
    private Map<Long, Set<Long>> map = new MapMaker().makeMap();

    public static KeyCategoryChildrenMap getInstance() {
        return singletonInstance;
    }

    private KeyCategoryChildrenMap() {}

    /**
     * Initializes the wrapped map with the specified collection of key category IDs.
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
     * Adds the specified parent key category ID mapped to its direct- and indirect key category children IDs.
     *
     * @param parent the key category to add all direct- and indirect children of
     */
    private void addChildren(KeyCategory parent) {
        map.put(parent.getId(), addChildrenRecursively(parent));
    }

    /**
     * Recursively adds all direct- and indirect key category children IDs of the specified parent key category.
     *
     * @param parent the key category to add all direct- and indirect children of
     */
    private Set<Long> addChildrenRecursively(KeyCategory parent) {
        if (parent.getChildren().isEmpty()) {
            return Sets.newConcurrentHashSet();
        } else {
            Set<Long> leafChildren = Sets.newConcurrentHashSet();
            for (KeyCategory child : parent.getChildren()) {
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
