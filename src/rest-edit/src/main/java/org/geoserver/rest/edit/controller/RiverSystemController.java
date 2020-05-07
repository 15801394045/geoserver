package org.geoserver.rest.edit.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.geoserver.rest.edit.entity.Polygon;
import org.geoserver.rest.edit.entity.RiverSystem;
import org.geoserver.rest.edit.event.CleanCacheEvent;
import org.geoserver.rest.edit.service.RiverSystemService;
import org.geoserver.rest.edit.util.ConnectionParameterUtil;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.*;

/**
 * 前端控制器
 *
 * @author ily
 * @since 2020-04-28
 */
@Api(tags = "水系")
@RestController
@AllArgsConstructor
@RequestMapping("/river-system")
public class RiverSystemController {
    private RiverSystemService service;
    private ApplicationContext applicationContext;

    @PostMapping
    @ApiOperation(value = "添加", notes = "根据实体对象添加数据")
    public RiverSystem save(@RequestBody RiverSystem entity) {
        applicationContext.publishEvent(
                new CleanCacheEvent(this, ConnectionParameterUtil.getTableName(entity)));
        service.save(entity);
        return entity;
    }
    @PutMapping
    @ApiOperation(value = "修改", notes = "根据实体对象修改数据")
    public RiverSystem updateById(@RequestBody RiverSystem entity) {
        applicationContext.publishEvent(
                new CleanCacheEvent(this, ConnectionParameterUtil.getTableName(entity)));
        service.updateById(entity);
        return entity;
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除", notes = "根据id删除")
    public boolean delete(@PathVariable("id") Integer id) {
        applicationContext.publishEvent(
                new CleanCacheEvent(
                        this, ConnectionParameterUtil.getTableName(service.getById(id))));
        return service.removeById(id);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "根据id查询", notes = "通过id查询")
    public RiverSystem get(@PathVariable("id") Integer id) {
        return service.getById(id);
    }

    @GetMapping(path = {"/list/page", "/list/page/{page}/{size}"})
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "page", value = "页码", dataType = "Long"),
        @ApiImplicitParam(name = "size", value = "每一次显示多少条", dataType = "Long")
    })
    public Page<RiverSystem> listPage(
            @PathVariable(required = false) Long page, @PathVariable(required = false) Long size) {

        Page<RiverSystem> poiPage =
                new Page<>(
                        Optional.ofNullable(page).orElse(0L),
                        Optional.ofNullable(size).orElse(25L));
        return service.page(poiPage);
    }
}
