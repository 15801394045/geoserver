package org.geoserver.rest.edit.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import org.geoserver.rest.edit.entity.Poi;
import org.geoserver.rest.edit.mapper.PoiMapper;
import org.geoserver.rest.edit.service.EditService;
import org.springframework.stereotype.Service;

/**
 * @author ily
 * @date 04 24, 2020
 * @since 1.0.0
 */
@Service
@AllArgsConstructor
public class EditServiceImpl implements EditService {
    private PoiMapper mapper;

    @Override
    public Page<Poi> test() {
        Page<Poi> page = new Page<>(1, 2);
        QueryWrapper<Poi> wrapper = new QueryWrapper<>();
        wrapper.select("st_asgeojson(geom) geom");
        return mapper.selectPage(page, wrapper);
    }
}
