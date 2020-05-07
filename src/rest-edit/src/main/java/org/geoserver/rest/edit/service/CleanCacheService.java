package org.geoserver.rest.edit.service;

/**
 * 清空缓存服务
 *
 * @author ily
 * @date 04 26, 2020
 * @since 1.0.0
 */
public interface CleanCacheService {

    void cleanCache(String tableName);
}
