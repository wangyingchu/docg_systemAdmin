package com.viewfunction.docg.views.corerealm.featureUI.coreRealmData;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.viewfunction.docg.element.commonComponent.chart.CartesianHeatmapChart;

public class RelationAndConceptionKindAttachInfoWidget extends VerticalLayout {

    public RelationAndConceptionKindAttachInfoWidget(){
        //this.getStyle().set("background-color","#EFEFEF");
        CartesianHeatmapChart cartesianHeatmapChart1 = new CartesianHeatmapChart();
        add(cartesianHeatmapChart1);

        CartesianHeatmapChart cartesianHeatmapChart2 = new CartesianHeatmapChart();
        add(cartesianHeatmapChart2);
    }

    public void refreshRelationAndConceptionKindAttachInfo(){}
}
