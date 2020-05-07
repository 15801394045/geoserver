package org.geoserver.rest.edit.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author ily
 * @date 04 26, 2020
 * @since 1.0.0
 */
@Data
@Configuration
public class DataSourceConfig {

    @Value("${datasource.driver-class-name}")
    private String driverClassName;

    @Value("${datasource.jdbc-url}")
    private String jdbcUrl;

    @Value("${datasource.username}")
    private String username;

    @Value("${datasource.password}")
    private String password;
}
