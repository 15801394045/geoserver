package org.geoserver.rest.edit.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.geoserver.rest.edit.entity.Line;
import org.geoserver.rest.edit.mapper.LineMapper;
import org.geoserver.rest.edit.service.LineService;
import org.springframework.stereotype.Service;

/**
 * 服务实现类
 *
 * @author ily
 * @since 2020-04-28
 */
@Service
public class LineServiceImpl extends ServiceImpl<LineMapper, Line> implements LineService {}
