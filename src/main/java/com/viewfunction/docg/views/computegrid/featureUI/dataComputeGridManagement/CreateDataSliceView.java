package com.viewfunction.docg.views.computegrid.featureUI.dataComputeGridManagement;

import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class CreateDataSliceView extends VerticalLayout {
    private Dialog containerDialog;
    private H6 errorMessage;

    public CreateDataSliceView(){
        this.setWidthFull();
        HorizontalLayout messageContainerLayout = new HorizontalLayout();
        add(messageContainerLayout);

        H6 viewTitle = new H6("数据切片信息");
        messageContainerLayout.add(viewTitle);
    }

    public void setContainerDialog(Dialog containerDialog) {
        this.containerDialog = containerDialog;
    }
}
