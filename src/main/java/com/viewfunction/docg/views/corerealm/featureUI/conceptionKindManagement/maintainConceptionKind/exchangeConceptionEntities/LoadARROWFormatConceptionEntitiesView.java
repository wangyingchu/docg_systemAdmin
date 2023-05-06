package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionKind.exchangeConceptionEntities;

import cn.hutool.core.io.FileUtil;
import cn.hutool.extra.pinyin.PinyinUtil;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.viewfunction.docg.coreRealm.realmServiceCore.exception.CoreRealmServiceRuntimeException;
import com.viewfunction.docg.coreRealm.realmServiceCore.operator.EntitiesExchangeOperator;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.EntitiesOperationStatistics;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.ConceptionKind;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.SecondaryIconTitle;
import com.viewfunction.docg.element.eventHandling.ConceptionEntitiesCountRefreshEvent;
import com.viewfunction.docg.util.ResourceHolder;
import com.viewfunction.docg.util.config.SystemAdminCfgPropertiesHandler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

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
    private String uploadedFileName;
    private SecondaryIconTitle uploadZipFileInfoSectionTitle;
    private HorizontalLayout uploadedFileInfoLayout;
    private Label fileNameLabel;
    private File targetArrowFile;
    private RadioButtonGroup<String> arrowFileSourceGroup;
    private TextField filePathInput;

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
        operationAreaLayout.add(controlOptionsLayout);

        Label dataSplitChar = new Label("Arrow 文件来源:");
        dataSplitChar.addClassNames("text-xs","text-secondary");
        controlOptionsLayout.add(dataSplitChar);

        arrowFileSourceGroup = new RadioButtonGroup<>();
        arrowFileSourceGroup.setItems("客户端上传", "服务器本地路径");
        arrowFileSourceGroup.setValue("客户端上传");
        arrowFileSourceGroup.setRenderer(new ComponentRenderer<>(option -> {
            Label optionLabel = new Label(option);
            optionLabel.addClassNames("text-xs","text-secondary");
            return optionLabel;
        }));
        controlOptionsLayout.add(arrowFileSourceGroup);
        arrowFileSourceGroup.addValueChangeListener(new HasValue.ValueChangeListener<AbstractField.ComponentValueChangeEvent<RadioButtonGroup<String>, String>>() {
            @Override
            public void valueChanged(AbstractField.ComponentValueChangeEvent<RadioButtonGroup<String>, String> radioButtonGroupStringComponentValueChangeEvent) {
                String currentValue = radioButtonGroupStringComponentValueChangeEvent.getValue();
                if(currentValue.equals("客户端上传")){
                    filePathInput.setEnabled(false);
                    ((Button)upload.getUploadButton()).setEnabled(true);
                }
                if(currentValue.equals("服务器本地路径")){
                    filePathInput.setEnabled(true);
                    upload.clearFileList();
                    ((Button)upload.getUploadButton()).setEnabled(false);
                }
            }
        });

        HorizontalLayout arrowFilePathInfoLayout = new HorizontalLayout();
        arrowFilePathInfoLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        operationAreaLayout.add(arrowFilePathInfoLayout);

        Label filePathPrompt = new Label("Arrow 文件服务器路径:");
        filePathPrompt.addClassNames("text-xs","text-secondary");
        arrowFilePathInfoLayout.add(filePathPrompt);
        filePathInput = new TextField();
        filePathInput.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        filePathInput.setWidth(350,Unit.PIXELS);
        filePathInput.setEnabled(false);
        arrowFilePathInfoLayout.add(filePathInput);

        filePathInput.addValueChangeListener(new HasValue.ValueChangeListener<AbstractField.ComponentValueChangeEvent<TextField, String>>() {
            @Override
            public void valueChanged(AbstractField.ComponentValueChangeEvent<TextField, String> textFieldStringComponentValueChangeEvent) {
                String currentPathValue = textFieldStringComponentValueChangeEvent.getValue();
                if(currentPathValue != null && !currentPathValue.equals("")){
                    confirmButton.setEnabled(true);
                }else{
                    confirmButton.setEnabled(false);
                }
            }
        });

        MemoryBuffer buffer = new MemoryBuffer();
        upload = new Upload(buffer);
        upload.setWidth(100, Unit.PERCENTAGE);
        upload.setHeight(350,Unit.PIXELS);
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
            String fileName = event.getFileName();
            uploadedFileName = fileName;
            InputStream inputStream = buffer.getInputStream();
            File arrowFile = processFile(inputStream, fileName);
            if(arrowFile != null){
                confirmButton.setEnabled(true);
                cancelImportButton.setEnabled(true);
                fileNameLabel.setText(uploadedFileName);
                displayAttributesMappingUI();
            }
        });

        upload.addFailedListener(event ->{});
        upload.addFileRejectedListener(event ->{});
        upload.addFinishedListener(event ->{});
        upload.addStartedListener(event ->{});
        operationAreaLayout.add(upload);

        uploadZipFileInfoSectionTitle = new SecondaryIconTitle(new Icon(VaadinIcon.FILE_ZIP),"已上传压缩文件信息");
        uploadZipFileInfoSectionTitle.setWidth(100,Unit.PERCENTAGE);
        uploadZipFileInfoSectionTitle.getStyle().set("padding-top", "var(--lumo-space-s)");
        uploadZipFileInfoSectionTitle.getStyle()
                .set("border-bottom", "1px solid var(--lumo-contrast-10pct)")
                .set("padding-bottom", "var(--lumo-space-s)");
        operationAreaLayout.add(uploadZipFileInfoSectionTitle);

        uploadedFileInfoLayout = new HorizontalLayout();
        uploadedFileInfoLayout.getStyle().set("padding-top", "10px").set("padding-bottom", "5px");
        uploadedFileInfoLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        operationAreaLayout.add(uploadedFileInfoLayout);
        Icon fileIcon = VaadinIcon.FILE.create();
        fileIcon.setSize("8px");
        fileNameLabel = new Label("");
        fileNameLabel.addClassNames("text-xs","text-secondary");
        uploadedFileInfoLayout.add(fileIcon,fileNameLabel);

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
                doImportARROWFile();
            }
        });
        buttonbarLayout.add(confirmButton);

        cancelImportButton = new Button("取消导入已上传文件数据");
        cancelImportButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE,ButtonVariant.LUMO_SMALL);
        cancelImportButton.setEnabled(false);
        cancelImportButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                cancelImportUploadedFile();
            }
        });

        buttonbarLayout.add(cancelImportButton);
        displayUploadUI();
    }

    public void setContainerDialog(Dialog containerDialog) {
        this.containerDialog = containerDialog;
    }

    private File processFile(InputStream inputStream,String fileName){
        try {
            File fileFolder = new File(TEMP_FILES_STORAGE_LOCATION);
            if(!fileFolder.exists()){
                fileFolder.mkdirs();
            }
            String savedFileURI = TEMP_FILES_STORAGE_LOCATION+"/"+System.currentTimeMillis()+"_"+ PinyinUtil.getPinyin(fileName,"");
            targetArrowFile = new File(savedFileURI);
            FileOutputStream outStream  = new FileOutputStream(targetArrowFile);
            int byteRead = 0;
            byte[] buffer = new byte[8192];
            while((byteRead = inputStream.read(buffer,0,8192)) != -1){
                outStream.write(buffer,0,byteRead);
            }
            outStream.close();
            inputStream.close();
            return targetArrowFile;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void displayUploadUI(){
        uploadSectionTitle.setVisible(true);
        controlOptionsLayout.setVisible(true);
        upload.setVisible(true);
        uploadZipFileInfoSectionTitle.setVisible(false);
        uploadedFileInfoLayout.setVisible(false);
    }

    private void displayAttributesMappingUI(){
        uploadSectionTitle.setVisible(false);
        controlOptionsLayout.setVisible(false);
        upload.setVisible(false);
        uploadZipFileInfoSectionTitle.setVisible(true);
        uploadedFileInfoLayout.setVisible(true);
    }

    private void cancelImportUploadedFile(){
        upload.clearFileList();
        displayUploadUI();
        confirmButton.setEnabled(false);
        cancelImportButton.setEnabled(false);
        if(targetArrowFile != null){
            FileUtil.del(targetArrowFile);
        }
    }

    private void doImportARROWFile(){
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        EntitiesExchangeOperator entitiesExchangeOperator = coreRealm.getEntitiesExchangeOperator();
        String importFileSourceType = arrowFileSourceGroup.getValue();
        String targetArrowFilePath = "";
        if(importFileSourceType.equals("客户端上传")){
            targetArrowFilePath = targetArrowFile.getAbsolutePath();
        }
        if(importFileSourceType.equals("服务器本地路径")){
            targetArrowFilePath = filePathInput.getValue().trim();
        }
        EntitiesOperationStatistics importResult =
                entitiesExchangeOperator.importConceptionEntitiesFromArrow(this.conceptionKindName,targetArrowFilePath);
        if(importResult.getSuccessItemsCount() >0 ){
            ConceptionKind targetConceptionKind = coreRealm.getConceptionKind(this.conceptionKindName);
            long conceptionEntitiesCount = 0;
            try {
                conceptionEntitiesCount = targetConceptionKind.countConceptionEntities();
                ConceptionEntitiesCountRefreshEvent conceptionEntitiesCountRefreshEvent = new ConceptionEntitiesCountRefreshEvent();
                conceptionEntitiesCountRefreshEvent.setConceptionEntitiesCount(conceptionEntitiesCount);
                conceptionEntitiesCountRefreshEvent.setConceptionKindName(this.conceptionKindName);
                ResourceHolder.getApplicationBlackboard().fire(conceptionEntitiesCountRefreshEvent);
            } catch (CoreRealmServiceRuntimeException e) {
                throw new RuntimeException(e);
            }
            if(targetArrowFile != null && targetArrowFile.exists()){
                FileUtil.del(targetArrowFile);
            }
            if(containerDialog != null){
                containerDialog.close();
            }

            Notification notification = new Notification();
            notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            Div text = new Div(new Text("概念类型 "+conceptionKindName+" 数据导入操作成功"));
            Button closeButton = new Button(new Icon("lumo", "cross"));
            closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
            closeButton.addClickListener(event -> {
                notification.close();
            });
            HorizontalLayout layout = new HorizontalLayout(text, closeButton);
            layout.setWidth(100, Unit.PERCENTAGE);
            layout.setFlexGrow(1,text);
            notification.add(layout);

            VerticalLayout notificationMessageContainer = new VerticalLayout();
            notificationMessageContainer.add(new Div(new Text("Arrow 数据文件: "+uploadedFileName)));
            notificationMessageContainer.add(new Div(new Text("当前概念实体总数: " + conceptionEntitiesCount)));
            //notificationMessageContainer.add(new Div(new Text("操作摘要: "+importResult.getOperationSummary())));
            notificationMessageContainer.add(new Div(new Text("导入成功实体数: "+importResult.getSuccessItemsCount())));
            notificationMessageContainer.add(new Div(new Text("导入失败实体数: "+importResult.getFailItemsCount())));
            notificationMessageContainer.add(new Div(new Text("操作开始时间: "+importResult.getStartTime())));
            notificationMessageContainer.add(new Div(new Text("操作结束时间: "+importResult.getFinishTime())));
            notification.add(notificationMessageContainer);
            notification.open();
        }
    }
}
