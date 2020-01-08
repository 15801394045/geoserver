/* (c) 2015 Open Source Geospatial Foundation - all rights reserved
 * (c) 2015 OpenPlans
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.util;

/**
 * Simple generic Filter
 * 简单泛型筛选器
 *
 * @param <T>
 * @author Niels Charlier
 */
@FunctionalInterface
public interface Filter<T> {

    boolean accept(T obj);
}
