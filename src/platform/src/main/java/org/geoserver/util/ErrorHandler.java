/* (c) 2014 Open Source Geospatial Foundation - all rights reserved
 * (c) 2001 - 2013 OpenPlans
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.util;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * A simple sax error handle which hold on to errors and warnings when parsing an xml document.
 * 核心平台模块一个简单的sax错误句柄，它在解析xml文档时保留错误和警告。
 *
 * <p>If constructed with an instance of {@link java.util.logging.Logger} errors will be logged.
 *
 * <p>如果使用{@link java.util.logging.Logger}实例构造，则将记录错误。
 *
 * @author Justin Deoliveira, The Open Planning Project
 */
public class ErrorHandler extends DefaultHandler {

    /** Logger and level */
    Logger logger;

    Level level;

    public List errors = new ArrayList();

    public ErrorHandler() {}

    public ErrorHandler(Logger logger, Level level) {
        this.logger = logger;
        this.level = level;
    }

    public void error(SAXParseException e) throws SAXException {
        e(e);
        super.error(e);
    }

    public void fatalError(SAXParseException e) throws SAXException {
        e(e);
        super.fatalError(e);
    }

    public void warning(SAXParseException e) throws SAXException {
        // ignore
    }

    void e(SAXParseException e) {
        if (logger != null) {
            logger.log(level, e.getLocalizedMessage());
        }

        errors.add(e);
    }
}
