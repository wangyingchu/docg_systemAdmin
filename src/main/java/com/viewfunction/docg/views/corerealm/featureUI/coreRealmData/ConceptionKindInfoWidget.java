package com.viewfunction.docg.views.corerealm.featureUI.coreRealmData;

import com.github.appreciated.apexcharts.ApexCharts;
import com.github.appreciated.apexcharts.config.*;
import com.github.appreciated.apexcharts.config.chart.Type;
import com.github.appreciated.apexcharts.config.chart.Zoom;
import com.github.appreciated.apexcharts.config.grid.Row;
import com.github.appreciated.apexcharts.config.stroke.Curve;
import com.github.appreciated.apexcharts.config.subtitle.Align;
import com.github.appreciated.apexcharts.helper.Series;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import com.viewfunction.docg.element.commonComponent.PrimaryKeyValueDisplayItem;
import com.viewfunction.docg.element.commonComponent.chart.ChartGenerator;

import java.util.Arrays;

public class ConceptionKindInfoWidget extends HorizontalLayout {

    public ConceptionKindInfoWidget(){
        this.setSpacing(false);
        this.setMargin(false);
        this.addClassNames("bg-base");

        VerticalLayout leftComponentContainer = new VerticalLayout();
        leftComponentContainer.setWidth(350,Unit.PIXELS);
        leftComponentContainer.setSpacing(false);
        leftComponentContainer.setMargin(false);
        add(leftComponentContainer);

        new PrimaryKeyValueDisplayItem(leftComponentContainer,"概念类型数量:","500");

        HorizontalLayout spaceDivLayout = new HorizontalLayout();
        spaceDivLayout.setHeight(15,Unit.PIXELS);
        leftComponentContainer.add(spaceDivLayout);

        new PrimaryKeyValueDisplayItem(leftComponentContainer,"概念实体数量:","1,000,000,000");

        VerticalLayout rightComponentContainer = new VerticalLayout();
        rightComponentContainer.setWidth(250,Unit.PIXELS);
        rightComponentContainer.setSpacing(false);
        rightComponentContainer.setMargin(false);
        add(rightComponentContainer);

        this.setFlexGrow(1,rightComponentContainer);
        rightComponentContainer.add(generateConceptionEntityCountChart());
    }

    private ApexCharts generateConceptionEntityCountChart(){
        // Our Apex chart
        ApexCharts apexChart = new ApexCharts();

        // Series
        Series<Integer> series = new Series<Integer>();
        series.setData(new Integer[] {10, 41, 35, 51, 49, 62, 69, 91, 148});
        series.setName("Desktops");

        // Chart
        Chart chart = new Chart();
        chart.setHeight("200");
        chart.setType(Type.histogram);
        Zoom zoom = new Zoom();
        zoom.setEnabled(false);
        chart.setZoom(zoom);

        // Labels
        DataLabels dataLabels = new DataLabels();
        dataLabels.setEnabled(true);

        // Stroke
        Stroke stroke = new Stroke();
        stroke.setCurve(Curve.straight);

        // Title
        //TitleSubtitle titleSubtilte = new TitleSubtitle();
        //titleSubtilte.setText("Product Trends by Month");
        //titleSubtilte.setAlign(Align.left);

        // Grid
        Grid grid = new Grid();
        Row row = new Row();
        row.setColors(Arrays.asList(new String[] {"#f3f3f3", "transparent"}));
        row.setOpacity(0.5);
        grid.setRow(row);

        // Xaxis
        XAxis xaxis = new XAxis();
        xaxis.setCategories(Arrays.asList(new String[] {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep"}));

        // Tooltip
        Tooltip tooltip = new Tooltip();
        tooltip.setEnabled(false);

        // Include them all
        apexChart.setSeries(series);
        apexChart.setChart(chart);
        apexChart.setDataLabels(dataLabels);
        apexChart.setStroke(stroke);
        //apexChart.setTitle(titleSubtilte);
        //apexChart.setGrid(grid);
        apexChart.setXaxis(xaxis);
        apexChart.setTooltip(tooltip);

        return apexChart;


    }
}
