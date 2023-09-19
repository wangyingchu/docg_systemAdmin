package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionEntity.relation;

import com.vaadin.flow.component.html.Span;
import com.viewfunction.docg.element.commonComponent.chart.PieChart;

import java.util.Map;
import java.util.Set;

public class EntityAttachedConceptionKindsCountChart extends Span {
    public EntityAttachedConceptionKindsCountChart(Map<Set<String>,Long> attachedConceptionKindsMap){
        Set<Set<String>> conceptionKindsNamesSet = attachedConceptionKindsMap.keySet();

        String[] conceptionKindNameArray = new String[conceptionKindsNamesSet.size()];
        Double[] conceptionEntityCountArray = new Double[conceptionKindsNamesSet.size()];
        int idx = 0;
        for (Set<String> currentRelationKind : conceptionKindsNamesSet) {
            conceptionKindNameArray[idx] = currentRelationKind.toString();
            conceptionEntityCountArray[idx] = attachedConceptionKindsMap.get(currentRelationKind).doubleValue();
            idx++;
        }

        PieChart pieChart = new PieChart(980,350);
        add(pieChart);

        String[] pieColorArray = new String[]{"#03a9f4","#76b852","#00d1b2","#ced7df","#ee4f4f","#0288d1","#ffc107","#d32f2f","#168eea","#323b43","#59626a"};
        pieChart.setColor(pieColorArray);
        pieChart.setCenter(25,40);
        pieChart.setRadius(70);
        pieChart.enableRightLegend();
        pieChart.setDate(conceptionKindNameArray,conceptionEntityCountArray);
    }
}
