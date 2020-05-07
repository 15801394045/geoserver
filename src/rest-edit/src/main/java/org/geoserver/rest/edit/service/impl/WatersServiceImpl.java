package org.geoserver.rest.edit.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.geoserver.rest.edit.entity.Waters;
import org.geoserver.rest.edit.mapper.WatersMapper;
import org.geoserver.rest.edit.service.WatersService;
import org.springframework.stereotype.Service;

/**
 * 服务实现类
 *
 * @author ily
 * @since 2020-04-28
 */
@Service
public class WatersServiceImpl extends ServiceImpl<WatersMapper, Waters> implements WatersService {}
