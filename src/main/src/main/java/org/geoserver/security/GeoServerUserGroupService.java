/* (c) 2014 Open Source Geospatial Foundation - all rights reserved
 * (c) 2001 - 2013 OpenPlans
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.security;

import java.io.IOException;
import java.util.SortedSet;
import org.geoserver.security.event.UserGroupLoadedEvent;
import org.geoserver.security.event.UserGroupLoadedListener;
import org.geoserver.security.impl.GeoServerUser;
import org.geoserver.security.impl.GeoServerUserGroup;
import org.geoserver.security.password.PasswordValidator;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * This interface is an extenstion to {@link UserDetailsService} 此接口是{@link UserDetailsService}的扩展
 *
 * <p>A class implementing this interface implements a read only backend for user and group
 * management
 *
 * <p>实现此接口的类为用户和组管理实现只读后端
 *
 * @author christian
 */
public interface GeoServerUserGroupService extends GeoServerSecurityService, UserDetailsService {

    /**
     * Creates the user group store that corresponds to this service, or null if creating a store is
     * not supported. 创建与此服务对应的用户组存储，如果不支持创建存储，则为空。
     *
     * <p>Implementations that do not support a store should ensure that {@link #canCreateStore()}
     * returns <code>false</code>.
     *
     * <p>不支持存储的实现应确保{@link #canCreateStore（）}返回<code>false</code>。
     *
     * @return GeoServerUserGroupStore
     * @throws IOException IOException
     */
    GeoServerUserGroupStore createStore() throws IOException;

    /**
     * Register for notifications on load 加载时注册通知
     *
     * @param listener UserGroupLoadedListener
     */
    void registerUserGroupLoadedListener(UserGroupLoadedListener listener);

    /**
     * Unregister for notifications on store/load 注销存储/加载时的通知
     *
     * @param listener UserGroupLoadedListener
     */
    void unregisterUserGroupLoadedListener(UserGroupLoadedListener listener);

    /**
     * Returns the the group object, null if not found 返回组对象，如果找不到则为空
     *
     * @param groupname groupname
     * @return null if group not found
     * @throws IOException DataAccessException
     */
    GeoServerUserGroup getGroupByGroupname(String groupname) throws IOException;

    /**
     * Returns the the user object, null if not found 返回用户对象，如果找不到则为空
     *
     * @param username username
     * @return null if user not found
     * @throws IOException DataAccessException
     */
    GeoServerUser getUserByUsername(String username) throws IOException;

    /**
     * Create a user object. Implementations can use subclasses of {@link GeoServerUser}
     * 创建用户对象。实现可以使用{@link GeoServerUser}的子类
     *
     * @param username 用户名
     * @param password 密码
     * @param isEnabled 已启用
     * @return GeoServerUser
     * @throws IOException IOException
     */
    GeoServerUser createUserObject(String username, String password, boolean isEnabled)
            throws IOException;

    /**
     * Create a user object. Implementations can use classes implementing {@link GeoServerUserGroup}
     *
     * @param groupname
     * @param password
     * @param isEnabled
     */
    /**
     * Create a user object. Implementations can use classes implementing {@link GeoServerUserGroup}
     * 创建用户对象。实现可以使用实现{@link GeoServerUserGroup}的类
     *
     * @param groupname 组名
     * @param isEnabled 已启用
     * @return GeoServerUserGroup
     * @throws IOException IOException
     */
    GeoServerUserGroup createGroupObject(String groupname, boolean isEnabled) throws IOException;

    /**
     * Returns the list of users. 返回用户列表。
     *
     * @return a collection which cannot be modified 无法修改的集合
     */
    SortedSet<GeoServerUser> getUsers() throws IOException;

    /**
     * Returns the list of GeoserverUserGroups. 返回GeoserverUserGroups的列表。
     *
     * @return a collection which cannot be modified 无法修改的集合
     */
    SortedSet<GeoServerUserGroup> getUserGroups() throws IOException;

    /**
     * get users for a group 获取组的用户
     *
     * @param group GeoServerUserGroup
     * @return a collection which cannot be modified
     */
    SortedSet<GeoServerUser> getUsersForGroup(GeoServerUserGroup group) throws IOException;

    /**
     * get the groups for a user, an implementation not supporting user groups returns an empty
     * collection 获取用户的组，不支持用户组的实现将返回空集合
     *
     * @param user GeoServerUser
     * @return a collection which cannot be modified
     */
    SortedSet<GeoServerUserGroup> getGroupsForUser(GeoServerUser user) throws IOException;

    /**
     * load from backendstore. On success, a {@link UserGroupLoadedEvent} should be triggered
     * 从后端存储加载。成功时，应触发{@link UserGroupLoadedEvent}
     */
    void load() throws IOException;

    /**
     * @return the Spring name of the {@link GeoServerPasswordEncoder} object. mandatory, default is
     *     {@link GeoServerDigestPasswordEncoder#BeanName}.
     */
    String getPasswordEncoderName();

    /**
     * @return the name of the {@link PasswordValidator} object. mandatory, default is {@link
     *     PasswordValidator#DEFAULT_NAME} Validators can be loaded using {@link
     *     GeoServerSecurityManager#loadPasswordValidator(String)}
     */
    String getPasswordValidatorName();

    /** @return the number of users */
    int getUserCount() throws IOException;

    /** @return the number of groups */
    int getGroupCount() throws IOException;

    /**
     * Returns a set of {@link GeoServerUser} objects having the specified property
     * 返回一组具有指定属性的{@link GeoServerUser}对象
     *
     * @param propname
     * @throws IOException
     */
    SortedSet<GeoServerUser> getUsersHavingProperty(String propname) throws IOException;

    /**
     * Returns the number of {@link GeoServerUser} objects having the specified property
     * 返回具有指定属性的{@link GeoServerUser}对象的数目
     *
     * @param propname
     * @throws IOException
     */
    int getUserCountHavingProperty(String propname) throws IOException;

    /**
     * Returns a set of {@link GeoServerUser} objects NOT having the specified property
     *
     * @param propname
     * @throws IOException
     */
    SortedSet<GeoServerUser> getUsersNotHavingProperty(String propname) throws IOException;

    /**
     * Returns the number of {@link GeoServerUser} objects NOT having the specified property
     *
     * @param propname
     * @throws IOException
     */
    int getUserCountNotHavingProperty(String propname) throws IOException;

    /**
     * Returns a set of {@link GeoServerUser} objects having the property with the specified value
     *
     * @param propname
     * @param propvalue
     * @throws IOException
     */
    SortedSet<GeoServerUser> getUsersHavingPropertyValue(String propname, String propvalue)
            throws IOException;

    /**
     * Returns the number of {@link GeoServerUser} objects having the property with the specified
     * value
     *
     * @param propname
     * @param propvalue
     * @throws IOException
     */
    int getUserCountHavingPropertyValue(String propname, String propvalue) throws IOException;
}
