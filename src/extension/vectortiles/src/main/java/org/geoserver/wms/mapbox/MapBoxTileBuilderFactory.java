/* (c) 2015 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.wms.mapbox;

import com.google.common.collect.ImmutableSet;

import java.awt.*;
import java.util.Set;

import org.geoserver.wms.vector.VectorTileBuilderFactory;
import org.geotools.geometry.jts.ReferencedEnvelope;

/**
 * @author Niels Charlier
 */
public class MapBoxTileBuilderFactory implements VectorTileBuilderFactory {

    public static final String MIME_TYPE = "application/vnd.mapbox-vector-tile";
    public static final String LEGACY_MIME_TYPE = "application/x-protobuf;type=mapbox-vector";

    public static final Set<String> OUTPUT_FORMATS =
            ImmutableSet.of(MIME_TYPE, LEGACY_MIME_TYPE, "pbf");

    @Override
    public Set<String> getOutputFormats() {
        return OUTPUT_FORMATS;
    }

    @Override
    public String getMimeType() {
        return MIME_TYPE;
    }

    @Override
    public MapBoxTileBuilder newBuilder(Rectangle screenSize, ReferencedEnvelope mapArea) {
        return new MapBoxTileBuilder(screenSize, mapArea);
    }

    /**
     * For Mapbox tiles, since they are rendered in screen/tile space, oversampling produces more
     * consistent results when zooming. See this question here:
     * 对于贴图盒平铺，由于它们是在屏幕/平铺空间中渲染的，因此在缩放时，过采样会产生更一致的结果。请看这里的问题：
     * <p>https://github.com/mapbox/vector-tiles/issues/45
     *
     * @return
     */
    @Override
    public boolean shouldOversampleScale() {
        return true;
    }

    /**
     * Use 16x oversampling to match actual Mapbox tile extent, which is 4096 for 900913 tiles
     * 使用16x过采样来匹配实际的Mapbox平铺范围，900913平铺为4096
     *
     * @return
     */
    @Override
    public int getOversampleX() {
        return 16;
    }

    /**
     * Use 16x oversampling to match actual Mapbox tile extent, which is 4096 for 900913 tiles
     * 使用16x过采样来匹配实际的Mapbox平铺范围，900913平铺为4096
     *
     * @return
     */
    @Override
    public int getOversampleY() {
        return 16;
    }
}
