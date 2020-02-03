/* (c) 2015 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.wms.geojson;

import java.awt.*;
import java.util.Map;
import java.util.logging.Logger;
import javax.measure.Unit;
import no.ecc.vectortile.VectorTileEncoder;
import no.ecc.vectortile.VectorTileEncoderNoClip;
import org.geoserver.wms.WMSMapContent;
import org.geoserver.wms.map.RawMap;
import org.geoserver.wms.mapbox.MapBoxTileBuilderFactory;
import org.geoserver.wms.vector.VectorTileBuilder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.util.logging.Logging;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.PrecisionModel;
import org.locationtech.jts.precision.CoordinatePrecisionReducerFilter;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import si.uom.SI;

public class GeoJsonBuilder implements VectorTileBuilder {
    private static final Logger LOGGER = Logging.getLogger(GeoJsonBuilder.class);
    private CoordinatePrecisionReducerFilter precisionReducerFilter;
    private VectorTileEncoder encoder;

    public GeoJsonBuilder(Rectangle mapSize, ReferencedEnvelope mapArea) {

        final int extent = Math.max(mapSize.width, mapSize.height);
        final int polygonClipBuffer = extent / 32;
        final boolean autoScale = false;
        this.encoder = new VectorTileEncoderNoClip(extent, polygonClipBuffer, autoScale);
        CoordinateReferenceSystem mapCrs = mapArea.getCoordinateReferenceSystem();
        Unit<?> unit = mapCrs.getCoordinateSystem().getAxis(0).getUnit();
        Unit<?> standardUnit = unit.getSystemUnit();
        PrecisionModel pm = null;
        if (SI.RADIAN.equals(standardUnit)) {
            // truncate coords at 6 decimals
            // 在六位小数处截断坐标
            pm = new PrecisionModel(1e6);
        } else if (SI.METRE.equals(standardUnit)) {
            // truncate coords at 2 decimals
            // 在2位小数处截断坐标
            pm = new PrecisionModel(100);
        }
        if (pm != null) {
            precisionReducerFilter = new CoordinatePrecisionReducerFilter(pm);
        }
    }

    @Override
    public void addFeature(
            String layerName,
            String featureId,
            String geometryName,
            Geometry geometry,
            Map<String, Object> properties) {

        if (precisionReducerFilter != null) {
            geometry.apply(precisionReducerFilter);
        }
        int id = -1;
        if (featureId.matches(".*\\.[0-9]+")) {
            try {
                id = Integer.parseInt(featureId.split("\\.")[1]);
            } catch (NumberFormatException e) {
            }
        }
        if (id < 0) {
            LOGGER.warning("Cannot obtain numeric id from featureId: " + featureId);
        }

        encoder.addFeature(layerName, properties, geometry, id);
    }

    @Override
    public RawMap build(WMSMapContent mapContent) {
        byte[] contents = encoder.encode();
        return new RawMap(mapContent, contents, MapBoxTileBuilderFactory.MIME_TYPE);
    }
}
