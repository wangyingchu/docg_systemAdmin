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
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

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
    private String currentSavedCSVFile;

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
            String processedFileURI = processFile(inputStream, fileName);
            /*
            if(processFileSuccess){
                List<String> attributeList = getAttributesFromHeader(processedFileURI);
                if(attributeList == null || attributeList.size() == 0){
                    CommonUIOperationUtil.showPopupNotification("已上传文件 "+fileName+" 中不包含合法的概念实体属性信息", NotificationVariant.LUMO_ERROR);
                    File currentSaveCSVFile = new File(currentSavedCSVFile);
                    if(currentSaveCSVFile.exists()){
                        currentSaveCSVFile.delete();
                    }
                }else{
                    fileNameLabel.setText(fileName);
                    CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
                    ConceptionKind targetConceptionKind = coreRealm.getConceptionKind(this.conceptionKindName);
                    List<KindEntityAttributeRuntimeStatistics> kindEntityAttributeRuntimeStatisticsList = targetConceptionKind.statisticEntityAttributesDistribution(10000);
                    List<String> kindExistingStringFormatAttributesList = new ArrayList<>();
                    if(kindEntityAttributeRuntimeStatisticsList != null){
                        for(KindEntityAttributeRuntimeStatistics currentKindEntityAttributeRuntimeStatistics:kindEntityAttributeRuntimeStatisticsList){
                            currentKindEntityAttributeRuntimeStatistics.getAttributeName();
                            if(currentKindEntityAttributeRuntimeStatistics.getAttributeDataType().equals(AttributeDataType.STRING)){
                                kindExistingStringFormatAttributesList.add(currentKindEntityAttributeRuntimeStatistics.getAttributeName());
                            }
                        }
                    }
                    if(entityAttributeNamesMappingView == null){
                        entityAttributeNamesMappingView = new EntityAttributeNamesMappingView(attributeList,kindExistingStringFormatAttributesList);
                        attributeMappingLayout.add(entityAttributeNamesMappingView);
                    }else{
                        entityAttributeNamesMappingView.refreshEntityAttributeNamesMappingInfo(attributeList,kindExistingStringFormatAttributesList);
                    }

                    displayAttributesMappingUI();
                    confirmButton.setEnabled(true);
                    cancelImportButton.setEnabled(true);
                }
            }
            */

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

    private String processFile(InputStream inputStream,String fileName){
        try {
            File fileFolder = new File(TEMP_FILES_STORAGE_LOCATION);
            if(!fileFolder.exists()){
                fileFolder.mkdirs();
            }

            String savedFileURI = TEMP_FILES_STORAGE_LOCATION+"/"+System.currentTimeMillis()+"_"+ PinyinUtil.getPinyin(fileName,"");
            File targetFile = new File(savedFileURI);
            currentSavedCSVFile = savedFileURI;
            FileOutputStream outStream  = new FileOutputStream(targetFile);
            int byteRead = 0;
            byte[] buffer = new byte[8192];
            while((byteRead = inputStream.read(buffer,0,8192)) != -1){
                outStream.write(buffer,0,byteRead);
            }
            outStream.close();
            inputStream.close();
            processFileSuccess = true;
            /*
            if(isZipFile){
                File unzippedFileFolder = ZipUtil.unzip(targetFile);
                if(unzippedFileFolder.list().length != 1){
                    processFileSuccess = false;
                    CommonUIOperationUtil.showPopupNotification("已上传 ZIP 文件 "+fileName+" 中只允许包含一个CSV格式的数据文件", NotificationVariant.LUMO_ERROR);
                }else{
                    File targetCSVFile = unzippedFileFolder.listFiles()[0];
                    String fileType = FileTypeUtil.getType(targetCSVFile);
                    if(fileType.equals("csv")){
                        String formattedCSVFileName = TEMP_FILES_STORAGE_LOCATION+"/"+System.currentTimeMillis()+"_"+ PinyinUtil.getPinyin(targetCSVFile.getName(),"");
                        targetCSVFile.renameTo(new File(formattedCSVFileName));
                        savedFileURI = formattedCSVFileName;
                        currentSavedCSVFile = savedFileURI;
                        processFileSuccess = true;
                    }else{
                        processFileSuccess = false;
                        CommonUIOperationUtil.showPopupNotification("已上传数据文件 "+targetCSVFile.getName()+" 必须是CSV格式文件", NotificationVariant.LUMO_ERROR);
                    }
                }
                FileUtil.del(unzippedFileFolder);
                FileUtil.del(targetFile);
            }
            */
            return savedFileURI;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setContainerDialog(Dialog containerDialog) {
        this.containerDialog = containerDialog;
    }
}
