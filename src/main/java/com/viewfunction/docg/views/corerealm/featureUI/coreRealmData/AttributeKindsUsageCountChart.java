package com.viewfunction.docg.views.corerealm.featureUI.coreRealmData;

import com.github.appreciated.apexcharts.ApexChartsBuilder;
import com.github.appreciated.apexcharts.config.Stroke;
import com.github.appreciated.apexcharts.config.builder.ChartBuilder;
import com.github.appreciated.apexcharts.config.builder.DataLabelsBuilder;
import com.github.appreciated.apexcharts.config.builder.PlotOptionsBuilder;
import com.github.appreciated.apexcharts.config.builder.XAxisBuilder;
import com.github.appreciated.apexcharts.config.chart.Type;
import com.github.appreciated.apexcharts.config.plotoptions.builder.BarBuilder;
import com.github.appreciated.apexcharts.helper.Series;

public class AttributeKindsUsageCountChart extends ApexChartsBuilder {

    public AttributeKindsUsageCountChart(){

        Stroke stroke = new Stroke();
        stroke.setWidth(0.5);
        withChart(ChartBuilder.get()
                .withType(Type.bar)
                .build())
                .withLabels("Kind A", "Kind B", "Kind C", "Kind D", "Kind E", "Kind F","Kind G", "Kind H", "Kind I", "Kind J","OTHER")
                .withPlotOptions(PlotOptionsBuilder.get()
                        .withBar(BarBuilder.get()
                                .withHorizontal(false)
                                .build())
                        .build())
                .withDataLabels(DataLabelsBuilder.get()
                        .withEnabled(false)
                        .build())
                .withSeries(new Series<>(400.0, 430.0, 448.0, 470.0, 540.0, 580.0, 690.0, 1100.0, 1200.0, 1380.0,456.1))
                .withXaxis(XAxisBuilder.get()
                        .withCategories()
                        .build());
    }
}
