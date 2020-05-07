package org.geoserver.rest.edit.entity;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * @author ily
 * @since 2020-04-28
 */
@Data
@TableName(autoResultMap = true, value = "build")
public class Build implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键 */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /** 名称 */
    private String name;

    /** 楼层数 */
    private Integer floor;

    /** 楼层高度 */
    private Double floorHeight;

    /** 高度 */
    private Double height;

    /** 来源 1高度 2其他 */
    private Integer source;

    /** 地理信息 */
    private JSONObject geom;
}
