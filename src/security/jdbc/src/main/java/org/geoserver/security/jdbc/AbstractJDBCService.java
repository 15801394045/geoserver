/* (c) 2014 Open Source Geospatial Foundation - all rights reserved
 * (c) 2001 - 2013 OpenPlans
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.security.jdbc;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.io.FileUtils;
import org.geoserver.platform.resource.Files;
import org.geoserver.platform.resource.Resource;
import org.geoserver.platform.resource.Resources;
import org.geoserver.security.config.SecurityNamedServiceConfig;
import org.geoserver.security.impl.AbstractGeoServerSecurityService;
import org.geoserver.security.jdbc.config.JDBCSecurityServiceConfig;
import org.geoserver.util.IOUtils;

/**
 * JDBC base implementation for common used methods 常用方法的JDBC基实现
 *
 * @author christian
 */
public abstract class AbstractJDBCService extends AbstractGeoServerSecurityService {
    /** logger */
    static Logger LOGGER =
            org.geotools.util.logging.Logging.getLogger("org.geoserver.security.jdbc");

    protected Properties ddlProps, dmlProps;
    protected DataSource datasource;

    /** Default isolation level to use 要使用的默认隔离级别 */
    static final int DEFAULT_ISOLATION_LEVEL = Connection.TRANSACTION_READ_COMMITTED;

    protected AbstractJDBCService() {}

    /**
     * initialize a {@link DataSource} form a {@link JdbcSecurityServiceConfig} object 从 {@link
     * JdbcSecurityServiceConfig} 对象初始化{@link DataSource}
     *
     * @param config
     * @throws IOException
     */
    public void initializeDSFromConfig(SecurityNamedServiceConfig namedConfig) throws IOException {
        JDBCSecurityServiceConfig config = (JDBCSecurityServiceConfig) namedConfig;
        if (config.isJndi()) {
            String jndiName = config.getJndiName();
            try {
                Context initialContext = new InitialContext();
                datasource = (DataSource) initialContext.lookup(jndiName);
            } catch (NamingException e) {
                throw new IOException(e);
            }
        } else {
            BasicDataSource bds = new BasicDataSource();
            bds.setDriverClassName(config.getDriverClassName());
            bds.setUrl(config.getConnectURL());
            bds.setUsername(config.getUserName());
            bds.setPassword(config.getPassword());
            bds.setDefaultAutoCommit(false);
            bds.setDefaultTransactionIsolation(DEFAULT_ISOLATION_LEVEL);
            bds.setMaxActive(10);
            datasource = bds;
        }
    }

    /** simple getter */
    protected DataSource getDataSource() {
        return datasource;
    }

    /**
     * Get a new connection from the datasource, check/set autocommit == false and isolation level
     * according to {@link #DEFAULT_ISOLATION_LEVEL} 从数据源获取一个新连接，根据 {@link
     * #DEFAULT_ISOLATION_LEVEL}检查/设置autocommit==false和隔离级别
     *
     * @throws SQLException
     */
    protected Connection getConnection() throws SQLException {
        Connection con = getDataSource().getConnection();
        if (con.getAutoCommit()) {
            con.setAutoCommit(false);
        }
        if (con.getTransactionIsolation() != DEFAULT_ISOLATION_LEVEL) {
            con.setTransactionIsolation(DEFAULT_ISOLATION_LEVEL);
        }

        return con;
    }

    /**
     * close a sql connection 关闭sql连接
     *
     * @param con
     * @throws SQLException
     */
    protected void closeConnection(Connection con) throws SQLException {
        con.close();
    }

