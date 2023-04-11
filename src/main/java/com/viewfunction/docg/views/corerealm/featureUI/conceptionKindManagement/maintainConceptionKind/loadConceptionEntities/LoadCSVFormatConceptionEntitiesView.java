package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionKind.loadConceptionEntities;

import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;

import com.viewfunction.docg.element.commonComponent.SecondaryIconTitle;
import com.viewfunction.docg.util.config.SystemAdminCfgPropertiesHandler;

import java.io.*;

public class LoadCSVFormatConceptionEntitiesView extends VerticalLayout {

    private String conceptionKindName;
    private String TEMP_FILES_STORAGE_LOCATION =
            SystemAdminCfgPropertiesHandler.getPropertyValue(SystemAdminCfgPropertiesHandler.TEMP_FILES_STORAGE_LOCATION);

    public LoadCSVFormatConceptionEntitiesView(String conceptionKindName){
        this.setWidth(100,Unit.PERCENTAGE);
        this.conceptionKindName = conceptionKindName;

        SecondaryIconTitle iconTitle1 = new SecondaryIconTitle(new Icon(VaadinIcon.FILE_O),"上传 CSV 格式文件");
        iconTitle1.getStyle().set("padding-top", "var(--lumo-space-s)");
        add(iconTitle1);

        MemoryBuffer buffer = new MemoryBuffer();
        Upload upload = new Upload(buffer);
        upload.setWidth(100, Unit.PERCENTAGE);
        upload.setHeight(150,Unit.PIXELS);
        upload.setAcceptedFileTypes(".csv");

        upload.setMaxFileSize(100);

        upload.addSucceededListener(event -> {
            String fileName = event.getFileName();
            InputStream inputStream = buffer.getInputStream();
            processFile(inputStream, fileName);
        });




        add(upload);
    }

    private void processFile(InputStream inputStream,String fileName){
        try {
            File targetFile = new File(TEMP_FILES_STORAGE_LOCATION+"/"+fileName);
            FileOutputStream outStream  = new FileOutputStream(targetFile);
            int byteRead = 0;
            byte[] buffer = new byte[8192];
            while((byteRead = inputStream.read(buffer,0,8192)) != -1){
                outStream.write(buffer,0,byteRead);
            }
            outStream.close();
            inputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
