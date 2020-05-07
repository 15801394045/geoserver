package org.geoserver.rest.edit.event;

import org.springframework.context.ApplicationEvent;

/**
 * 清空缓存事件
 *
 * @author ily
 * @date 04 27, 2020
 * @since 1.0.0
 */
public class CleanCacheEvent extends ApplicationEvent {

    /** 表名 */
    private String tableName;

    public CleanCacheEvent(Object source, String tableName) {
        super(source);
        this.tableName = tableName;
    }

    public final String getLatestTableName() {
        return this.tableName;
    }
}
