package org.geoserver.rest.edit.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.geoserver.rest.edit.entity.Poi;

/**
 * @author ily
 * @date 04 24, 2020
 * @since 1.0.0
 */
public interface EditService {
    Page<Poi> test();
}