    /**
     * helper method, if any of the parametres is not null, try to close it and throw away a
     * possible {@link SQLException} helper方法，如果任何参数不为空，则尝试关闭它并丢弃可能的{@link SQLException}
     *
     * @param con
     * @param ps
     * @param rs
     */
    protected void closeFinally(Connection con, PreparedStatement ps, ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException ex) {
        }
        try {
            if (ps != null) {
                ps.close();
            }
        } catch (SQLException ex) {
        }
        try {
            if (con != null) {
                closeConnection(con);
            }
        } catch (SQLException ex) {
        }
    }

    /**
     * get a prepared DML statement for a property key 为属性键获取准备好的DML语句
     *
     * @param key
     * @param con
     * @throws IOException
     * @throws SQLException
     */
    protected PreparedStatement getDMLStatement(String key, Connection con)
            throws IOException, SQLException {
        return getJDBCStatement(key, dmlProps, con);
    }

    /**
     * get a prepared Jdbc Statement by looking into the props for the given key
     * 通过查看给定键的道具来获取准备好的Jdbc语句
     *
     * @param key
     * @param props
     * @param con
     * @throws IOException if key does not exist 如果key不存在
     * @throws SQLException
     */
    protected PreparedStatement getJDBCStatement(String key, Properties props, Connection con)
            throws IOException, SQLException {
        String statementString = props.getProperty(key);
        if (statementString == null || statementString.trim().length() == 0) {
            throw new IOException("No sql statement for key : " + key);
        }
        return con.prepareStatement(statementString.trim());
    }

    /**
     * get a prepared DDL statement for a property key 为属性键获取准备好的DDL语句
     *
     * @param key
     * @param con
     * @throws IOException
     * @throws SQLException
     */
    protected PreparedStatement getDDLStatement(String key, Connection con)
            throws IOException, SQLException {
        return getJDBCStatement(key, ddlProps, con);
    }

    /**
     * create a boolean from a String 从字符串创建布尔值
     *
     * <p>"Y" or "y" results in true, all other values result in false
     *
     * <p>“Y”或“Y”将导致true，所有其他值将导致false
     *
     * @param booleanString
     */
    protected boolean convertFromString(String booleanString) {
        if (booleanString == null) {
            return false;
        }
        return "y".equalsIgnoreCase(booleanString);
    }

    /**
     * convert boolean to string true --> "Y" false --> "N" 将布尔值转换为字符串true-->“Y”false-->“N”
     *
     * @param b
     */
    protected String convertToString(boolean b) {
        return b ? "Y" : "N";
    }

    /** Get ordered property keys for creating tables/indexes 获取用于创建表/索引的有序属性键 */
    protected abstract String[] getOrderedNamesForCreate();

    /** Get ordered property keys for dropping tables/indexes 获取用于删除表/索引的有序属性键 */
    protected abstract String[] getOrderedNamesForDrop();

    public void createTablesIfRequired(JDBCSecurityServiceConfig config) throws IOException {

        if (!this.canCreateStore()) {
            return;
        }
        if (!config.isCreatingTables()) {
            return;
        }
        if (tablesAlreadyCreated()) {
            return;
        }

        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = datasource.getConnection();
            if (con.getAutoCommit()) {
                con.setAutoCommit(false);
            }
            con = getConnection();
            for (String stmt : getOrderedNamesForCreate()) {
                ps = getDDLStatement(stmt, con);
                ps.execute();
                ps.close();
            }
            con.commit();
        } catch (SQLException ex) {
            throw new IOException(ex);
        } finally {
            closeFinally(con, ps, null);
        }
    }

    /**
     * create tables and indexes, statement order defined by {@link #getOrderedNamesForCreate()}
     * 创建表和索引，语句顺序由 {@link #getOrderedNamesForCreate()}定义
     *
     * @throws IOException
     */
    public void createTables() throws IOException {
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = getConnection();
            for (String stmt : getOrderedNamesForCreate()) {
                ps = getDDLStatement(stmt, con);
                ps.execute();
                ps.close();
            }
        } catch (SQLException ex) {
            throw new IOException(ex);
        } finally {
            closeFinally(con, ps, null);
        }
    }

    /**
     * drops tables, statement oder defined by {@link #getOrderedNamesForDrop()} 删除由{@link
     * #getOrderedNamesForDrop()}定义的表、语句oder
     *
     * @throws IOException
     */
    public void dropTables() throws IOException {
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = getConnection();
            for (String stmt : getOrderedNamesForDrop()) {
                ps = getDDLStatement(stmt, con);
                ps.execute();
                ps.close();
            }
        } catch (SQLException ex) {
            throw new IOException(ex);
        } finally {
            closeFinally(con, ps, null);
        }
    }

    /**
     * drops tables, ignore SQLExceptions
     *
     * @throws IOException
     */
    //    public void dropExistingTables() throws IOException {
    //        Connection con = null;
    //        PreparedStatement ps = null;
    //        try {
    //            con = getConnection();
    //            for (String stmt : getOrderedNamesForDrop()) {
    //                try {
    //                    ps= getDDLStatement(stmt, con);
    //                    ps.execute();
    //                    ps.close();
    //                } catch (SQLException ex) {
    //                    // ignore
    //                }
    //            }
    //        } catch (SQLException ex) {
    //            throw new IOException(ex);
    //        } finally {
    //            closeFinally(con, ps, null);
    //        }
    //    }

    /**
     * Check DML statements using {@link #checkSQLStatements(Properties)} 使用{@link
     * #checkSQLStatements(Properties)}检查DML语句
     *
     * @throws IOException
     */
    public Map<String, SQLException> checkDMLStatements() throws IOException {
        return checkSQLStatements(dmlProps);
    }

    /**
     * Check DDL statements using {@link #checkSQLStatements(Properties)} 使用 {@link
     * #checkSQLStatements(Properties)}检查DDL语句
     *
     * @throws IOException
     */
    public Map<String, SQLException> checkDDLStatements() throws IOException {
        return checkSQLStatements(ddlProps);
    }

    /**
     * Checks if the tables are already created
     *
     * @param con
     * @throws IOException
     */
    public boolean tablesAlreadyCreated() throws IOException {
        ResultSet rs = null;
        Connection con = null;
        try {
            con = getConnection();
            DatabaseMetaData md = con.getMetaData();
            String schemaName = null;
            String tableName = ddlProps.getProperty("check.table");
            if (tableName.contains(".")) {
                StringTokenizer tok = new StringTokenizer(tableName, ".");
                schemaName = tok.nextToken();
                tableName = tok.nextToken();
            }
            // try exact match
            rs = md.getTables(null, schemaName, tableName, null);
            if (rs.next()) {
                return true;
            }

            // try with upper case letters
            rs.close();
            schemaName = schemaName == null ? null : schemaName.toUpperCase();
            tableName = tableName.toUpperCase();
            rs = md.getTables(null, schemaName, tableName, null);
            if (rs.next()) {
                return true;
            }

            // try with lower case letters
            rs.close();
            schemaName = schemaName == null ? null : schemaName.toLowerCase();
            tableName = tableName.toLowerCase();
            rs = md.getTables(null, schemaName, tableName, null);
            return rs.next();

        } catch (SQLException ex) {
            throw new IOException(ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (con != null) {
                    closeConnection(con);
                }
            } catch (SQLException e) {
                // do nothing
            }
        }
    }

    /**
     * Checks if the sql statements contained in props can be prepared against the db
     * 检查是否可以针对数据库准备props中包含的sql语句
     *
     * @param props
     * @return return error protocol containing key,statement and {@link SQLException}. The key is
     *     created as follows: 返回包含键、语句和{@link SQLException}的错误协议。密钥创建如下：
     *     <p>property key + "|" + statement string
     *     <p>属性键+“|”+语句字符串
     * @throws IOException
     */
    protected Map<String, SQLException> checkSQLStatements(Properties props) throws IOException {

        Map<String, SQLException> reportMap = new HashMap<String, SQLException>();
        Connection con = null;
        try {
            con = getConnection();
            for (Object key : props.keySet()) {
                String stmt = props.getProperty(key.toString()).trim();
                try {
                    con.prepareStatement(stmt.trim());
                } catch (SQLException ex) {
                    reportMap.put(key.toString() + "|" + stmt, ex);
                }
            }
        } catch (SQLException ex) {
            throw new IOException(ex);
        } finally {
            closeFinally(con, null, null);
        }
        return reportMap;
    }

    @Override
    public String toString() {
        return this.getClass() + " : " + getName();
    }

    /**
     * Check for the existence of the file, if the file exists do nothing. 检查文件的存在，如果文件存在，什么也不做。
     *
     * <p>If the file does not exist, check for a template file contained in the jar with the same
     * name, if found, use it.
     *
     * <p>如果文件不存在，检查包含在jar中的模板文件，名称相同，如果找到，使用它。
     *
     * <p>If no template was found, use the default template
     *
     * <p>如果找不到模板，请使用默认模板
     *
     * @param fileName target location 目标位置
     * @param namedRoot parent dir if fileName is relative 如果文件名是相对的，则为父目录
     * @param defaultResource the standard template 标准模板
     * @throws IOException
     * @return the file to use 要使用的文件
     */
    protected Resource checkORCreateJDBCPropertyFile(
            String fileName, Resource namedRoot, String defaultResource) throws IOException {

        Resource resource;
        fileName = fileName != null ? fileName : defaultResource;
        File file = new File(fileName);
        if (file.isAbsolute()) {
            resource = Files.asResource(file);
        } else {
            resource = namedRoot.get(fileName);
        }

        if (Resources.exists(resource)) {
            // we are happy
            return resource;
        }

        // try to find a template with the same name
        // 尝试查找同名模板
        try (InputStream is = this.getClass().getResourceAsStream(fileName)) {
            if (is != null) {
                IOUtils.copy(is, resource.out());
            } else // use the default template 使用默认模板
            {
                FileUtils.copyURLToFile(getClass().getResource(defaultResource), file);
            }
        }

        return resource;
    }
}
