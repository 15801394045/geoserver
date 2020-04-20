/* (c) 2014 - 2015 Open Source Geospatial Foundation - all rights reserved
 * (c) 2013 OpenPlans
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.util;

import org.geoserver.config.GeoServer;
import org.geotools.util.PreventLocalEntityResolver;
import org.xml.sax.EntityResolver;

/**
 * Creates an EntityResolver using geoserver configuration settings.
 * 使用geoserver配置设置创建EntityResolver。
 *
 * @author Davide Savazzi - geo-solutions.it
 */
public class EntityResolverProvider {

    private static EntityResolver entityResolver = PreventLocalEntityResolver.INSTANCE;

    /** A entity resolver provider that always disables entity resolution 始终禁用实体解析的实体解析程序提供程序 */
    public static final EntityResolverProvider RESOLVE_DISABLED_PROVIDER =
            new EntityResolverProvider(null);

    private final GeoServer geoServer;

    public EntityResolverProvider(GeoServer geoServer) {
        this.geoServer = geoServer;
    }

    public static void setEntityResolver(EntityResolver resolver) {
        entityResolver = resolver;
    }

    public EntityResolver getEntityResolver() {
        if (geoServer != null) {
            Boolean externalEntitiesEnabled = geoServer.getGlobal().isXmlExternalEntitiesEnabled();
            if (externalEntitiesEnabled != null && externalEntitiesEnabled) {
                // XML parser will try to resolve entities
                // XML解析器将尝试解析实体
                return null;
            }
        }

        return entityResolver;
    }
}
