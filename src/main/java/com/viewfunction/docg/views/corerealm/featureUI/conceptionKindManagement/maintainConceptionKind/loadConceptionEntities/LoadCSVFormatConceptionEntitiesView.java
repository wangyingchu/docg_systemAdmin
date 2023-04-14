package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionKind.loadConceptionEntities;

import cn.hutool.core.io.FileTypeUtil;
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
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;

import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.viewfunction.docg.coreRealm.realmServiceCore.exception.CoreRealmServiceRuntimeException;
import com.viewfunction.docg.coreRealm.realmServiceCore.internal.neo4j.util.BatchDataOperationUtil;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.KindEntityAttributeRuntimeStatistics;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.AttributeDataType;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.ConceptionKind;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.CoreRealmStorageImplTech;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.SecondaryIconTitle;
import com.viewfunction.docg.element.eventHandling.ConceptionEntitiesCountRefreshEvent;
import com.viewfunction.docg.element.userInterfaceUtil.CommonUIOperationUtil;
import com.viewfunction.docg.util.ResourceHolder;
import com.viewfunction.docg.util.config.SystemAdminCfgPropertiesHandler;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class LoadCSVFormatConceptionEntitiesView extends VerticalLayout {

    private String conceptionKindName;
    private String TEMP_FILES_STORAGE_LOCATION =
            SystemAdminCfgPropertiesHandler.getPropertyValue(SystemAdminCfgPropertiesHandler.TEMP_FILES_STORAGE_LOCATION);
    private Upload upload;
    private Button confirmButton;
    private Button cancelImportButton;
    private int maxSizeOfFileInMBForUpload = 0;
    private EntityAttributeNamesMappingView entityAttributeNamesMappingView;
    private VerticalLayout attributeMappingLayout;
    private String tabSplitSequence = "\t";
    private String spaceSplitSequence = " ";
    private String commaSplitSequence = ",";
    private RadioButtonGroup<String> splitCharGroup;
    private Checkbox useZipCheckbox;
    private SecondaryIconTitle uploadSectionTitle;
    private HorizontalLayout controlOptionsLayout;
    private SecondaryIconTitle attributesMappingSectionTitle;
    private Scroller scroller;
    private String currentSavedCSVFile;
    private HorizontalLayout uploadedFileInfoLayout;
    private Label fileNameLabel;
    private Dialog containerDialog;
    private String uploadedFileName;
    private ProgressBar importProgressBar;

    public LoadCSVFormatConceptionEntitiesView(String conceptionKindName,int viewWidth){
        this.setWidth(100,Unit.PERCENTAGE);
        this.conceptionKindName = conceptionKindName;

        VerticalLayout operationAreaLayout = new VerticalLayout();
        operationAreaLayout.setWidth(100,Unit.PERCENTAGE);
        operationAreaLayout.setHeight(400,Unit.PIXELS);
        operationAreaLayout.setSpacing(false);
        operationAreaLayout.setPadding(false);
        operationAreaLayout.setMargin(false);
        add(operationAreaLayout);

        uploadSectionTitle = new SecondaryIconTitle(new Icon(VaadinIcon.FILE_O),"上传 CSV 格式文件");
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

        splitCharGroup = new RadioButtonGroup<>();
        splitCharGroup.setItems("逗号[ , ]", "制表符[Tab]", "空格[ _ ]");
        splitCharGroup.setValue("逗号[ , ]");
        splitCharGroup.setRenderer(new ComponentRenderer<>(option -> {
            Label optionLabel = new Label(option);
            optionLabel.addClassNames("text-xs","text-secondary");
            return optionLabel;
        }));
        controlOptionsLayout.add(splitCharGroup);

        useZipCheckbox = new Checkbox();
        useZipCheckbox.setLabel(".zip 格式压缩文件");
        useZipCheckbox.addClassNames("text-xs","text-secondary");
        useZipCheckbox.getStyle().set("padding-bottom", "5px");
        useZipCheckbox.addValueChangeListener(new HasValue.ValueChangeListener<AbstractField.ComponentValueChangeEvent<Checkbox, Boolean>>() {
            @Override
            public void valueChanged(AbstractField.ComponentValueChangeEvent<Checkbox, Boolean> checkboxBooleanComponentValueChangeEvent) {
                boolean useZipFileFlag = checkboxBooleanComponentValueChangeEvent.getValue();
                if(useZipFileFlag){
                    upload.setAcceptedFileTypes("application/zip",".zip");
                }else{
                    upload.setAcceptedFileTypes("text/csv",".csv");
                }
            }
        });
        operationAreaLayout.add(useZipCheckbox);

        MemoryBuffer buffer = new MemoryBuffer();
        upload = new Upload(buffer);
        upload.setWidth(100, Unit.PERCENTAGE);
        upload.setHeight(350,Unit.PIXELS);
        upload.setAutoUpload(false);

        //MIME types
        //https://developer.mozilla.org/en-US/docs/Web/HTTP/Basics_of_HTTP/MIME_types/Common_types
        upload.setAcceptedFileTypes("text/csv",".csv");

        maxSizeOfFileInMBForUpload = Integer.valueOf(
                SystemAdminCfgPropertiesHandler.getPropertyValue(SystemAdminCfgPropertiesHandler.MAX_SIZE_OF_FILE_IN_MB_FOR_UPLOAD));
        int maxFileSizeInBytes = maxSizeOfFileInMBForUpload * 1024 * 1024; // 10MB
        upload.setMaxFileSize(maxFileSizeInBytes);

        Button uploadButton = new Button("上传 CSV 文件 ...");
        uploadButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        upload.setUploadButton(uploadButton);
        Span dropLabel = new Span("请将 CSV 文件拖放到此处");
        dropLabel.addClassNames("text-xs","text-secondary");
        upload.setDropLabel(dropLabel);

        upload.addSucceededListener(event -> {
            String fileName = event.getFileName();
            boolean isZipFile = event.getMIMEType().equals("application/zip") ? true:false;
            uploadedFileName = fileName;
            InputStream inputStream = buffer.getInputStream();
            String processedFileURI = processFile(inputStream, fileName,isZipFile);
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
        });

        upload.addFailedListener(event ->{});
        upload.addFileRejectedListener(event ->{});
        upload.addFinishedListener(event ->{});
        upload.addStartedListener(event ->{});
        operationAreaLayout.add(upload);

        attributesMappingSectionTitle = new SecondaryIconTitle(new Icon(VaadinIcon.FLIP_H),"概念实体属性映射");
        attributesMappingSectionTitle.setWidth(100,Unit.PERCENTAGE);
        attributesMappingSectionTitle.getStyle().set("padding-top", "var(--lumo-space-s)");
        attributesMappingSectionTitle.getStyle()
                .set("border-bottom", "1px solid var(--lumo-contrast-10pct)")
                .set("padding-bottom", "var(--lumo-space-s)");
        operationAreaLayout.add(attributesMappingSectionTitle);

        uploadedFileInfoLayout = new HorizontalLayout();
        uploadedFileInfoLayout.getStyle().set("padding-top", "10px").set("padding-bottom", "5px");
        uploadedFileInfoLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        operationAreaLayout.add(uploadedFileInfoLayout);
        Icon fileIcon = VaadinIcon.FILE.create();
        fileIcon.setSize("8px");
        fileNameLabel = new Label("");
        fileNameLabel.addClassNames("text-xs","text-secondary");
        uploadedFileInfoLayout.add(fileIcon,fileNameLabel);

        attributeMappingLayout = new VerticalLayout();
        attributeMappingLayout.setWidth(viewWidth - 10,Unit.PIXELS);
        attributeMappingLayout.setPadding(false);
        attributeMappingLayout.setMargin(false);
        attributeMappingLayout.setSpacing(false);

        scroller = new Scroller(attributeMappingLayout);
        scroller.setHeight(500,Unit.PIXELS);
        operationAreaLayout.add(scroller);

        HorizontalLayout spaceDivLayout2 = new HorizontalLayout();
        spaceDivLayout2.setWidthFull();
        spaceDivLayout2.getStyle().set("border-bottom", "1px solid var(--lumo-contrast-20pct)");
        add(spaceDivLayout2);

        importProgressBar = new ProgressBar();
        importProgressBar.setIndeterminate(true);
        importProgressBar.setVisible(false);
        add(importProgressBar);

        HorizontalLayout buttonbarLayout = new HorizontalLayout();
        add(buttonbarLayout);
        setHorizontalComponentAlignment(Alignment.END,buttonbarLayout);

        confirmButton = new Button("确认导入概念类型实体数据",new Icon(VaadinIcon.CHECK_CIRCLE));
        confirmButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        confirmButton.setEnabled(false);
        confirmButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                doImportCSVFile();
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

    private String processFile(InputStream inputStream,String fileName,boolean isZipFile){
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

            if(isZipFile){
                File unzippedFileFolder = ZipUtil.unzip(targetFile);
                if(unzippedFileFolder.list().length != 1){
                    CommonUIOperationUtil.showPopupNotification("已上传 ZIP 文件 "+fileName+" 中只允许包含一个CSV格式的数据文件", NotificationVariant.LUMO_ERROR);
                }else{
                    File targetCSVFile = unzippedFileFolder.listFiles()[0];
                    String fileType = FileTypeUtil.getType(targetCSVFile);
                    if(fileType.equals("csv")){
                        String formattedCSVFileName = TEMP_FILES_STORAGE_LOCATION+"/"+System.currentTimeMillis()+"_"+ PinyinUtil.getPinyin(targetCSVFile.getName(),"");
                        targetCSVFile.renameTo(new File(formattedCSVFileName));
                        savedFileURI = formattedCSVFileName;
                        currentSavedCSVFile = savedFileURI;
                    }else{
                        CommonUIOperationUtil.showPopupNotification("已上传数据文件 "+targetCSVFile.getName()+" 必须是CSV格式文件", NotificationVariant.LUMO_ERROR);
                    }
                }
                unzippedFileFolder.delete();
                targetFile.delete();
            }
            return savedFileURI;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> getAttributesFromHeader(String csvLocation){
        if(csvLocation == null){
            return null;
        }else{
            try {
                String splitChar = commaSplitSequence;
                String lineSplitCharOption = splitCharGroup.getValue();
                if(lineSplitCharOption.equals("逗号[ , ]")){
                    splitChar = commaSplitSequence;
                }
                if(lineSplitCharOption.equals("制表符[Tab]")){
                    splitChar = tabSplitSequence;
                }
                if(lineSplitCharOption.equals("空格[ _ ]")){
                    splitChar = spaceSplitSequence;
                }
                BufferedReader reader = new BufferedReader(new FileReader(csvLocation));
                String header = reader.readLine();
                List<String> attributesList = new ArrayList<>();
                String[] attributesArray = header.split(splitChar);
                for(String currentStr : attributesArray){
                    String formattedStr = currentStr.replaceAll("\"","");
                    attributesList.add(formattedStr);
                }
                return attributesList;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private void displayUploadUI(){
        uploadSectionTitle.setVisible(true);
        controlOptionsLayout.setVisible(true);
        useZipCheckbox.setVisible(true);
        upload.setVisible(true);
        attributesMappingSectionTitle.setVisible(false);
        uploadedFileInfoLayout.setVisible(false);
        scroller.setVisible(false);
    }

    private void displayAttributesMappingUI(){
        uploadSectionTitle.setVisible(false);
        controlOptionsLayout.setVisible(false);
        useZipCheckbox.setVisible(false);
        upload.setVisible(false);
        attributesMappingSectionTitle.setVisible(true);
        uploadedFileInfoLayout.setVisible(true);
        scroller.setVisible(true);
    }

    private void cancelImportUploadedFile(){
        if(currentSavedCSVFile != null){
            File currentSaveCSVFile = new File(currentSavedCSVFile);
            if(currentSaveCSVFile.exists()){
                currentSaveCSVFile.delete();
            }
        }
        upload.clearFileList();
        displayUploadUI();
        cancelImportButton.setEnabled(false);
        confirmButton.setEnabled(false);
    }

    private void doImportCSVFile(){
        if(currentSavedCSVFile != null){
            Map<String,String> attributeMap = entityAttributeNamesMappingView.getAttributesMapping();
            Set<String> orgAttributeNames = attributeMap.keySet();
            List<String> existingMappedValue = new ArrayList<>();
            boolean attributeMappingCheckResult = true;
            for(String currentOrgAttributeName : orgAttributeNames){
                String currentMappedNewName = attributeMap.get(currentOrgAttributeName);
                if(existingMappedValue.contains(currentMappedNewName)){
                    CommonUIOperationUtil.showPopupNotification("属性 "+currentOrgAttributeName+" 映射的新名称 "+currentMappedNewName +"已经被使用", NotificationVariant.LUMO_ERROR);
                    attributeMappingCheckResult = false;
                }else{
                    existingMappedValue.add(currentMappedNewName);
                }
            }
            if(attributeMappingCheckResult){
                CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
                if(coreRealm.getStorageImplTech().equals(CoreRealmStorageImplTech.NEO4J)){
                    File currentSaveCSVFile = new File(currentSavedCSVFile);
                    String filePath = currentSaveCSVFile.getAbsolutePath();
                    String splitChar = commaSplitSequence;
                    String lineSplitCharOption = splitCharGroup.getValue();
                    if(lineSplitCharOption.equals("逗号[ , ]")){
                        splitChar = commaSplitSequence;
                    }
                    if(lineSplitCharOption.equals("制表符[Tab]")){
                        splitChar = tabSplitSequence;
                    }
                    if(lineSplitCharOption.equals("空格[ _ ]")){
                        splitChar = spaceSplitSequence;
                    }
                    importProgressBar.setVisible(true);
                    confirmButton.setEnabled(false);
                    cancelImportButton.setEnabled(false);

                    boolean importResult = BatchDataOperationUtil.importConceptionEntitiesFromCSV(filePath,this.conceptionKindName,attributeMap,splitChar);
                    if(importResult){
                        long conceptionEntitiesCount = 0 ;
                        ConceptionKind targetConceptionKind = coreRealm.getConceptionKind(this.conceptionKindName);
                        try {
                            conceptionEntitiesCount = targetConceptionKind.countConceptionEntities();
                            ConceptionEntitiesCountRefreshEvent conceptionEntitiesCountRefreshEvent = new ConceptionEntitiesCountRefreshEvent();
                            conceptionEntitiesCountRefreshEvent.setConceptionEntitiesCount(conceptionEntitiesCount);
                            conceptionEntitiesCountRefreshEvent.setConceptionKindName(this.conceptionKindName);
                            ResourceHolder.getApplicationBlackboard().fire(conceptionEntitiesCountRefreshEvent);
                        } catch (CoreRealmServiceRuntimeException e) {
                            throw new RuntimeException(e);
                        }
                        if(currentSaveCSVFile.exists()){
                            currentSaveCSVFile.delete();
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
                        notificationMessageContainer.add(new Div(new Text("CSV数据文件: "+uploadedFileName)));
                        notificationMessageContainer.add(new Div(new Text("当前概念实体总数: " + conceptionEntitiesCount)));
                        notification.add(notificationMessageContainer);
                        notification.open();
                    }else{
                        CommonUIOperationUtil.showPopupNotification("概念类型 "+conceptionKindName+" 导入数据实例操作发生服务器端错误",NotificationVariant.LUMO_ERROR);
                    }
                    confirmButton.setEnabled(true);
                    cancelImportButton.setEnabled(true);
                    importProgressBar.setVisible(false);
                }
            }
        }
    }

    public void setContainerDialog(Dialog containerDialog) {
        this.containerDialog = containerDialog;
    }
}
