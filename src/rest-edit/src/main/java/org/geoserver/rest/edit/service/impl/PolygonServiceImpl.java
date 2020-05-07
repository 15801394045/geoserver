package org.geoserver.rest.edit.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.geoserver.rest.edit.entity.Polygon;
import org.geoserver.rest.edit.mapper.PolygonMapper;
import org.geoserver.rest.edit.service.PolygonService;
import org.springframework.stereotype.Service;

/**
 * 服务实现类
 *
 * @author ily
 * @since 2020-04-28
 */
@Service
public class PolygonServiceImpl extends ServiceImpl<PolygonMapper, Polygon>
        implements PolygonService {}
