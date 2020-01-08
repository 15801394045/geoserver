package org.geoserver.web.data.store.shape;

import static org.geotools.data.shapefile.ShapefileDataStoreFactory.URLP;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.geoserver.web.data.store.panel.FileUploadParamPanel;
import org.geoserver.web.util.MapModel;
import org.geoserver.web.wicket.ParamResourceModel;
import org.geoserver.web.wicket.browser.ExtensionFileFilter;

/**
 * @author ily
 * @create 01 03, 2020
 * @since 1.0.0
 */
public class UploadShapefileEditPanel extends ShapefileStoreEditPanel {
    public UploadShapefileEditPanel(String componentId, Form storeEditForm) {
        super(componentId, storeEditForm);
    }

    @Override
    protected Panel buildFileParamPanel(final IModel paramsModel) {
        FileUploadParamPanel file =
                new FileUploadParamPanel(
                        "url",
                        new MapModel(paramsModel, URLP.key),
                        new ParamResourceModel("shapefile", this),
                        false);
        file.setFileFilter(new Model<>(new ExtensionFileFilter(".shp")));
        return file;
    }
}
