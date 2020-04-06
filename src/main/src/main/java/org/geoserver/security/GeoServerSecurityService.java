/* (c) 2014 Open Source Geospatial Foundation - all rights reserved
 * (c) 2001 - 2013 OpenPlans
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.security;

import java.io.IOException;
import org.geoserver.security.config.SecurityNamedServiceConfig;

/**
 * Common interface for {@link GeoServerRoleService} and {@link GeoServerUserGroupService}.
 *
 * @author Justin Deoliveira, OpenGeo
 */
public interface GeoServerSecurityService {

    /**
     * Initialize from configuration object 从配置对象初始化
     *
     * @param config
     * @throws IOException
     */
    void initializeFromConfig(SecurityNamedServiceConfig config) throws IOException;

    /**
     * Flag specifying whether the service can create an associated store. 指定服务是否可以创建关联存储的标志。
     *
     * @return boolean
     */
    boolean canCreateStore();

    /**
     * The name of this service 此服务的名称
     *
     * @return String
     */
    String getName();

    /**
     * Sets the name of this service 设置此服务的名称
     *
     * @param name
     */
    void setName(String name);

    /** */
    /**
     * Sets the reference to the security manager facade for the service. 设置对服务的安全管理器外观的引用。
     *
     * <p>This method is called when the service is loaded.
     *
     * <p>加载服务时调用此方法。
     *
     * @param securityManager GeoServerSecurityManager
     */
    void setSecurityManager(GeoServerSecurityManager securityManager);

    /**
     * Returns the reference to the security manager, set by {@link
     * #setSecurityManager(GeoServerSecurityManager)}. 返回对安全管理器的引用
     *
     * @return GeoServerSecurityManager
     */
    GeoServerSecurityManager getSecurityManager();

    //    /**
    //     * The user details service.
    //     */
    //    GeoserverUserDetailsService getUserDetailsService();
    //
    //    /**
    //     * Sets the user details service.
    //     */
    //    void setUserDetailsService(GeoserverUserDetailsService userDetailsService);
}
