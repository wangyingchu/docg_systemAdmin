package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionKind.loadConceptionEntities;

import cn.hutool.core.io.FileTypeUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ZipUtil;
import cn.hutool.extra.pinyin.PinyinUtil;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;

import com.viewfunction.docg.coreRealm.realmServiceCore.exception.CoreRealmServiceRuntimeException;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.ConceptionKind;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.geospatial.GeospatialOperationUtil;
import com.viewfunction.docg.element.commonComponent.SecondaryIconTitle;
import com.viewfunction.docg.element.eventHandling.ConceptionEntitiesCountRefreshEvent;
import com.viewfunction.docg.element.userInterfaceUtil.CommonUIOperationUtil;
import com.viewfunction.docg.util.ResourceHolder;
import com.viewfunction.docg.util.config.SystemAdminCfgPropertiesHandler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class LoadSHPFormatConceptionEntitiesView extends VerticalLayout {

    private String conceptionKindName;
    private String TEMP_FILES_STORAGE_LOCATION =
            SystemAdminCfgPropertiesHandler.getPropertyValue(SystemAdminCfgPropertiesHandler.TEMP_FILES_STORAGE_LOCATION);
    private Dialog containerDialog;
    private SecondaryIconTitle uploadSectionTitle;
    private HorizontalLayout controlOptionsLayout;
    private TextField fileEncodeInput;
    private Checkbox removeExistDataCheckbox;
    private Upload upload;
    private Button confirmButton;
    private Button cancelImportButton;
    private int maxSizeOfFileInMBForUpload = 0;
    private String uploadedFileName;
    private boolean processFileSuccess;
    private File targetSHPFile;
    private File importWorkingFolder;
    private SecondaryIconTitle uploadZipFileInfoSectionTitle;
    private HorizontalLayout uploadedFileInfoLayout;
    private Label fileNameLabel;
    private VerticalLayout zipFileContentLayout;
    private Scroller scroller;

    public LoadSHPFormatConceptionEntitiesView(String conceptionKindName, int viewWidth){
        this.setWidth(100, Unit.PERCENTAGE);
        this.conceptionKindName = conceptionKindName;

        VerticalLayout operationAreaLayout = new VerticalLayout();
        operationAreaLayout.setWidth(100,Unit.PERCENTAGE);
        operationAreaLayout.setHeight(400,Unit.PIXELS);
        operationAreaLayout.setSpacing(false);
        operationAreaLayout.setPadding(false);
        operationAreaLayout.setMargin(false);
        add(operationAreaLayout);

        uploadSectionTitle = new SecondaryIconTitle(new Icon(VaadinIcon.FILE_ZIP),"上传包含 SHP 数据的 ZIP 压缩文件");
        uploadSectionTitle.setWidth(100,Unit.PERCENTAGE);
        uploadSectionTitle.getStyle().set("padding-top", "var(--lumo-space-s)");
        uploadSectionTitle.getStyle()
                .set("border-bottom", "1px solid var(--lumo-contrast-10pct)")
                .set("padding-bottom", "var(--lumo-space-s)");
        operationAreaLayout.add(uploadSectionTitle);

        controlOptionsLayout = new HorizontalLayout();
        controlOptionsLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        operationAreaLayout.add(controlOptionsLayout);

        Label dataSplitChar = new Label("分隔符:");
        dataSplitChar.addClassNames("text-xs","text-secondary");
        controlOptionsLayout.add(dataSplitChar);

        fileEncodeInput = new TextField();
        fileEncodeInput.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        fileEncodeInput.setValue("UTF-8");
        controlOptionsLayout.add(fileEncodeInput);

        removeExistDataCheckbox = new Checkbox();
        removeExistDataCheckbox.setLabel("清除概念类型中已有数据");
        removeExistDataCheckbox.addClassNames("text-xs","text-secondary");
        removeExistDataCheckbox.getStyle().set("padding-bottom", "5px");
        operationAreaLayout.add(removeExistDataCheckbox);

        MemoryBuffer buffer = new MemoryBuffer();
        upload = new Upload(buffer);
        upload.setWidth(100, Unit.PERCENTAGE);
        upload.setHeight(350,Unit.PIXELS);
        upload.setAutoUpload(false);

        //MIME types
        //https://developer.mozilla.org/en-US/docs/Web/HTTP/Basics_of_HTTP/MIME_types/Common_types
        upload.setAcceptedFileTypes("application/zip",".zip");

        maxSizeOfFileInMBForUpload = Integer.valueOf(
                SystemAdminCfgPropertiesHandler.getPropertyValue(SystemAdminCfgPropertiesHandler.MAX_SIZE_OF_FILE_IN_MB_FOR_UPLOAD));
        int maxFileSizeInBytes = maxSizeOfFileInMBForUpload * 1024 * 1024; // 10MB
        upload.setMaxFileSize(maxFileSizeInBytes);

        Button uploadButton = new Button("上传 ZIP 文件 ...");
        uploadButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        upload.setUploadButton(uploadButton);
        Span dropLabel = new Span("请将 ZIP 文件拖放到此处");
        dropLabel.addClassNames("text-xs","text-secondary");
        upload.setDropLabel(dropLabel);

        upload.addSucceededListener(event -> {
            String fileName = event.getFileName();
            uploadedFileName = fileName;
            InputStream inputStream = buffer.getInputStream();
            File shpFile = processFile(inputStream, fileName);
            if(processFileSuccess && shpFile != null){
                targetSHPFile = shpFile;
                confirmButton.setEnabled(true);
                cancelImportButton.setEnabled(true);
                fileNameLabel.setText(fileName);
                zipFileContentLayout.removeAll();
                if(importWorkingFolder != null){
                    File[] childFileArray = importWorkingFolder.listFiles();
                    for(File currentFile:childFileArray){
                        String currentFileName = currentFile.getName();
                        Icon fileIcon = null;
                        if(currentFile.isDirectory()){
                            fileIcon = VaadinIcon.FOLDER.create();
                        }else{
                            fileIcon = VaadinIcon.FILE.create();
                        }
                        fileIcon.setSize("8px");
                        fileIcon.getStyle().set("padding-left","10px");
                        Label fileNameLabel = new Label(currentFileName);
                        fileNameLabel.addClassNames("text-xs","text-secondary");
                        HorizontalLayout fileInfoLayout = new HorizontalLayout(fileIcon,fileNameLabel);
                        fileInfoLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
                        zipFileContentLayout.add(fileInfoLayout);
                    }
                }
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

        zipFileContentLayout = new VerticalLayout();
        zipFileContentLayout.setWidth(viewWidth - 10,Unit.PIXELS);
        zipFileContentLayout.setPadding(false);
        zipFileContentLayout.setMargin(false);
        zipFileContentLayout.setSpacing(false);

        scroller = new Scroller(zipFileContentLayout);
        scroller.setHeight(500,Unit.PIXELS);
        operationAreaLayout.add(scroller);

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
                doImportSHPFile();
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

    private File processFile(InputStream inputStream,String fileName){
        File currentSHPFile = null;
        try {
            File fileFolder = new File(TEMP_FILES_STORAGE_LOCATION);
            if(!fileFolder.exists()){
                fileFolder.mkdirs();
            }

            String savedFileURI = TEMP_FILES_STORAGE_LOCATION+"/"+System.currentTimeMillis()+"_"+ PinyinUtil.getPinyin(fileName,"");
            File targetFile = new File(savedFileURI);
            FileOutputStream outStream  = new FileOutputStream(targetFile);
            int byteRead = 0;
            byte[] buffer = new byte[8192];
            while((byteRead = inputStream.read(buffer,0,8192)) != -1){
                outStream.write(buffer,0,byteRead);
            }
            outStream.close();
            inputStream.close();
            processFileSuccess = true;

            File unzippedFileFolder = ZipUtil.unzip(targetFile);

            File[] childFileList = unzippedFileFolder.listFiles();
            int containsShpFileNumber = 0;
            for(File currentFile : childFileList){
                String currentFileType = FileTypeUtil.getType(currentFile);
                if(currentFileType.equals("shp")||currentFileType.equals("SHP")){
                    containsShpFileNumber++;
                    currentSHPFile = currentFile;
                }
            }
            if(containsShpFileNumber != 1){
                CommonUIOperationUtil.showPopupNotification("已上传数据压缩文件 "+fileName+" 中必须包含且只能包含一个SHP格式文件", NotificationVariant.LUMO_ERROR);
                processFileSuccess = false;
                importWorkingFolder = null;
                FileUtil.del(unzippedFileFolder);
            }else{
                processFileSuccess = true;
                importWorkingFolder = unzippedFileFolder;
            }
            FileUtil.del(targetFile);
            return currentSHPFile;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void doImportSHPFile(){
        boolean removeExistingDataFlag = removeExistDataCheckbox.getValue();
        String charEncode = fileEncodeInput.getValue().equals("") ? "UTF-8" : fileEncodeInput.getValue();
        try {
            GeospatialOperationUtil.importSHPDataDirectlyToConceptionKind(conceptionKindName,removeExistingDataFlag,targetSHPFile,charEncode);
            ConceptionEntitiesCountRefreshEvent conceptionEntitiesCountRefreshEvent = new ConceptionEntitiesCountRefreshEvent();
            CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
            ConceptionKind targetConceptionKind = coreRealm.getConceptionKind(this.conceptionKindName);
            long conceptionEntitiesCount = targetConceptionKind.countConceptionEntities();
            conceptionEntitiesCountRefreshEvent.setConceptionEntitiesCount(conceptionEntitiesCount);
            conceptionEntitiesCountRefreshEvent.setConceptionKindName(this.conceptionKindName);
            ResourceHolder.getApplicationBlackboard().fire(conceptionEntitiesCountRefreshEvent);

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
            notificationMessageContainer.add(new Div(new Text("CSV数据文件: "+uploadedFileName)));
            notificationMessageContainer.add(new Div(new Text("当前概念实体总数: " + conceptionEntitiesCount)));
            notification.add(notificationMessageContainer);
            notification.open();

            if(containerDialog != null){
                containerDialog.close();
            }
        } catch (IOException e) {
            CommonUIOperationUtil.showPopupNotification("概念类型 "+conceptionKindName+" 导入数据实例操作发生服务器端错误",NotificationVariant.LUMO_ERROR);
            throw new RuntimeException(e);
        } catch (CoreRealmServiceRuntimeException e) {
            CommonUIOperationUtil.showPopupNotification("概念类型 "+conceptionKindName+" 导入数据实例操作发生服务器端错误",NotificationVariant.LUMO_ERROR);
            throw new RuntimeException(e);
        }
        if(importWorkingFolder != null){
            FileUtil.del(importWorkingFolder);
        }
    }

    private void cancelImportUploadedFile(){
        upload.clearFileList();
        displayUploadUI();
        confirmButton.setEnabled(false);
        cancelImportButton.setEnabled(false);
        if(importWorkingFolder != null){
            FileUtil.del(importWorkingFolder);
        }
    }

    private void displayUploadUI(){
        uploadSectionTitle.setVisible(true);
        controlOptionsLayout.setVisible(true);
        removeExistDataCheckbox.setVisible(true);
        upload.setVisible(true);
        uploadZipFileInfoSectionTitle.setVisible(false);
        uploadedFileInfoLayout.setVisible(false);
        scroller.setVisible(false);
    }

    private void displayAttributesMappingUI(){
        uploadSectionTitle.setVisible(false);
        controlOptionsLayout.setVisible(false);
        removeExistDataCheckbox.setVisible(false);
        upload.setVisible(false);
        uploadZipFileInfoSectionTitle.setVisible(true);
        uploadedFileInfoLayout.setVisible(true);
        scroller.setVisible(true);
    }

    public void setContainerDialog(Dialog containerDialog) {
        this.containerDialog = containerDialog;
    }
}
