/* (c) 2014 Open Source Geospatial Foundation - all rights reserved
 * (c) 2001 - 2013 OpenPlans
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */

package org.geoserver.security.event;

import java.util.EventObject;
import org.geoserver.security.GeoServerUserGroupService;

/**
 * Event fired after loading user/groups from the backend store into memory 将用户/组从后端存储加载到内存中后激发的事件
 *
 * <p>This event is intended for stateful services of type {@link GeoServerUserGroupService}. If the
 * backend is changed externally and a reload occurs, listeners should be notified.
 *
 * <p>此事件用于{@link GeoServerUserGroupService}类型的有状态服务。如果后端在外部更改并且发生重新加载，则应通知侦听器。
 *
 * @author christian
 */
public class UserGroupLoadedEvent extends EventObject {

    /** */
    private static final long serialVersionUID = 1L;

    public UserGroupLoadedEvent(GeoServerUserGroupService source) {
        super(source);
    }

    public GeoServerUserGroupService getService() {
        return (GeoServerUserGroupService) getSource();
    }
}
