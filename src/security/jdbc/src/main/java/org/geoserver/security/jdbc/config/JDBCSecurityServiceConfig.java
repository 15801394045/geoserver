/* (c) 2014 Open Source Geospatial Foundation - all rights reserved
 * (c) 2001 - 2013 OpenPlans
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.security.jdbc.config;

import org.geoserver.security.config.BaseSecurityNamedServiceConfig;
import org.geoserver.security.config.SecurityNamedServiceConfig;

/**
 * Extension of {@link SecurityNamedServiceConfig} in which the underlying config is stored in a
 * database accessible via JDBC.
 *
 * <p>{@link SecurityNamedServiceConfig}的扩展，其中底层配置存储在可通过JDBC访问的数据库中。
 *
 * @author christian
 */
public abstract class JDBCSecurityServiceConfig extends BaseSecurityNamedServiceConfig {

    private static final long serialVersionUID = 1L;

    private String propertyFileNameDDL;
    private String propertyFileNameDML;
    private String jndiName;
    private boolean jndi;
    private String driverClassName;
    private String connectURL;
    private String userName;
    private String password;
    private boolean creatingTables;

    public JDBCSecurityServiceConfig() {}

    public JDBCSecurityServiceConfig(JDBCSecurityServiceConfig other) {
        super(other);
        propertyFileNameDDL = other.getPropertyFileNameDDL();
        propertyFileNameDML = other.getPropertyFileNameDML();
        jndiName = other.getJndiName();
        jndi = other.isJndi();
        driverClassName = other.getClassName();
        connectURL = other.getConnectURL();
        userName = other.getUserName();
        password = other.getPassword();
    }

    /**
     * Flag controlling whether to connect through JNDI or through creation of a direct connection.
     * 控制是通过JNDI连接还是通过创建直接连接的标志。
     *
     * <p>If set {@link #getJndiName()} is used to obtain the connection.
     *
     * <p>如果使用set {@link #getJndiName()}获取连接。
     */
    public boolean isJndi() {
        return jndi;
    }

    /**
     * Set flag controlling whether to connect through JNDI or through creation of a direct
     * connection.
     *
     * <p>设置标志，控制是通过JNDI还是通过创建直接连接进行连接。
     */
    public void setJndi(boolean jndi) {
        this.jndi = jndi;
    }

    /**
     * Name of JNDI resource for database connection. 数据库连接的JNDI资源的名称。
     *
     * <p>Used if {@link #isJndi()} is set.
     *
     * <p>如果设置了{@link #isJndi()}，则使用。
     */
    public String getJndiName() {
        return jndiName;
    }

    /** Sets name of JNDI resource for database connection. 设置数据库连接的JNDI资源的名称。 */
    public void setJndiName(String jndiName) {
        this.jndiName = jndiName;
    }

    /** File name of property file containing DDL statements. 包含DDL语句的属性文件的文件名。 */
    public String getPropertyFileNameDDL() {
        return propertyFileNameDDL;
    }

    /** Sets file name of property file containing DDL statements. 设置包含DDL语句的属性文件的文件名。 */
    public void setPropertyFileNameDDL(String propertyFileNameDDL) {
        this.propertyFileNameDDL = propertyFileNameDDL;
    }

    /** File name of property file containing DML statements. 包含DML语句的属性文件的文件名。 */
    public String getPropertyFileNameDML() {
        return propertyFileNameDML;
    }

    /** Sets file name of property file containing DML statements. 设置包含DML语句的属性文件的文件名。 */
    public void setPropertyFileNameDML(String propertyFileNameDML) {
        this.propertyFileNameDML = propertyFileNameDML;
    }

    /**
     * The JDBC driver class name. JDBC驱动程序类名。
     *
     * <p>Used only if {@link #isJndi()} is false.
     *
     * <p>仅在{@link #isJndi()}为false时使用。
     */
    public String getDriverClassName() {
        return driverClassName;
    }

    /** Sets the JDBC driver class name. 设置JDBC驱动程序类名 */
    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    /**
     * The JDBC url with which to obtain a database connection with. 用于获取数据库连接的JDBC url。
     *
     * <p>Used only if {@link #isJndi()} is false.
     *
     * <p>仅在{@link #isJndi()}为false时使用。
     */
    public String getConnectURL() {
        return connectURL;
    }

    /** The JDBC url with which to obtain a database connection with. 用于获取数据库连接的JDBC url。 */
    public void setConnectURL(String connectURL) {
        this.connectURL = connectURL;
    }

    /**
     * The database user name.
     *
     * <p>Used only if {@link #isJndi()} is false.
     */
    public String getUserName() {
        return userName;
    }

    /** Sets the database user name. */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * /** The database password.
     *
     * <p>Used only if {@link #isJndi()} is false.
     */
    public String getPassword() {
        return password;
    }

    /** Sets the database password. */
    public void setPassword(String password) {
        this.password = password;
    }

    /** Indicates if the tables are created behind the scenes */
    public boolean isCreatingTables() {
        return creatingTables;
    }

    /**
     * set table creation flag
     *
     * @param creatingTables
     */
    public void setCreatingTables(boolean creatingTables) {
        this.creatingTables = creatingTables;
    }

    /** Helper method to determine if the backing database is mysql. 用于确定备份数据库是否为mysql的帮助器方法。 */
    protected boolean isMySQL() {
        return "com.mysql.jdbc.Driver".equals(driverClassName);
    }

    /**
     * Initializes the DDL and DML property files based on the database type. 根据数据库类型初始化DDL和DML属性文件。
     */
    @Override
    public void initBeforeSave() {
        if (propertyFileNameDDL == null) {
            propertyFileNameDDL = isMySQL() ? defaultDDLFilenameMySQL() : defaultDDLFilename();
        }

        if (propertyFileNameDML == null) {
            propertyFileNameDML = isMySQL() ? defaultDMLFilenameMySQL() : defaultDMLFilename();
        }
    }

    /** return the default filename for the DDL file. 返回DDL文件的默认文件名。 */
    protected abstract String defaultDDLFilename();

    /** return the default filename for the DDL file on MySQL. 返回MySQL上DDL文件的默认文件名。 */
    protected abstract String defaultDDLFilenameMySQL();

    /** return the default filename for the DML file. 返回DML文件的默认文件名。 */
    protected abstract String defaultDMLFilename();

    /** return the default filename for the DML file on MySQL. 返回MySQL上DML文件的默认文件名。 */
    protected abstract String defaultDMLFilenameMySQL();
}
