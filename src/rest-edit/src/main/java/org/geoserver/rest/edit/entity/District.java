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
@TableName(autoResultMap = true, value = "district")
public class District implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String name;

    private String layer;

    private String code;

    private JSONObject geom;
}
