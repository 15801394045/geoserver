package org.geoserver.rest.edit.event;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.geoserver.rest.edit.service.CleanCacheService;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 清空缓存事件监控
 *
 * @author ily
 * @date 04 27, 2020
 * @since 1.0.0
 */
@Slf4j
@Async
@Component
@AllArgsConstructor
public class CleanCacheEventListener {

    private CleanCacheService cleanCacheService;

    @EventListener(classes = CleanCacheEvent.class)
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void cleanCacheEventListener(CleanCacheEvent event) {
        String tableName = event.getLatestTableName();
        log.info("清除[" + tableName + "]表相关缓存!");
        cleanCacheService.cleanCache(tableName);
    }
}
