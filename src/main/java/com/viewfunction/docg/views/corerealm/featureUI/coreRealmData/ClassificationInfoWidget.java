package com.viewfunction.docg.views.corerealm.featureUI.coreRealmData;

import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.viewfunction.docg.element.commonComponent.chart.ChartGenerator;

public class ClassificationInfoWidget extends VerticalLayout {

    public ClassificationInfoWidget(){
        this.setWidth(100, Unit.PERCENTAGE);
        add(ChartGenerator.generateSOChartTreeChart());
    }
}
