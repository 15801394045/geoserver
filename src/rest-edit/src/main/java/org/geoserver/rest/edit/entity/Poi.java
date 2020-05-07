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
@TableName(autoResultMap = true, value = "poi")
public class Poi implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String name;

    private String adress;

    private String telephone;

    private String category;

    private String pLater;

    private String pCode;

    private JSONObject geom;

    private String poiCode;
}
