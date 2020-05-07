package org.geoserver.rest.edit.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.LayerGroupInfo;
import org.geoserver.catalog.LayerInfo;
import org.geoserver.catalog.PublishedInfo;
import org.geoserver.gwc.GWC;
import org.geoserver.rest.edit.config.DataSourceConfig;
import org.geoserver.rest.edit.service.CleanCacheService;
import org.geoserver.rest.edit.util.ConnectionParameterUtil;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 清空缓存实现
 *
 * @author ily
 * @date 04 26, 2020
 * @since 1.0.0
 */
@Slf4j
@Service
@AllArgsConstructor
public class CleanCacheServiceImpl implements CleanCacheService {
    @Qualifier("catalog")
    private Catalog catalog;

    private DataSourceConfig config;

    @Override
    public void cleanCache(String tableName) {
        if (StringUtils.hasText(tableName)) {
            GWC facade = GWC.get();
            List<LayerInfo> layers = catalog.getLayers();
            Set<String> layerNames = new HashSet<>();
            if (layers != null && !layers.isEmpty()) {
                for (LayerInfo info : layers) {
                    LayerInfo layerInfo =
                            ConnectionParameterUtil.layerInfo(info, config, tableName);
                    if (layerInfo != null) {
                        String layerName = layerInfo.prefixedName();
                        log.info("清理[" + layerName + "]图层缓存!");
                        facade.truncate(layerName);
                        layerNames.add(layerName);
                    }
                }
            }
            if (!layerNames.isEmpty()) {
                Set<String> layerGroupNames = new HashSet<>();
                for (LayerGroupInfo groupInfo : catalog.getLayerGroups()) {
                    String groupName = layerGroupName(groupInfo, layerNames, layerGroupNames);
                    if (StringUtils.hasText(groupName)) {
                        log.info("清理[" + groupName + "]图层组缓存!");
                        facade.truncate(groupName);
                    }
                }
            }
        }
    }

    public String layerGroupName(
            LayerGroupInfo groupInfo, Set<String> layerNames, Set<String> layerGroupNames) {
        List<PublishedInfo> layers = groupInfo.getLayers();
        for (PublishedInfo info : layers) {
            if (info instanceof LayerInfo) {
                boolean result = layerNames.stream().anyMatch(l -> l.equals(info.prefixedName()));
                if (result) {
                    layerGroupNames.add(groupInfo.prefixedName());
                    return groupInfo.prefixedName();
                }
            } else if (info instanceof LayerGroupInfo) {
                boolean result =
                        layerGroupNames.stream().anyMatch(l -> l.equals(info.prefixedName()));
                if (result) {
                    return groupInfo.prefixedName();
                }
                String layerGroupName =
                        layerGroupName((LayerGroupInfo) info, layerNames, layerGroupNames);
                if (StringUtils.hasText(layerGroupName)) {
                    layerGroupNames.add(groupInfo.prefixedName());
                    return groupInfo.prefixedName();
                }
            }
        }
        return null;
    }
}
