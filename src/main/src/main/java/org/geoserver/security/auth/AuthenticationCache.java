/* (c) 2014 Open Source Geospatial Foundation - all rights reserved
 * (c) 2001 - 2013 OpenPlans
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */

package org.geoserver.security.auth;

import org.geoserver.security.filter.AuthenticationCachingFilter;
import org.springframework.security.core.Authentication;

/**
 * Interface to cache {@link Authentication} objects. 缓存{@link Authentication}对象的接口。
 *
 * <p>The key is created from the name of the filter and the result of {@link
 * AuthenticationCachingFilter#getCacheKey(javax.servlet.http.HttpServletRequest)}
 *
 * <p>密钥是根据筛选器的名称和 {@link
 * AuthenticationCachingFilter#getCacheKey(javax.servlet.http.HttpServletRequest)}
 *
 * @author mcr
 */
public interface AuthenticationCache {

    int DEFAULT_IDLE_TIME = 300;
    int DEFAULT_LIVE_TIME = 600;

    /** Clears all cache entries 清除所有缓存项 */
    public void removeAll();
    /**
     * Clears all cache entries for filterName 清除filterName的所有缓存项
     *
     * @param filterName
     */
    public void removeAll(String filterName);

    /**
     * Clears a specific chache entry
     *
     * @param filterName
     * @param cacheKey
     */
    public void remove(String filterName, String cacheKey);

    /**
     * @param filterName
     * @param cacheKey
     */
    public Authentication get(String filterName, String cacheKey);

    /**
     * @param filterName
     * @param cacheKey
     * @param auth
     * @param timeToIdleSeconds (time to evict after last access)
     * @param timeToLiveSeconds (time to evict after creation time)
     */
    public void put(
            String filterName,
            String cacheKey,
            Authentication auth,
            Integer timeToIdleSeconds,
            Integer timeToLiveSeconds);

    /**
     * timeToIdleSeconds and timeToLiveSeconds are derived from the cache global settings
     * timeToIdleSeconds和timeToLiveSeconds是从缓存全局设置派生的
     *
     * @param filterName
     * @param cacheKey
     * @param auth
     */
    public void put(String filterName, String cacheKey, Authentication auth);
}
