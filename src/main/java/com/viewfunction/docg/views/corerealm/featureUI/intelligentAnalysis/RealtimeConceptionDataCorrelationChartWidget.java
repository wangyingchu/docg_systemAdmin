package com.viewfunction.docg.views.corerealm.featureUI.intelligentAnalysis;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.ConceptionKindCorrelationInfo;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.RealmConstant;
import com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.ConceptionKindsCorrelationInfoSummaryChart;

import java.util.HashSet;
import java.util.Set;

public class RealtimeConceptionDataCorrelationChartWidget extends VerticalLayout {

    private ConceptionKindsCorrelationInfoSummaryChart conceptionKindsCorrelationInfoSummaryChart;

    public RealtimeConceptionDataCorrelationChartWidget(int widgetWidth,int widgetHeight) {
        conceptionKindsCorrelationInfoSummaryChart =
                new ConceptionKindsCorrelationInfoSummaryChart(widgetWidth,widgetHeight);
        conceptionKindsCorrelationInfoSummaryChart.hideLegend();
        conceptionKindsCorrelationInfoSummaryChart.enableDynamicRenderingEdgeWidth();
        add(conceptionKindsCorrelationInfoSummaryChart);
    }

    public void renderConceptionDataCorrelationRealtimeInfo(Set<ConceptionKindCorrelationInfo> conceptionKindCorrelationInfoSet){
        Set<ConceptionKindCorrelationInfo> filteredSet = new HashSet<>();
        for(ConceptionKindCorrelationInfo currentConceptionKindCorrelationInfo : conceptionKindCorrelationInfoSet){
            if(currentConceptionKindCorrelationInfo.getSourceConceptionKindName().startsWith(RealmConstant.RealmInnerTypePerFix) &
            currentConceptionKindCorrelationInfo.getTargetConceptionKindName().startsWith(RealmConstant.RealmInnerTypePerFix)
            ){
            }else{
                filteredSet.add(currentConceptionKindCorrelationInfo);
            }
        }
        this.conceptionKindsCorrelationInfoSummaryChart.setData(filteredSet);
    }
}
