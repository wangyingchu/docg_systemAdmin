package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionEntity;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class ConceptionEntityRelationInfoView extends VerticalLayout {
    private String conceptionKind;
    private String conceptionEntityUID;
    public ConceptionEntityRelationInfoView(String conceptionKind,String conceptionEntityUID){
        this.conceptionKind = conceptionKind;
        this.conceptionEntityUID = conceptionEntityUID;
    }
}
