package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionEntity.relation;

import com.vaadin.flow.component.html.Span;
import com.viewfunction.docg.element.commonComponent.chart.PieChart;

import java.util.Map;
import java.util.Set;

public class EntityAttachedRelationKindsCountChart extends Span {

    public EntityAttachedRelationKindsCountChart(Map<String, Long> attachedRelationKindCountInfo) {
        Set<String> relationKindsSet = attachedRelationKindCountInfo.keySet();

        String[] relationKindNameArray = new String[relationKindsSet.size()];
        Double[] relationEntityCountArray = new Double[relationKindsSet.size()];
        int idx = 0;
        for (String currentRelationKind : relationKindsSet) {
            relationKindNameArray[idx] = currentRelationKind;
            relationEntityCountArray[idx] = attachedRelationKindCountInfo.get(currentRelationKind).doubleValue();
            idx++;
        }

        PieChart pieChart = new PieChart(320,180);
        add(pieChart);
        pieChart.setDate(relationKindNameArray,relationEntityCountArray);
    }
}
