/* (c) 2014-2015 Open Source Geospatial Foundation - all rights reserved
 * (c) 2014 OpenPlans
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.platform.resource;

/**
 * Used to manage configuration storage (file system, test harness, or database blob).
 * 用于管理配置存储（文件系统、测试线束或数据库blob）。
 *
 * <p>InputStream used to access configuration information: 用于访问配置信息的InputStream：
 *
 * <pre>
 * <code>
 * Properties properties = new Properties();
 * properties.load( resourceStore.get("module/configuration.properties").in() );
 * </code>
 * </pre>
 *
 * An OutputStream is provided for storage (Resources will be created as needed):
 * 为存储提供了OutputStream（将根据需要创建资源）：
 *
 * <pre>
 * <code>
 * Properties properties = new Properties();
 * properties.put("hello","world");
 * OutputStream out = resourceStore.get("module/configuration.properties").out();
 * properties.store( out, null );
 * out.close();
 * </code>
 * </pre>
 *
 * Resources can also be extracted to a file if needed. 如果需要，还可以将资源提取到文件中。
 *
 * <pre>
 * <code>
 * File file = resourceStore.get("module/logo.png");
 * BufferedImage img = ImageIO.read( file );
 * </code>
 * </pre>
 *
 * The base directory is available using {@link Paths#BASE} (as "") but relative paths ("." and
 * "..") are not supported. 基本目录可以使用{@link Paths#BASE}（as“”）使用，但不支持相对路径（“.”和“..”）。
 *
 * <p>This abstraction assumes a unix like file system, all paths should use forward slash "/" as
 * the separator.
 *
 * <p>这个抽象假设一个类unix的文件系统，所有路径都应该使用正斜杠“/”作为分隔符。
 *
 * @see Resources
 * @see Resource
 */
public interface ResourceStore {
    /**
     * Empty placeholder for ResourceStore. ResourceStore的空占位符。
     *
     * <p>Empty placeholder intended for test cases (used as spring context default when a base
     * directory is not provided). This implementation prevents client code from requiring null
     * checks on {@link ResourceStore#get(String)}. IllegalStateException are thrown by in(), out()
     * and file() which are the usual methods clients require error handling.
     *
     * <p>用于测试用例的空占位符（未提供基目录时用作spring上下文默认值）。此实现可防止客户端代码要求对{@link ResourceStore#get(String)}进行空检查。
     * IllegalStateException由in（）、out（）和file（）引发，这是客户端通常需要错误处理的方法。
     */
    public static ResourceStore EMPTY = new NullResourceStore();

    /**
     * Path based resource access. 基于路径的资源访问。
     *
     * <p>The returned Resource acts as a handle, and may be UNDEFINED. In general Resources are
     * created in a lazy fashion when used for the first time.
     *
     * <p>返回的资源充当句柄，可能未定义。一般来说，资源在第一次使用时是以惰性的方式创建的。
     *
     * @param path Path (using unix conventions, forward slash as separator) of requested resource
     *     请求资源的路径（使用unix约定，正斜杠作为分隔符）
     * @return Resource at the indicated location (null is never returned although Resource may be
     *     UNDEFINED). 指定位置的资源（尽管资源可能未定义，但从不返回空值）。
     * @throws IllegalArgumentException If path is invalid 如果路径无效
     */
    Resource get(String path);

    /**
     * Remove resource at indicated path (including individual resources or directories).
     * 删除指定路径上的资源（包括单个资源或目录）。
     * <p>Returns <code>true</code> if Resource existed and was successfully removed. For read-only
     * content (or if a security check) prevents the resource from being removed <code>false</code>
     * is returned.
     *
     * *< p>返回<code>true< /code>如果资源存在并成功删除。对于只读内容（或如果安全检查）阻止删除资源，则返回<code>false</code>。
     * @param path Path of resource to remove (using unix conventions, forward slash as separator)
     *             要删除的资源路径（使用unix约定，正斜杠作为分隔符）
     * @return <code>false</code> if doesn't exist or unable to remove <code> false <code>如果不存在或无法删除
     *
     */
    boolean remove(String path);

    /**
     * Move resource at indicated path (including individual resources or directories).
     * 在指定的路径上移动资源（包括单个资源或目录）。
     *
     * @param path Path of resource to move (using unix conventions, forward slash as separator)
     *     要移动的资源路径（使用unix约定，正斜杠作为分隔符）
     * @param target path for moved resource 移动资源的路径
     * @return true if resource was moved target path 如果资源已移动到目标路径，则为true
     */
    boolean move(String path, String target);

    /**
     * The Resource Notification Dispatcher 资源通知调度程序
     *
     * @return resource notification dispatcher 资源通知调度程序
     */
    ResourceNotificationDispatcher getResourceNotificationDispatcher();
}
