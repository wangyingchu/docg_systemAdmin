package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionEntity.relation;

import com.github.appreciated.apexcharts.ApexChartsBuilder;
import com.github.appreciated.apexcharts.config.Stroke;
import com.github.appreciated.apexcharts.config.builder.ChartBuilder;
import com.github.appreciated.apexcharts.config.builder.LegendBuilder;
import com.github.appreciated.apexcharts.config.chart.Type;
import com.github.appreciated.apexcharts.config.legend.Position;

import java.util.Map;
import java.util.Set;

public class EntityAttachedRelationKindsCountChart extends ApexChartsBuilder {

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

        Stroke stroke = new Stroke();
        stroke.setWidth(0.5);
        withChart(ChartBuilder.get().withType(Type.pie).build())
                .withStroke(stroke)
                .withLabels(relationKindNameArray)
                .withLegend(LegendBuilder.get().withFloating(true).withPosition(Position.right).withOffsetX(800.0).build())
                .withSeries(relationEntityCountArray)
                .build();
    }
}
