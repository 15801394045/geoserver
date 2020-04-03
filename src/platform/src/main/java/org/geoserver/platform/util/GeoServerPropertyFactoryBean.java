/* (c) 2014 - 2015 Open Source Geospatial Foundation - all rights reserved
 * (c) 2001 - 2013 OpenPlans
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */

package org.geoserver.platform.util;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.geoserver.platform.GeoServerExtensions;
import org.geotools.util.logging.Logging;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Spring FactoryBean that can create a bean based on the value of a system property, context
 * parameter, or environment variable Spring FactoryBean，它可以基于系统属性、上下文参数或环境变量的值创建bean
 *
 * @author Kevin Smith, Boundless
 * @param <T>
 */
public abstract class GeoServerPropertyFactoryBean<T> extends AbstractFactoryBean<T>
        implements ApplicationContextAware {
    private static final Logger LOGGER = Logging.getLogger(GeoServerPropertyFactoryBean.class);

    private ApplicationContext applicationContext;

    private final String propertyName;
    private String defaultValue;

    /** @param propertyName The property to check when creating a bean 创建bean时要检查的属性 */
    public GeoServerPropertyFactoryBean(final String propertyName) {
        super();
        this.propertyName = propertyName;
    }

    @Override
    protected T createInstance() throws Exception {
        String value = GeoServerExtensions.getProperty(propertyName, applicationContext);
        Object[] logParams = new Object[] {propertyName, value, getDefaultValue()};
        if (value == null || value.isEmpty()) {
            LOGGER.log(
                    Level.INFO,
                    "{0} was empty or undefined, using default \"{2}\" instead",
                    logParams);
            return getDefaultBean();
        }
        T bean = createInstance(value);
        if (bean == null) {
            LOGGER.log(
                    Level.WARNING,
                    "{0} had unexpected value \"{1}\", using default \"{2}\" instead",
                    logParams);
            bean = getDefaultBean();
        }
        return bean;
    }

    private T getDefaultBean() throws Exception {
        String value = getDefaultValue();
        if (value == null) {
            throw new IllegalStateException("No default value for " + propertyName);
        }
        T defaultBean = createInstance(value);
        if (defaultBean == null) {
            throw new IllegalStateException(
                    propertyName + " default value \"" + value + "\" did not prduce a bean");
        }
        return defaultBean;
    }

    /**
     * Create a bean based on the given property value
     *
     * @param propertyValue
     * @return
     */
    protected abstract T createInstance(final String propertyValue) throws Exception;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /** @return the defaultValue */
    public String getDefaultValue() {
        return defaultValue;
    }

    /** @param defaultValue the defaultValue to set */
    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }
}
