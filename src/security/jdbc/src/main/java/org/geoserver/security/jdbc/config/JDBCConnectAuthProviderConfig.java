/* (c) 2014 Open Source Geospatial Foundation - all rights reserved
 * (c) 2001 - 2013 OpenPlans
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.security.jdbc.config;

import org.geoserver.security.config.BaseSecurityNamedServiceConfig;
import org.geoserver.security.config.SecurityAuthProviderConfig;
import org.geoserver.security.config.SecurityNamedServiceConfig;

/**
 * Extension of {@link SecurityNamedServiceConfig} for authentication providers checking
 * username/password with a JDBC connect. {@link
 * SecurityNamedServiceConfig}的扩展，用于身份验证提供程序使用JDBC连接检查用户名/密码。
 *
 * @author christian
 */
public class JDBCConnectAuthProviderConfig extends BaseSecurityNamedServiceConfig
        implements SecurityAuthProviderConfig {

    private static final long serialVersionUID = 1L;

    private String driverClassName;
    private String connectURL;
    private String userGroupServiceName;

    public JDBCConnectAuthProviderConfig() {}

    public JDBCConnectAuthProviderConfig(JDBCConnectAuthProviderConfig other) {
        super(other);
        driverClassName = other.getDriverClassName();
        connectURL = other.getConnectURL();
        userGroupServiceName = other.getUserGroupServiceName();
    }

    /**
     * The JDBC driver class name. JDBC驱动程序类名。
     *
     * <p>Used only if {@link #isJndi()} is false.
     *
     * <p>仅在{@link #isJndi()} 为false时使用。
     */
    public String getDriverClassName() {
        return driverClassName;
    }

    /** Sets the JDBC driver class name. 设置JDBC驱动程序类名。 */
    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    /**
     * The JDBC url with which to obtain a database connection with. 用于获取数据库连接的JDBC url。
     *
     * <p>Used only if {@link #isJndi()} is false.
     *
     * <p>仅在 {@link #isJndi()} 为false时使用。
     */
    public String getConnectURL() {
        return connectURL;
    }

    /** The JDBC url with which to obtain a database connection with. 用于获取数据库连接的JDBC url。 */
    public void setConnectURL(String connectURL) {
        this.connectURL = connectURL;
    }

    @Override
    public String getUserGroupServiceName() {
        return userGroupServiceName;
    }

    @Override
    public void setUserGroupServiceName(String userGroupServiceName) {
        this.userGroupServiceName = userGroupServiceName;
    }
}
