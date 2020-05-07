package org.geoserver.rest.edit.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.geoserver.rest.edit.entity.Build;
import org.geoserver.rest.edit.mapper.BuildMapper;
import org.geoserver.rest.edit.service.BuildService;
import org.springframework.stereotype.Service;

/**
 * 服务实现类
 *
 * @author ily
 * @since 2020-04-28
 */
@Service
public class BuildServiceImpl extends ServiceImpl<BuildMapper, Build> implements BuildService {}
