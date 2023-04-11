package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionKind.loadConceptionEntities;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;

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
            entityAttributeNamesMappingView = new EntityAttributeNamesMappingView(attributeList);
            attributeMappingLayout.add(entityAttributeNamesMappingView);

            parseCSVFile(processedFileURI);
        });

        upload.addFailedListener(event ->{
            System.out.println(event.getFileName());
            System.out.println(event.getReason());
            System.out.println(event.getMIMEType());
        });
        upload.addFileRejectedListener(event ->{
            System.out.println(event.getErrorMessage());
        });
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

    private void parseCSVFile(String fileURI){
        confirmButton.setEnabled(true);
    }

    public List<String> getAttributesFromHeader(String csvLocation){
        if(csvLocation == null){
            return null;
        }else{
            try {
                BufferedReader reader = new BufferedReader(new FileReader(csvLocation));
                String header = reader.readLine();
                List<String> attributesList = new ArrayList<>();
                String[] attributesArray = header.split(",");
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
