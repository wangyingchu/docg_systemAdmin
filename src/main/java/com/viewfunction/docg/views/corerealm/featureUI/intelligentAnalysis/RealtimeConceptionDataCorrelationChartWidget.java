package com.viewfunction.docg.views.corerealm.featureUI.intelligentAnalysis;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.ConceptionKindCorrelationInfo;
import com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.ConceptionKindsCorrelationInfoSummaryChart;

import java.util.Set;

public class RealtimeConceptionDataCorrelationChartWidget extends VerticalLayout {

    private ConceptionKindsCorrelationInfoSummaryChart conceptionKindsCorrelationInfoSummaryChart;

    public RealtimeConceptionDataCorrelationChartWidget(int widgetWidth,int widgetHeight) {
        conceptionKindsCorrelationInfoSummaryChart =
                new ConceptionKindsCorrelationInfoSummaryChart(widgetWidth,widgetHeight);
        conceptionKindsCorrelationInfoSummaryChart.hideLegend();
        add(conceptionKindsCorrelationInfoSummaryChart);
    }

    public void renderConceptionDataCorrelationRealtimeInfo(Set<ConceptionKindCorrelationInfo> conceptionKindCorrelationInfoSet){
        this.conceptionKindsCorrelationInfoSummaryChart.setData(conceptionKindCorrelationInfoSet);
    }
}
