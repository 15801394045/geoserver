/* (c) 2014 Open Source Geospatial Foundation - all rights reserved
 * (c) 2001 - 2013 OpenPlans
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.platform.exception;

/**
 * Interface class for exceptions whose messages can be localized. 可本地化其消息的异常的接口类。
 *
 * @see GeoServerException
 * @see GeoServerRuntimException
 * @author Justin Deoliveira, OpenGeo
 */
public interface IGeoServerException {

    /**
     * Id for the exception, used to locate localized message for the exception. 异常的Id，用于定位异常的本地化消息。
     */
    String getId();

    /** Arguments to pass into the localized exception message 要传递到本地化异常消息中的参数 */
    Object[] getArgs();
}
