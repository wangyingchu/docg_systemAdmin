package com.viewfunction.docg.views.corerealm.featureUI.timeFlowManagement.maintainTimeFlow;

import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionEntity.ConceptionEntityMetaInfoView;

public class TimeFlowRelatedEntitySummaryInfoView extends VerticalLayout {
    private String entityKind;
    private String entityUID;

    public TimeFlowRelatedEntitySummaryInfoView(){
        this.setWidth(300, Unit.PIXELS);
        this.setHeight(320,Unit.PIXELS);

        this.setSpacing(false);
        this.setMargin(false);
        this.setPadding(false);
    }

    public void showEntitySummaryInfo(String entityKind,String entityUID){
        removeAll();
        this.entityKind = entityKind;
        this.entityUID = entityUID;
        ConceptionEntityMetaInfoView conceptionEntityMetaInfoView = new ConceptionEntityMetaInfoView(this.entityKind,this.entityUID);
        add(conceptionEntityMetaInfoView);
    }
}
