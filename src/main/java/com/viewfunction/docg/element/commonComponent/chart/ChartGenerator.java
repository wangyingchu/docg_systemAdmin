package com.viewfunction.docg.element.commonComponent.chart;

import com.github.appreciated.apexcharts.ApexCharts;
import com.github.appreciated.apexcharts.config.*;
import com.github.appreciated.apexcharts.config.chart.Type;
import com.github.appreciated.apexcharts.config.chart.Zoom;
import com.github.appreciated.apexcharts.config.grid.Row;
import com.github.appreciated.apexcharts.config.stroke.Curve;
import com.github.appreciated.apexcharts.config.subtitle.Align;
import com.github.appreciated.apexcharts.helper.Series;
import com.storedobject.chart.SOChart;
import com.storedobject.chart.Title;
import com.storedobject.chart.TreeChart;
import com.storedobject.chart.TreeData;
import com.vaadin.flow.component.Unit;
import org.vaadin.addons.chartjs.ChartJs;
import org.vaadin.addons.chartjs.config.BarChartConfig;
import org.vaadin.addons.chartjs.data.BarDataset;
import org.vaadin.addons.chartjs.data.Dataset;
import org.vaadin.addons.chartjs.data.LineDataset;
import org.vaadin.addons.chartjs.options.Position;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class ChartGenerator {

    public static ChartJs generateChartJSBarChart(){
        BarChartConfig config = new BarChartConfig();
        config
                .data()
                .labels("January", "February", "March", "April", "May", "June", "July")
                .addDataset(new BarDataset().type().label("Dataset 1").backgroundColor("rgba(151,187,205,0.5)").borderColor("white").borderWidth(2))
                .addDataset(new LineDataset().type().label("Dataset 2").backgroundColor("rgba(151,187,205,0.5)").borderColor("white").borderWidth(2))
                .addDataset(new BarDataset().type().label("Dataset 3").backgroundColor("rgba(220,220,220,0.5)"))
                .and();

        config.
                options()
                .responsive(true)
                .title()
                .display(true)
                .position(Position.LEFT)
                .text("Chart.js Combo Bar Line Chart")
                .and()
                .done();

        List<String> labels = config.data().getLabels();
        for (Dataset<?, ?> ds : config.data().getDatasets()) {
            List<Double> data = new ArrayList<>();
            for (int i = 0; i < labels.size(); i++) {
                data.add((double) (Math.random() > 0.5 ? 1.0 : -1.0) * Math.round(Math.random() * 100));
            }

            if (ds instanceof BarDataset) {
                BarDataset bds = (BarDataset) ds;
                bds.dataAsList(data);
            }

            if (ds instanceof LineDataset) {
                LineDataset lds = (LineDataset) ds;
                lds.dataAsList(data);
            }
        }

        ChartJs chart1 = new ChartJs(config);
        chart1.setSizeFull();

        return chart1;
    }

    public static ApexCharts generateApexChartsLineChart(){
        // Our Apex chart
        ApexCharts apexChart = new ApexCharts();

        // Series
        Series<Integer> series = new Series<Integer>();
        series.setData(new Integer[] {10, 41, 35, 51, 49, 62, 69, 91, 148});
        series.setName("Desktops");

        // Chart
        Chart chart = new Chart();
        chart.setHeight("350");
        chart.setType(Type.line);
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
        TitleSubtitle titleSubtilte = new TitleSubtitle();
        titleSubtilte.setText("Product Trends by Month");
        titleSubtilte.setAlign(Align.left);

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
        apexChart.setTitle(titleSubtilte);
        apexChart.setGrid(grid);
        apexChart.setXaxis(xaxis);
        apexChart.setTooltip(tooltip);

        return apexChart;
    }

    public static SOChart generateSOChartTreeChart(){

        // Creating a chart display area
        SOChart soChart = new SOChart();
        //soChart.setSize("800px", "500px");
        soChart.setWidth(330, Unit.PIXELS);
        soChart.setHeight(400,Unit.PIXELS);
        // Tree chart
        // (By default it assumes circular shape. Otherwise, we can set orientation)
        // All values are randomly generated
        TreeChart tc = new TreeChart();
        TreeData td = new TreeData("Root", 1000);
        tc.setTreeData(td);
        Random r = new Random();
        for(int i = 1; i < 21; i++) {
            td.add(new TreeData("Node " + i, r.nextInt(500)));
        }
        TreeData td1 = td.get(13);
        td = td.get(9);
        for(int i = 50; i < 56; i++) {
            td.add(new TreeData("Node " + i, r.nextInt(500)));
        }
        for(int i = 30; i < 34; i++) {
            td1.add(new TreeData("Node " + i, r.nextInt(500)));
        }

        // Add to the chart display area with a simple title
        soChart.add(tc, new Title("A Circular Tree Chart"));

        return soChart;


    }
}
