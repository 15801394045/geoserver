package org.geoserver.rest.edit.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * @author ily
 * @date 04 26, 2020
 * @since 1.0.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Constant {

    /** geoserver 连接信息 ip */
    public static final String STORE_CONNECTION_PARAMETER_HOST = "host";
    /** geoserver 连接信息 端口号 */
    public static final String STORE_CONNECTION_PARAMETER_PROT = "port";
    /** geoserver 连接信息 数据库 */
    public static final String STORE_CONNECTION_PARAMETER_DATA_BASE = "database";
    /** geoserver 连接信息 数据库类型 */
    public static final String STORE_CONNECTION_PARAMETER_DB_TYPE = "dbtype";
    /** 用户名 */
    public static final String STORE_CONNECTION_PARAMETER_USER = "user";
    /** 密码 */
    public static final String STORE_CONNECTION_PARAMETER_PASSWD = "passwd";

    /** postgresql 默认服务地址 */
    public static final String POSTGRESQL_DEFAULT_HOST = "localhost";
    /** postgresql 默认端口号 */
    public static final String POSTGRESQL_DEFAULT_PROT = "5432";

    /** 默认ip地址 */
    public static final String DEFAULT_IP = "127.0.0.1";
}
