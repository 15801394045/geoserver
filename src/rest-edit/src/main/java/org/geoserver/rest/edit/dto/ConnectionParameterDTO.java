package org.geoserver.rest.edit.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 数据库连接对象
 *
 * @author ily
 * @date 04 26, 2020
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConnectionParameterDTO {
    /** 服务器地址 */
    private String host;
    /** 端口号 */
    private String port;
    /** 数据类型 */
    private String dbType;
    /** 数据库名称 */
    private String dataBase;
    /** 用户名 */
    private String username;
    /** 密码 */
    private String password;
}
