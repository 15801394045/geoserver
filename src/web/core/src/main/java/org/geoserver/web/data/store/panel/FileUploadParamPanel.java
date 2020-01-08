package org.geoserver.web.data.store.panel;

import static org.geoserver.web.util.ZipUtil.createFileName;

import java.io.File;
import java.io.FileFilter;
import java.util.Collections;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.extensions.ajax.markup.html.autocomplete.AutoCompleteTextField;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.form.validation.FormComponentFeedbackBorder;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.validation.IValidator;
import org.geoserver.config.GeoServerDataDirectory;
import org.geoserver.web.GeoServerApplication;
import org.geoserver.web.util.ZipUtil;
import org.geoserver.web.wicket.browser.FileRootsFinder;
import org.geotools.util.logging.Logging;

/**
 * FileUploadParamPanel
 *
 * @author ily
 * @create 01 06, 2020
 * @since 1.0.0
 */
public class FileUploadParamPanel extends Panel implements ParamPanel {
    private static final Logger LOGGER = Logging.getLogger(FileParamPanel.class);
    private static final long serialVersionUID = 2630421795437249103L;
    protected TextField<String> textField;
    protected IModel<? extends FileFilter> fileFilter;
    protected Form storeEditForm;
    /** 上传按钮 */
    protected AjaxSubmitLinkMsg uploadLink;
    /** 消息提示框 */
    protected Label msgLabel;

    protected FileUploadField fileUploadField;

    protected GeoServerApplication getGeoServerApplication() {
        return (GeoServerApplication) getApplication();
    }

    /**
     * @param id
     * @param paramValue
     * @param paramLabelModel
     * @param required
     * @param validators any extra validator that should be added to the input field, or {@code
     *     null}
     */
    @SafeVarargs
    public FileUploadParamPanel(
            final String id,
            final IModel<String> paramValue,
            final IModel<String> paramLabelModel,
            final boolean required,
            IValidator<? super String>... validators) {
        // make the value of the text field the model of this panel, for easy value retrieval
        // 使文本字段的值成为此面板的模型，以便于检索值
        super(id, paramValue);
        // the label
        String requiredMark = required ? " *" : "";
        Label label = new Label("paramName", paramLabelModel.getObject() + requiredMark);
        add(label);

        // the text field, with a decorator for validations
        // 文本字段，带有用于验证的decorator
        FileRootsFinder rootsFinder = new FileRootsFinder(false);
        textField =
                new AutoCompleteTextField<String>("paramValue", getFileModel(paramValue)) {
                    @Override
                    protected Iterator<String> getChoices(String input) {
                        try {
                            // do we need to filter files?
                            FileFilter fileFilter =
                                    FileUploadParamPanel.this.fileFilter != null
                                            ? FileUploadParamPanel.this.fileFilter.getObject()
                                            : null;

                            return rootsFinder.getMatches(input, fileFilter).iterator();
                        } catch (Exception e) {
                            // this is a helper, don't let it break the UI at runtime but log errors
                            // instead
                            LOGGER.log(
                                    Level.INFO,
                                    "Failed to provide autocomplete for path " + input,
                                    e);
                            return Collections.emptyIterator();
                        }
                    }
                };
        textField.setRequired(required);
        textField.setOutputMarkupId(true);
        // set the label to be the paramLabelModel otherwise a validation error would look like
        // "Parameter 'paramValue' is required"
        textField.setLabel(paramLabelModel);
        if (validators != null) {
            for (IValidator<? super String> validator : validators) {
                textField.add(validator);
            }
        }

        FormComponentFeedbackBorder feedback = new FormComponentFeedbackBorder("border");
        feedback.add(textField);
        // 上传组件
        fileUploadField = new FileUploadField("filename");
        fileUploadField.setRequired(true);
        // Explicitly set model so this doesn't use the form model
        fileUploadField.setDefaultModel(new Model<>(""));
        fileUploadField.setOutputMarkupId(true);
        uploadLink =
                new AjaxSubmitLinkMsg("upload", storeEditForm) {
                    @Override
                    protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                        FileUpload upload = fileUploadField.getFileUpload();
                        if (upload == null) {
                            warn("No file selected.");
                            return;
                        }
                        try {
                            GeoServerDataDirectory dd =
                                    getGeoServerApplication()
                                            .getBeanOfType(GeoServerDataDirectory.class);
                            File zip = dd.findOrCreateDataDir(createFileName(null));
                            String absolutePath = zip.getAbsolutePath();
                            ZipUtil.upZipFile(upload.getInputStream(), absolutePath);
                            textField.clearInput();
                            textField.setModelValue(new String[] {absolutePath});
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        uploadLink.msg = "文件上传成功!";
                        fileUploadField.clearInput();
                        fileUploadField.setRequired(false);
                        target.add(textField, msgLabel, fileUploadField);
                    }
                };
        msgLabel = new Label("msg", new PropertyModel(uploadLink, "msg"));
        uploadLink.setDefaultFormProcessing(false);
        uploadLink.setOutputMarkupId(true);

        msgLabel.setOutputMarkupId(true);
        feedback.add(msgLabel);
        feedback.add(uploadLink);
        feedback.add(fileUploadField);
        add(feedback);
    }

    protected IModel<String> getFileModel(IModel<String> paramValue) {
        return new FileModel(paramValue);
    }

    /** The text field stored inside the panel. */
    @Override
    public FormComponent<String> getFormComponent() {
        return textField;
    }

    /**
     * Sets the filter that will act in the file chooser dialog
     *
     * @param fileFilter
     */
    public void setFileFilter(IModel<? extends FileFilter> fileFilter) {
        this.fileFilter = fileFilter;
    }

    class AjaxSubmitLinkMsg extends AjaxSubmitLink {
        public AjaxSubmitLinkMsg(String id, Form<?> form) {
            super(id, form);
        }

        public String msg;
    }
}
