/* (c) 2015 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.wms.vector;

import java.awt.Rectangle;
import java.util.Set;
import org.geotools.geometry.jts.ReferencedEnvelope;

public interface VectorTileBuilderFactory {

    /**
     * A set of identifiers for the format produced by builders from this factory. May include MIME
     * type or file extension. 一组标识符，用于该工厂的建设者生成的格式。可能包括MIME类型或文件扩展名。
     */
    Set<String> getOutputFormats();

    /** The MIME type of the format produced by builders from this factory. 此工厂的建设者生成的格式的MIME类型。 */
    String getMimeType();

    /**
     * Create a builder 创建生成器
     *
     * @param screenSize The extent of the tile in screen coordinates 屏幕坐标中平铺的范围
     * @param mapArea The extent of the tile in target CRS coordinates 目标CRS坐标中的平铺范围
     */
    VectorTileBuilder newBuilder(Rectangle screenSize, ReferencedEnvelope mapArea);

    /**
     * Whether tiles from this builder should be "oversampled", that is, "rendered" at a higher
     * resolution than the tile resolution. The motivation for this is Mapbox vector tiles, which
     * are rendered in screen space per tile and have inconsistent behavior while zooming at lower
     * resolutions.
     * 此生成器中的平铺是否应“过采样”，即以高于平铺分辨率的分辨率“渲染”。这样做的动机是Mapbox矢量平铺，它在每个平铺的屏幕空间中呈现，并且在以较低分辨率缩放时具有不一致的行为。
     *
     * @return whether this builder requires oversampling. defaults to false. 此生成器是否需要过采样。默认为false。
     */
    default boolean shouldOversampleScale() {
        return false;
    }

    /**
     * @return the horizontal oversampling factor. default is 1, no oversampling 水平过采样系数。默认值为1，无过采样
     */
    default int getOversampleX() {
        return 1;
    }

    /**
     * @return the vertical oversampling factor. default is 1, no oversampling 垂直过采样因子。默认值为1，无过采样
     */
    default int getOversampleY() {
        return 1;
    }
}
