package org.geoserver.rest.edit.util;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.geoserver.catalog.LayerInfo;
import org.geoserver.catalog.ResourceInfo;
import org.geoserver.catalog.StoreInfo;
import org.geoserver.rest.edit.config.DataSourceConfig;
import org.geoserver.rest.edit.constant.Constant;
import org.geoserver.rest.edit.dto.ConnectionParameterDTO;
import org.springframework.util.StringUtils;

/**
 * 数据连接信息工具类
 *
 * @author ily
 * @date 04 26, 2020
 * @since 1.0.0
 */
@UtilityClass
public class ConnectionParameterUtil {

    /**
     * 获取 数据库连接信息
     *
     * @param dataSourceConfig DataSource
     * @return 连接信息
     */
    public List<ConnectionParameterDTO> postGreSqlConnectionParameter(
            DataSourceConfig dataSourceConfig) {

        String jdbcUrl = dataSourceConfig.getJdbcUrl();
        if (StringUtils.hasText(jdbcUrl)) {
            String dbType = getDbType(jdbcUrl);
            if (StringUtils.hasText(dbType)) {
                jdbcUrl = jdbcUrl.replace("//", "/");
                String[] parameter = jdbcUrl.split("/");
                if (parameter.length == 1) {
                    String dataBase = parameter[0];
                    dataBase = dataBase.split(":")[2];
                    return connectionParameter(
                            null,
                            null,
                            dbType,
                            dataBase,
                            dataSourceConfig.getUsername(),
                            dataSourceConfig.getPassword());
                } else if (parameter.length == 3) {
                    String dataBase = parameter[2];
                    String hostPort = parameter[1];
                    String[] hostPorts = hostPort.split(":");
                    if (hostPorts.length == 2) {
                        return connectionParameter(
                                hostPorts[0],
                                hostPorts[1],
                                dbType,
                                dataBase,
                                dataSourceConfig.getUsername(),
                                dataSourceConfig.getPassword());
                    } else {
                        return connectionParameter(
                                hostPorts[0],
                                null,
                                dbType,
                                dataBase,
                                dataSourceConfig.getUsername(),
                                dataSourceConfig.getPassword());
                    }
                }
            }
        }
        return null;
    }

    /**
     * 获取连接属性
     *
     * @param host 服务地址
     * @param port 端口号
     * @param dbType 数据库类型
     * @param dataBase 数据库
     * @param username 用户名
     * @param password 密码
     * @return 连接信息
     */
    public List<ConnectionParameterDTO> connectionParameter(
            String host,
            String port,
            String dbType,
            String dataBase,
            String username,
            String password) {
        port = StringUtils.hasText(port) ? port : Constant.POSTGRESQL_DEFAULT_PROT;
        if (StringUtils.hasText(host)
                && !Constant.POSTGRESQL_DEFAULT_HOST.equals(host)
                && !Constant.DEFAULT_IP.equals(host)) {
            return Collections.singletonList(
                    new ConnectionParameterDTO(host, port, dbType, dataBase, username, password));
        } else {
            return Arrays.asList(
                    new ConnectionParameterDTO(
                            Constant.POSTGRESQL_DEFAULT_HOST,
                            Constant.POSTGRESQL_DEFAULT_PROT,
                            dbType,
                            dataBase,
                            username,
                            password),
                    new ConnectionParameterDTO(
                            Constant.DEFAULT_IP,
                            Constant.POSTGRESQL_DEFAULT_PROT,
                            dbType,
                            dataBase,
                            username,
                            password));
        }
    }

    public String getDbType(String jdbcUrl) {
        if (StringUtils.hasText(jdbcUrl)) {
            if (jdbcUrl.toLowerCase().contains("postgresql")
                    || jdbcUrl.toLowerCase().contains("postgis")) {
                return "postgresql";
            }
        }
        return null;
    }

    /**
     * 转字符串
     *
     * @param val value
     * @return String
     */
    public String vlaToString(Serializable val) {
        return val != null ? String.valueOf(val) : null;
    }

    public LayerInfo layerInfo(
            LayerInfo layerInfo, DataSourceConfig dataSourceConfig, @NonNull String tableName) {
        ResourceInfo resource = layerInfo.getResource();
        if (resource != null) {
            StoreInfo store = resource.getStore();
            if (store != null) {
                ConnectionParameterDTO connectionParameterDTO =
                        creatConnectionParameter(store.getConnectionParameters());
                if (connectionParameterDTO != null) {
                    List<ConnectionParameterDTO> connectionParameterDTOS =
                            postGreSqlConnectionParameter(dataSourceConfig);
                    if (connectionParameterDTOS != null && !connectionParameterDTOS.isEmpty()) {
                        boolean result =
                                connectionParameterDTOS
                                        .stream()
                                        .anyMatch(
                                                connectionParameter ->
                                                        connectionParameter.equals(
                                                                connectionParameterDTO));
                        if (result) {
                            // 图层对应表名
                            String nativeName = resource.getNativeName();
                            if (tableName.equals(nativeName)) {
                                return layerInfo;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * 创建连接信息
     *
     * @param connectionParameters GeoServer连接信息
     * @return ConnectionParameterDTO
     */
    public ConnectionParameterDTO creatConnectionParameter(
            Map<String, Serializable> connectionParameters) {
        if (connectionParameters != null) {
            ConnectionParameterDTO connectionParameterDTO = new ConnectionParameterDTO();
            connectionParameterDTO.setHost(
                    vlaToString(
                            connectionParameters.get(Constant.STORE_CONNECTION_PARAMETER_HOST)));
            connectionParameterDTO.setPort(
                    vlaToString(
                            connectionParameters.get(Constant.STORE_CONNECTION_PARAMETER_PROT)));
            connectionParameterDTO.setDbType(
                    getDbType(
                            vlaToString(
                                    connectionParameters.get(
                                            Constant.STORE_CONNECTION_PARAMETER_DB_TYPE))));
            connectionParameterDTO.setDataBase(
                    vlaToString(
                            connectionParameters.get(
                                    Constant.STORE_CONNECTION_PARAMETER_DATA_BASE)));
            connectionParameterDTO.setUsername(
                    vlaToString(
                            connectionParameters.get(Constant.STORE_CONNECTION_PARAMETER_USER)));
            connectionParameterDTO.setPassword(
                    vlaToString(
                            connectionParameters.get(Constant.STORE_CONNECTION_PARAMETER_PASSWD)));
            return connectionParameterDTO;
        }
        return null;
    }

    /**
     * 获取表明
     *
     * @param DO 数据表对象
     * @return 表名
     */
    public String getTableName(Object DO) {
        if (DO != null) {
            Annotation[] annotations = DO.getClass().getAnnotations();
            for (Annotation annotation : annotations) {
                if (annotation instanceof TableName) {
                    TableName tableName = (TableName) annotation;
                    return tableName.value();
                }
            }
        }

        return null;
    }
}
