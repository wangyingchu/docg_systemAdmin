package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionKind.loadConceptionEntities;

import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;

import com.viewfunction.docg.element.commonComponent.SecondaryIconTitle;

import java.io.InputStream;

public class LoadCSVFormatConceptionEntitiesView extends VerticalLayout {

    private String conceptionKindName;

    public LoadCSVFormatConceptionEntitiesView(String conceptionKindName){
        this.setWidth(100,Unit.PERCENTAGE);
        this.conceptionKindName = conceptionKindName;

        SecondaryIconTitle iconTitle1 = new SecondaryIconTitle(new Icon(VaadinIcon.FILE_O),"上传 CSV 格式文件");
        iconTitle1.getStyle().set("padding-top", "var(--lumo-space-s)");
        add(iconTitle1);

        MemoryBuffer buffer = new MemoryBuffer();
        Upload upload = new Upload(buffer);
        upload.setWidth(100, Unit.PERCENTAGE);
        upload.setHeight(100,Unit.PIXELS);
        upload.addSucceededListener(event -> {
            String fileName = event.getFileName();
            InputStream inputStream = buffer.getInputStream();

            // Do something with the file data
            // processFile(inputStream, fileName);
        });

        add(upload);
    }
}
