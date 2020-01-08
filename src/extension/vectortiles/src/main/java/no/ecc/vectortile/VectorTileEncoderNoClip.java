/* (c) 2016 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package no.ecc.vectortile;

import org.locationtech.jts.geom.Geometry;

/**
 * 提供不执行任何剪辑的VectorTileEncoder。我们的剪辑系统是“更好的”（这里更健壮、更快和可维护）。
 *
 * @author ily
 */
public class VectorTileEncoderNoClip extends VectorTileEncoder {

    public VectorTileEncoderNoClip(int extent, int polygonClipBuffer, boolean autoScale) {
        super(extent, polygonClipBuffer, autoScale);
    }

    /**
     * 返回原始几何图形-不剪切。假设上游已经剪掉了！
     *
     * @param geometry Geometry
     * @return returns original geometry - no clipping. Assume upstream has already clipped!
     */
    @Override
    protected Geometry clipGeometry(Geometry geometry) {
        return geometry;
    }
}
