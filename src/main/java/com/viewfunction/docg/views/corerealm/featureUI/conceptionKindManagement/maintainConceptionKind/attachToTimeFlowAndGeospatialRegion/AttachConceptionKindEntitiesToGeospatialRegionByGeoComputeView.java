package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionKind.attachToTimeFlowAndGeospatialRegion;

import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class AttachConceptionKindEntitiesToGeospatialRegionByGeoComputeView extends VerticalLayout {
    private String conceptionKindName;
    private Dialog containerDialog;

    public AttachConceptionKindEntitiesToGeospatialRegionByGeoComputeView(String conceptionKindName){
        this.conceptionKindName = conceptionKindName;
        this.setWidthFull();
    }


    public void setContainerDialog(Dialog containerDialog) {
        this.containerDialog = containerDialog;
    }
}
