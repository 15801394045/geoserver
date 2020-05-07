package org.geoserver.rest.edit.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.geoserver.rest.edit.entity.RiverSystem;
import org.geoserver.rest.edit.mapper.RiverSystemMapper;
import org.geoserver.rest.edit.service.RiverSystemService;
import org.springframework.stereotype.Service;

/**
 * 服务实现类
 *
 * @author ily
 * @since 2020-04-28
 */
@Service
public class RiverSystemServiceImpl extends ServiceImpl<RiverSystemMapper, RiverSystem>
        implements RiverSystemService {}
