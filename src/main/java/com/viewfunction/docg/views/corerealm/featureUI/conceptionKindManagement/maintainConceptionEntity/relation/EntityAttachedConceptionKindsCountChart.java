package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionEntity.relation;

import com.github.appreciated.apexcharts.ApexChartsBuilder;
import com.github.appreciated.apexcharts.config.Stroke;
import com.github.appreciated.apexcharts.config.builder.ChartBuilder;
import com.github.appreciated.apexcharts.config.chart.Type;

import java.util.Map;
import java.util.Set;

public class EntityAttachedConceptionKindsCountChart extends ApexChartsBuilder {
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

        Stroke stroke = new Stroke();
        stroke.setWidth(0.5);
        withChart(ChartBuilder.get().withType(Type.pie).build())
                .withStroke(stroke)
                .withLabels(conceptionKindNameArray)
                //.withLegend(LegendBuilder.get().withFloating(true).withPosition(Position.right).withOffsetX(600.0).build())
                .withSeries(conceptionEntityCountArray)
                .build();
    }
}
