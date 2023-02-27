package com.viewfunction.docg.views.corerealm.featureUI.relationKindManagement;

import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class RelationKindCorrelationInfoChart extends VerticalLayout {

    public RelationKindCorrelationInfoChart(int chartHeight){
        this.setWidthFull();
        this.setSpacing(false);
        this.setMargin(false);
        this.setPadding(false);
        this.setHeight(chartHeight, Unit.PIXELS);
    }

    public void clearData(){}
}
