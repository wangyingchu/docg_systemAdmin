package com.viewfunction.docg.views.corerealm.featureUI.coreRealmData;

import com.github.appreciated.apexcharts.ApexChartsBuilder;
import com.github.appreciated.apexcharts.config.Stroke;
import com.github.appreciated.apexcharts.config.builder.ChartBuilder;
import com.github.appreciated.apexcharts.config.builder.LegendBuilder;
import com.github.appreciated.apexcharts.config.builder.ResponsiveBuilder;
import com.github.appreciated.apexcharts.config.chart.Type;
import com.github.appreciated.apexcharts.config.legend.Position;
import com.github.appreciated.apexcharts.config.responsive.builder.OptionsBuilder;

public class ConceptionEntityCountChart extends ApexChartsBuilder {

    public ConceptionEntityCountChart(){

        Stroke stroke = new Stroke();
        stroke.setWidth(0.5);
        withChart(ChartBuilder.get().withType(Type.pie).build())
                .withStroke(stroke)
                .withLabels("Team A", "Team B", "Team C", "Team D", "Team E", "Team F","Team G", "Team H", "Team I", "Team J")
                .withLegend(LegendBuilder.get()
                        .withPosition(Position.bottom)
                        .build())
                .withSeries(44.0, 55.0, 13.0, 43.0, 22.0,16.7,22.1,33.0,56.9,12.5)
                .withResponsive(ResponsiveBuilder.get()
                        .withBreakpoint(480.0)
                        .withOptions(OptionsBuilder.get()
                                .withLegend(LegendBuilder.get()
                                        .withPosition(Position.bottom)
                                        .build())
                                .build())
                        .build());
    }
}
