package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionKind.loadConceptionEntities;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.KindEntityAttributeRuntimeStatistics;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.AttributeDataType;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.ConceptionKind;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.SecondaryIconTitle;
import com.viewfunction.docg.element.userInterfaceUtil.CommonUIOperationUtil;
import com.viewfunction.docg.util.config.SystemAdminCfgPropertiesHandler;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class LoadARROWFormatConceptionEntitiesView extends VerticalLayout {

    private String conceptionKindName;
    private String TEMP_FILES_STORAGE_LOCATION =
            SystemAdminCfgPropertiesHandler.getPropertyValue(SystemAdminCfgPropertiesHandler.TEMP_FILES_STORAGE_LOCATION);
    private Dialog containerDialog;
    private SecondaryIconTitle uploadSectionTitle;
    private HorizontalLayout controlOptionsLayout;
    private Upload upload;
    private Button confirmButton;
    private Button cancelImportButton;
    private int maxSizeOfFileInMBForUpload = 0;

    public LoadARROWFormatConceptionEntitiesView(String conceptionKindName, int viewWidth){
        this.setWidth(100, Unit.PERCENTAGE);
        this.conceptionKindName = conceptionKindName;

        VerticalLayout operationAreaLayout = new VerticalLayout();
        operationAreaLayout.setWidth(100,Unit.PERCENTAGE);
        operationAreaLayout.setHeight(400,Unit.PIXELS);
        operationAreaLayout.setSpacing(false);
        operationAreaLayout.setPadding(false);
        operationAreaLayout.setMargin(false);
        add(operationAreaLayout);

        uploadSectionTitle = new SecondaryIconTitle(new Icon(VaadinIcon.ANGLE_DOUBLE_RIGHT),"上传 Apache Arrow 格式文件");
        uploadSectionTitle.setWidth(100,Unit.PERCENTAGE);
        uploadSectionTitle.getStyle().set("padding-top", "var(--lumo-space-s)");
        uploadSectionTitle.getStyle()
                .set("border-bottom", "1px solid var(--lumo-contrast-10pct)")
                .set("padding-bottom", "var(--lumo-space-s)");
        operationAreaLayout.add(uploadSectionTitle);

        controlOptionsLayout = new HorizontalLayout();
        controlOptionsLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        controlOptionsLayout.setHeight(10,Unit.PIXELS);
        operationAreaLayout.add(controlOptionsLayout);

        MemoryBuffer buffer = new MemoryBuffer();
        upload = new Upload(buffer);
        upload.setWidth(100, Unit.PERCENTAGE);
        upload.setHeight(360,Unit.PIXELS);
        upload.setAutoUpload(false);

        //MIME types
        //https://developer.mozilla.org/en-US/docs/Web/HTTP/Basics_of_HTTP/MIME_types/Common_types
        //Any kind of binary data : application/octet-stream
        upload.setAcceptedFileTypes("application/octet-stream",".arrow");

        maxSizeOfFileInMBForUpload = Integer.valueOf(
                SystemAdminCfgPropertiesHandler.getPropertyValue(SystemAdminCfgPropertiesHandler.MAX_SIZE_OF_FILE_IN_MB_FOR_UPLOAD));
        int maxFileSizeInBytes = maxSizeOfFileInMBForUpload * 1024 * 1024; // 10MB
        upload.setMaxFileSize(maxFileSizeInBytes);

        Button uploadButton = new Button("上传 Apache Arrow 文件 ...");
        uploadButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        upload.setUploadButton(uploadButton);
        Span dropLabel = new Span("请将 Arrow 文件拖放到此处");
        dropLabel.addClassNames("text-xs","text-secondary");
        upload.setDropLabel(dropLabel);

        upload.addSucceededListener(event -> {

        });

        upload.addFailedListener(event ->{});
        upload.addFileRejectedListener(event ->{});
        upload.addFinishedListener(event ->{});
        upload.addStartedListener(event ->{});
        operationAreaLayout.add(upload);

        HorizontalLayout spaceDivLayout2 = new HorizontalLayout();
        spaceDivLayout2.setWidthFull();
        spaceDivLayout2.getStyle().set("border-bottom", "1px solid var(--lumo-contrast-20pct)");
        add(spaceDivLayout2);

        HorizontalLayout buttonbarLayout = new HorizontalLayout();
        add(buttonbarLayout);
        setHorizontalComponentAlignment(Alignment.END,buttonbarLayout);

        confirmButton = new Button("确认导入概念类型实体数据",new Icon(VaadinIcon.CHECK_CIRCLE));
        confirmButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        confirmButton.setEnabled(false);
        confirmButton.setDisableOnClick(true);
        confirmButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                //doImportCSVFile();
            }
        });
        buttonbarLayout.add(confirmButton);

        cancelImportButton = new Button("取消导入已上传文件数据");
        cancelImportButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE,ButtonVariant.LUMO_SMALL);
        cancelImportButton.setEnabled(false);
        cancelImportButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                //cancelImportUploadedFile();
            }
        });

        buttonbarLayout.add(cancelImportButton);
    }

    public void setContainerDialog(Dialog containerDialog) {
        this.containerDialog = containerDialog;
    }
}
