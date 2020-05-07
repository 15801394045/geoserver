package org.geoserver.rest.edit.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.geoserver.rest.edit.entity.District;
import org.geoserver.rest.edit.mapper.DistrictMapper;
import org.geoserver.rest.edit.service.DistrictService;
import org.springframework.stereotype.Service;

/**
 * 服务实现类
 *
 * @author ily
 * @since 2020-04-28
 */
@Service
public class DistrictServiceImpl extends ServiceImpl<DistrictMapper, District>
        implements DistrictService {}
