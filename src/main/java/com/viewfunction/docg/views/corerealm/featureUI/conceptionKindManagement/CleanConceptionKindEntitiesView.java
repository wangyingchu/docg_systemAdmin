package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement;

import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class CleanConceptionKindEntitiesView extends VerticalLayout {

    private String conceptionKind;

    public CleanConceptionKindEntitiesView(String conceptionKind){
        this.conceptionKind = conceptionKind;

        H4 viewTitle = new H4("本操作将清除概念类型 "+conceptionKind+" 中的所有概念实体");

        add(viewTitle);
    }
}
