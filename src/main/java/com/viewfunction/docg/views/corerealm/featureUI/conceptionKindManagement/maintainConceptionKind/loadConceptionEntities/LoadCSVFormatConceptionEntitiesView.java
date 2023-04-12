package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionKind.loadConceptionEntities;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;

import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.KindEntityAttributeRuntimeStatistics;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.AttributeDataType;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.ConceptionKind;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.SecondaryIconTitle;
import com.viewfunction.docg.util.config.SystemAdminCfgPropertiesHandler;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class LoadCSVFormatConceptionEntitiesView extends VerticalLayout {

    private String conceptionKindName;
    private String TEMP_FILES_STORAGE_LOCATION =
            SystemAdminCfgPropertiesHandler.getPropertyValue(SystemAdminCfgPropertiesHandler.TEMP_FILES_STORAGE_LOCATION);
    private Upload upload;
    private Button confirmButton;
    private int maxSizeOfFileInMBForUpload = 0;
    private EntityAttributeNamesMappingView entityAttributeNamesMappingView;
    private VerticalLayout attributeMappingLayout;
    private String tabSplitSequence = "\t";
    private String spaceSplitSequence = " ";
    private String commaSplitSequence = ",";
    private RadioButtonGroup<String> splitCharGroup;
    private Checkbox useZipCheckbox;

    public LoadCSVFormatConceptionEntitiesView(String conceptionKindName,int viewWidth){
        this.setWidth(100,Unit.PERCENTAGE);
        this.conceptionKindName = conceptionKindName;

        SecondaryIconTitle iconTitle1 = new SecondaryIconTitle(new Icon(VaadinIcon.FILE_O),"上传 CSV 格式文件");
        iconTitle1.setWidth(100,Unit.PERCENTAGE);
        iconTitle1.getStyle().set("padding-top", "var(--lumo-space-s)");
        iconTitle1.getStyle()
                .set("border-bottom", "1px solid var(--lumo-contrast-10pct)")
                .set("padding-bottom", "var(--lumo-space-s)");
        add(iconTitle1);

        HorizontalLayout controlOptionsLayout = new HorizontalLayout();
        controlOptionsLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        add(controlOptionsLayout);

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
        add(useZipCheckbox);

        MemoryBuffer buffer = new MemoryBuffer();
        upload = new Upload(buffer);
        upload.setWidth(100, Unit.PERCENTAGE);
        upload.setHeight(150,Unit.PIXELS);
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
            InputStream inputStream = buffer.getInputStream();
            String processedFileURI = processFile(inputStream, fileName);
            List<String> attributeList = getAttributesFromHeader(processedFileURI);

            if(attributeList == null || attributeList.size() == 0){

            }else{
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
                entityAttributeNamesMappingView = new EntityAttributeNamesMappingView(attributeList,kindExistingStringFormatAttributesList);
                attributeMappingLayout.add(entityAttributeNamesMappingView);

                confirmButton.setEnabled(true);
            }
        });

        upload.addFailedListener(event ->{
            System.out.println(event.getFileName());
            System.out.println(event.getReason());
            System.out.println(event.getMIMEType());
        });
        upload.addFileRejectedListener(event ->{
            System.out.println(event.getErrorMessage());
        });
        upload.addFinishedListener(event ->{

        });
        upload.addStartedListener(event ->{});
        add(upload);

        SecondaryIconTitle iconTitle2 = new SecondaryIconTitle(new Icon(VaadinIcon.FLIP_H),"概念实体属性映射");
        iconTitle2.setWidth(100,Unit.PERCENTAGE);
        iconTitle2.getStyle().set("padding-top", "var(--lumo-space-s)");
        iconTitle2.getStyle()
                .set("border-bottom", "1px solid var(--lumo-contrast-10pct)")
                .set("padding-bottom", "var(--lumo-space-s)");
        add(iconTitle2);

        attributeMappingLayout = new VerticalLayout();
        attributeMappingLayout.setWidth(viewWidth - 10,Unit.PIXELS);
        attributeMappingLayout.setPadding(false);
        attributeMappingLayout.setMargin(false);
        attributeMappingLayout.setSpacing(false);

        Scroller scroller = new Scroller(attributeMappingLayout);
        scroller.setHeight(300,Unit.PIXELS);
        add(scroller);

        HorizontalLayout spaceDivLayout2 = new HorizontalLayout();
        spaceDivLayout2.setWidthFull();
        spaceDivLayout2.getStyle().set("border-bottom", "1px solid var(--lumo-contrast-20pct)");
        add(spaceDivLayout2);

        confirmButton = new Button("确认导入概念类型实体数据",new Icon(VaadinIcon.CHECK_CIRCLE));
        confirmButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        add(confirmButton);
        confirmButton.setEnabled(false);
        setHorizontalComponentAlignment(Alignment.END,confirmButton);
        confirmButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                //doAddConceptionEntity();
            }
        });
    }

    private String processFile(InputStream inputStream,String fileName){
        try {
            File fileFolder = new File(TEMP_FILES_STORAGE_LOCATION);
            if(!fileFolder.exists()){
                fileFolder.mkdirs();
            }

            String savedFileURI = TEMP_FILES_STORAGE_LOCATION+"/"+System.currentTimeMillis()+"_"+fileName;
            File targetFile = new File(savedFileURI);
            FileOutputStream outStream  = new FileOutputStream(targetFile);
            int byteRead = 0;
            byte[] buffer = new byte[8192];
            while((byteRead = inputStream.read(buffer,0,8192)) != -1){
                outStream.write(buffer,0,byteRead);
            }
            outStream.close();
            inputStream.close();
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
}
